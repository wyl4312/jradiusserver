package radius.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import radius.util.Queue;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 * 
 */
public class NetworkEngine implements Runnable {
	private final Selector selector;

	private final Queue waitCloseQueue;

	private final Queue waitRegisterQueue;

	private final Queue waitAddInterestQueue;

	private final Thread processorThread;

	private volatile boolean shutdown = false;
	
	private volatile boolean pause = false;

//	private final static NetworkEngine instance;
//
//	static {
//		try {
//			instance = new NetworkEngine();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public static NetworkEngine getDefaultInstance() {
//		return instance;
//	}

	public NetworkEngine() throws IOException {
		selector = Selector.open();
		waitCloseQueue = new Queue();
		waitRegisterQueue = new Queue();
		waitAddInterestQueue = new Queue();
		processorThread = new Thread(this, this.getClass().getName());
		// processorThread.setDaemon(true);//is this needed?
		processorThread.start();
	}

	/**
	 * @return Returns the selector.
	 */
	public Selector getSelector() {
		return selector;
	}

	public void register(SelectableChannel sc, Handler handler, int ops) {
		if (Thread.currentThread() == processorThread) {
			doRegister(sc, handler, ops);
		} else {
			ChannelAssociater r = new ChannelAssociater(sc, handler, ops);
			synchronized (waitRegisterQueue) {
				waitRegisterQueue.push(r);
				selector.wakeup();
			}
		}
	}

	public void addInterestOps(SelectableChannel sc, int addOps) {
		if (Thread.currentThread() == processorThread) {
			addOps(sc, addOps);
		} else {
			ChannelAssociater r = new ChannelAssociater(sc, null, addOps);
			synchronized (waitAddInterestQueue) {
				waitAddInterestQueue.push(r);
				selector.wakeup();
			}
		}
	}

	public void closeChannel(SelectableChannel sc) {
		if (Thread.currentThread() == processorThread) {
			doClose(sc);
		} else {
			synchronized (waitCloseQueue) {
				waitCloseQueue.push(sc);
				selector.wakeup();
			}
		}
	}

	protected void doRegister(SelectableChannel sc, Handler handler, int ops) {
		if (Thread.currentThread() == processorThread) {
			try {
				sc.register(selector, ops, handler);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}
	}

	protected void addOps(SelectableChannel sc, int addOps) {
		if (Thread.currentThread() == processorThread) {
			SelectionKey key = sc.keyFor(selector);
			if (key != null) {
				key.interestOps(key.interestOps() | addOps);
			}
		}
	}

	protected void doClose(SelectableChannel sc) {
		if (Thread.currentThread() == processorThread) {
			try {
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void dealRegister() {
		if (Thread.currentThread() == processorThread) {
			synchronized (waitRegisterQueue) {
				while (!waitRegisterQueue.isEmpty()) {
					ChannelAssociater ca = (ChannelAssociater) waitRegisterQueue
							.shift();
					doRegister(ca.sc, ca.handler, ca.ops);
				}
			}
		}
	}

	protected void dealAddInterest() {
		if (Thread.currentThread() == processorThread) {
			synchronized (waitAddInterestQueue) {
				while (!waitAddInterestQueue.isEmpty()) {
					ChannelAssociater ca = (ChannelAssociater) waitAddInterestQueue
							.shift();
					addOps(ca.sc, ca.ops);
				}
			}
		}
	}

	protected void dealClose() {
		if (Thread.currentThread() == processorThread) {
			synchronized (waitCloseQueue) {
				while (!waitCloseQueue.isEmpty()) {
					SelectableChannel sc = (SelectableChannel) waitCloseQueue
							.shift();
					doClose(sc);
				}
			}
		}
	}

	protected void dealShutdown() {
		Iterator iterator = selector.keys().iterator();
		while (iterator.hasNext()) {
			try {
				SelectionKey key = (SelectionKey) iterator.next();
				key.channel().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			selector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		shutdown = true;
		selector.wakeup();
	}
	
	public void pause() {
		pause = true;
		selector.wakeup();
	}
	
	public void goOn() {
		pause = false;
	}

	public void run() {
		int keyCount = 0;
		while (!shutdown) {
			dealRegister();
			dealAddInterest();
			dealClose();
			while (pause && !shutdown) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			if (shutdown) {
				break;
			}
			try {
				keyCount = selector.select();
				if (keyCount == 0) {
					continue;
				}
				Iterator iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					SelectionKey key = (SelectionKey) iterator.next();
					iterator.remove();
					key.interestOps(key.interestOps() & ~key.readyOps());
					Handler handler = (Handler) key.attachment();
					if (key.isAcceptable()) {
						((AcceptHandler) handler).handleAccept();

					} else if (key.isConnectable()) {
						((ConnectHandler) handler).handleConnect();

					} else {
						ReadWriteHandler rwh = ((ReadWriteHandler) handler);
						if (key.isValid() && key.isReadable()) {
							rwh.handleRead();
						} else if (key.isValid() && key.isWritable()) {
							rwh.handleWrite();
						}
					}
				}
			} catch (ClosedSelectorException cse) {
				System.err.println("selector closed:" + cse.getMessage()
						+ "\nquit");
				//TODO
				return;
			} catch (IOException e) {
				e.printStackTrace();
				//TODO
			}
		}
		dealShutdown();
	}

	private static class ChannelAssociater {
		SelectableChannel sc;

		Handler handler;

		int ops;

		ChannelAssociater(SelectableChannel sc, Handler handler, int ops) {
			this.sc = sc;
			this.handler = handler;
			this.ops = ops;
		}
	}

}