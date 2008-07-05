package radius.threadpool.impl;

import org.apache.commons.logging.*;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
class Worker implements Runnable {
	private static int NEXT_HASH = 0;

	private final static int HASH_INCREMENT = 0x2717;

	private Log log = LogFactory.getLog(Worker.class);

	private Runnable task;

	private Object lock = new Object();

	private volatile boolean idle = true;

	private volatile boolean stop = false;

	private int hashCode;

	private BackWorker backer;

	public Worker(BackWorker backer) {
		this.backer = backer;
		synchronized (Worker.class) {
			hashCode = NEXT_HASH;
			NEXT_HASH += HASH_INCREMENT;
		}
		new Thread(this).start();

	}

	/*
	 * £¨·Ç Javadoc£©
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Runnable temp;
		while (!stop) {
			synchronized (lock) {
				if (task == null) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
					}
				}
				temp = task;
				task = null;
			}
			if (temp != null) {
				try {
					temp.run();
				} catch (Throwable e) {
					log.warn("exception:",e);
				}
				idle = true;
				backer.backWorker(this);
			}
		}
	}

	public void work(Runnable task) {
		synchronized (lock) {
			this.task = task;
			lock.notify();
		}
	}

	public boolean isIdle() {
		return idle;
	}

	public void dispose() {
		stop = true;
		synchronized (lock) {
			lock.notify();
		}
	}

	public int hashCode() {
		return hashCode;
	}

}