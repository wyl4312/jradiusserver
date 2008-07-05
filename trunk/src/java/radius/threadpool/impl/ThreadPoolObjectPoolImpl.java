package radius.threadpool.impl;

import radius.pool.DefaultObjectPoolFactory;
import radius.pool.ObjectFactory;
import radius.pool.ObjectLifeCycle;
import radius.pool.ObjectPool;
import radius.pool.PoolConfig;
import radius.threadpool.ThreadPool;
import radius.threadpool.ThreadPoolConfig;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ThreadPoolObjectPoolImpl implements ThreadPool, BackWorker {
	private ThreadPoolConfig config;

	private ObjectPool pool;

	private PoolConfig poolConfig;

	public ThreadPoolObjectPoolImpl() {

	}

	public ThreadPoolConfig getConfig() {
		return config;
	}

	public int getCurrentThreads() {
		if (pool != null) {
			return pool.getCurrentSize();
		} else {
			return 0;
		}
	}

	public void open(ThreadPoolConfig config) {
		this.config = config;
		poolConfig = new PoolConfigAdapter(config);
		pool = DefaultObjectPoolFactory.getInstance().createPool(poolConfig,
				new ObjectFactory() {

					public Object create() {
						return new Worker(ThreadPoolObjectPoolImpl.this);
					}

				}, new ObjectLifeCycle() {

					public void active(Object obj) {

					}

					public void passive(Object obj) {

					}

					public void keepAlive(Object obj) {

					}

					public void destroy(Object obj) {
						Worker worker = (Worker) obj;
						worker.dispose();
					}

				});
	}

	public void execute(Runnable task) {
		Worker worker = (Worker) pool.get();
		worker.work(task);
	}

	public void dispose() {
		if (pool != null) {
			pool.destroy();
			pool = null;
		}
	}

	public void backWorker(Worker worker) {
		if (pool != null) {
			pool.back(worker);
		}
	}

	private static class PoolConfigAdapter extends PoolConfig {
		private ThreadPoolConfig tpc;

		public PoolConfigAdapter(ThreadPoolConfig tpc) {
			this.tpc = tpc;
		}

		public int getIncrement() {
			return tpc.getIncreament();
		}

		public int getMinSize() {
			return tpc.getMinSize();
		}

		public int getMaxSize() {
			return tpc.getMaxSize();
		}

		public boolean isKeepAlive() {
			return false;
		}
	}

}
