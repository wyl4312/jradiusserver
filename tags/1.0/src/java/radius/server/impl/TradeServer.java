/*
 * 创建日期 2005-5-5
 *
 */
package radius.server.impl;

import java.io.IOException;
import java.net.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import radius.Radius;
import radius.RadiusPacket;
import radius.server.AbstractRadiusServer;
import radius.server.RadiusContext;
import radius.util.ConvertUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class TradeServer extends AbstractRadiusServer {

    private TradeNetworkEngine authEngine;

    private TradeNetworkEngine accountEngine;

    public TradeServer() {
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.server.AbstractRadiusServer#doConsume(java.net.SocketAddress,
     *      java.nio.ByteBuffer, java.net.SocketAddress)
     */
    protected void doConsume(SocketAddress targetAddress,
            ByteBuffer resultBuffer, SocketAddress localAddress) {
        if (localAddress.equals(authAddress)) {
            authEngine.send(resultBuffer, targetAddress);
        } else if (localAddress.equals(accountAddress)) {
            authEngine.send(resultBuffer, accountAddress);
        } else {
            log.warn("localaddress not created,can't consume packet");
        }
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.server.RadiusServer#start()
     */
    public boolean start() {
        if (!started) {
            assert this.contextFactory != null : "context factory can't be null";
            log.info("starting server...");
            try {
                if (authAddress != null) {
                    authEngine = new TradeNetworkEngine();
                    authEngine.start(authAddress);
                }
                if (accountAddress != null) {
                    accountEngine = new TradeNetworkEngine();
                    accountEngine.start(accountAddress);
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

    private class TradeNetworkEngine implements Runnable {
        private DatagramSocket ds;

        private volatile boolean stop;

        private volatile boolean pause;

        private SocketAddress localAddress;

        private DatagramPacket sendPacket;

        public TradeNetworkEngine() {
            byte[] buf = new byte[RadiusPacket.MAX_PACKET_LENGTH];
            sendPacket = new DatagramPacket(buf, buf.length);
        }

        public void start(SocketAddress address) throws IOException {
            ds = new DatagramSocket();
            this.localAddress = address;
            ds.bind(address);
            Thread t = new Thread(this);
            t.start();
        }

        public void run() {
            ByteBuffer bb = ByteBuffer.allocate(RadiusPacket.MAX_PACKET_LENGTH);
            DatagramPacket dp = new DatagramPacket(bb.array(),
                    RadiusPacket.MAX_PACKET_LENGTH);
            while (!stop) {
                while (pause) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                }
                try {
                    ds.receive(dp);
                    bb.position(dp.getLength());
                    SocketAddress source = dp.getSocketAddress();
                    RadiusContext ctx = contextFactory.getContext(bb, source,
                            this.localAddress, TradeServer.this);
                    if (localAddress == authAddress) {
                        ctx.setServiceType(Radius.AUTH_SERVICE_TYPE);
                    } else {
                        ctx.setServiceType(Radius.ACCOUNT_SERVICE_TYPE);
                    }
                    consumer.consume(ctx);
                } catch (IOException e) {
                    log.info("receive data failed:" + e.getMessage(), e);
                } catch (Throwable e) {
                    log.error("unknown expected error:" + e.getMessage(), e);
                }
                bb.clear();
            }
        }

        public void stop() {
            stop = true;
        }

        public void pause() {
            pause = true;
        }

        public void goOn() {
            pause = false;
        }

        public void send(ByteBuffer bb, SocketAddress target) {
            synchronized (ds) {
                int pos = bb.position();
                bb.flip();
                bb.get(sendPacket.getData());
                sendPacket.setLength(pos);
                sendPacket.setSocketAddress(target);
                try {
                    ds.send(sendPacket);
                } catch (IOException e) {
                    log.error("send packet to (" + target + ") failed:"
                            + e.getMessage() + ",data=\r\n"
                            + ConvertUtil.toHex(bb), e);
                }
                bb.clear();
                bb.position(pos);
            }
        }
    }
    
    public boolean pause() {
        if (authEngine!=null)
            authEngine.pause();
        if (accountEngine!=null)
            accountEngine.pause();
        return super.pause();
    }

    public boolean goOn() {
        if (authEngine!=null)
            authEngine.goOn();
        if (accountEngine!=null)
            accountEngine.goOn();
        return super.goOn();
    }

    public boolean stop() {
        if (!stopped) {
            if (authEngine!=null)
                authEngine.stop();
            if (accountEngine!=null)
                accountEngine.stop();
            stopped = true;
            started = false;
        }
        return stopped;
    }
}