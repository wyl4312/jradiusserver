package radius.server.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

import radius.Radius;
import radius.RadiusPacket;
import radius.nio.NetworkEngine;
import radius.nio.ReadWriteHandler;
import radius.server.AbstractRadiusServer;
import radius.server.RadiusContext;
import radius.util.ConvertUtil;
import radius.util.Queue;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class NioServer extends AbstractRadiusServer {

    private NetworkEngine engine;

    private DriverHandler authHandler;

    private DriverHandler accountHandler;

    public NioServer() {
        try {
            engine = new NetworkEngine();
            log.info("create nio network engine success");
        } catch (IOException e) {
            log.fatal("create nio network engine faild:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*
     * @see radius.server.RadiusDriver#start()
     */
    public boolean start() {
        if (!started) {
            assert this.contextFactory != null : "context factory can't be null";
            log.info("starting server...");
            try {
                if (authAddress != null) {
                    authHandler = new DriverHandler();
                    authHandler.start(authAddress);
                }
                if (accountAddress != null) {
                    accountHandler = new DriverHandler();
                    accountHandler.start(accountAddress);
                }
                started = true;
                log.info("server started.");
                return true;
            } catch (IOException e) {
                log.fatal("start server failed", e);
                return false;
            }
        }
        return true;
    }

    private class DriverHandler implements ReadWriteHandler {
        //private NioServer server;

        private DatagramChannel dc;

        private SocketAddress localAddress;

        private Queue writeQueue;

        private ByteBuffer directBuffer;

        public DriverHandler() {
            writeQueue = new Queue();
            directBuffer = ByteBuffer
                    .allocateDirect(RadiusPacket.MAX_PACKET_LENGTH);
        }

        public void start(SocketAddress localAddress) throws IOException {
            dc = DatagramChannel.open();
            dc.socket().setReuseAddress(true);
            dc.socket().setReceiveBufferSize(RadiusPacket.MAX_PACKET_LENGTH);
            dc.socket().setSendBufferSize(RadiusPacket.MAX_PACKET_LENGTH);
            dc.socket().bind(localAddress);
            dc.configureBlocking(false);
            engine.register(dc, this, SelectionKey.OP_READ);
            this.localAddress = localAddress;
        }

        public void stop() {
            if (dc != null) {
                engine.closeChannel(dc);
            }
        }

        public void handleRead() {
            try {
                SocketAddress sourceAddress = dc.receive(directBuffer);
                if (sourceAddress == null)
                    return;
                if (log.isDebugEnabled()) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("raw data:");
                    ConvertUtil.toHex(directBuffer, sb);
                    log.debug(sb.toString());
                }
                RadiusContext context = contextFactory.getContext(directBuffer,
                        sourceAddress, localAddress, NioServer.this);
                if (localAddress == authAddress) {
                    context.setServiceType(Radius.AUTH_SERVICE_TYPE);
                } else {
                    context.setServiceType(Radius.ACCOUNT_SERVICE_TYPE);
                }
                consumer.consume(context);
                directBuffer.clear();
            } catch (IOException e) {
                log.warn("read data failed:" + e.getMessage());
            } finally {
                engine.addInterestOps(dc, SelectionKey.OP_READ);
            }
        }

        public void handleWrite() {
            synchronized (writeQueue) {
                if (!writeQueue.isEmpty()) {
                    WriteData wd = (WriteData) writeQueue.shift();
                    try {
                        if (wd.data.hasRemaining()) {
                            dc.send(wd.data, wd.target);
                            if (wd.data.hasRemaining()) {
                                writeQueue.unshift(wd);
                            }
                        }
                    } catch (IOException e) {
                        log.warn("write data failed:" + e.getMessage());
                    }
                }
                if (!writeQueue.isEmpty()) {
                    engine.addInterestOps(dc, SelectionKey.OP_WRITE);
                }
            }
        }

        public void close() {
            engine.closeChannel(dc);
        }

        public void send(ByteBuffer data, SocketAddress targetAddress) {
            try {
                int pos = data.position();
                data.flip();
                synchronized (dc) {
                    dc.send(data, targetAddress);
                }
                if (data.hasRemaining()) {// may be this is not needed
                    ByteBuffer buf = ByteBuffer.allocate(data.remaining());
                    buf.put(data);
                    synchronized (writeQueue) {
                        WriteData wd = new WriteData(targetAddress, buf);
                        writeQueue.push(wd);
                        engine.addInterestOps(dc, SelectionKey.OP_WRITE);
                    }
                }
                data.clear();
                data.position(pos);
            } catch (IOException e) {
                log.warn("send packet failed:" + e.getMessage());
            }
        }
    }

    public boolean pause() {
        engine.pause();
        return super.pause();
    }

    public boolean goOn() {
        engine.goOn();
        return super.goOn();
    }

    public boolean stop() {
        if (!stopped) {
            if (authHandler != null) {
                authHandler.close();
                authHandler = null;
            }
            if (accountHandler != null) {
                accountHandler.close();
                accountHandler = null;
            }
            stopped = true;
            started = false;
        }
        return stopped;
    }

    private static class WriteData {
        SocketAddress target;

        ByteBuffer data;

        public WriteData(SocketAddress target, ByteBuffer data) {
            this.target = target;
            this.data = data;
        }
    }

    protected void doConsume(SocketAddress targetAddress,
            ByteBuffer resultBuffer, SocketAddress localAddress) {
        if (localAddress.equals(authAddress)) {
            authHandler.send(resultBuffer, targetAddress);
        } else if (localAddress.equals(accountAddress)) {
            authHandler.send(resultBuffer, accountAddress);
        } else {
            log.warn("localaddress not created,can't consume packet");
        }
    }

}