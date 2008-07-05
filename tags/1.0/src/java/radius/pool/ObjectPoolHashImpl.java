package radius.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ObjectPoolHashImpl implements ObjectPool {
	protected ObjectFactory objectFactory;

	protected ObjectLifeCycle objectLifeCycle;

	protected PoolConfig poolConfig;

	protected LinkedList idleObjects;

	protected HashMap busyObjects;

	protected Object lock;

	protected volatile int currentSize;

	protected volatile int idleSize;

	protected volatile boolean alive = true;// not destroy

	protected KeepAliveThread keepAliveThread;

	ObjectPoolHashImpl(PoolConfig config, ObjectFactory factory) {
		this(config, factory, null);
	}

	ObjectPoolHashImpl(PoolConfig config, ObjectFactory factory,
			ObjectLifeCycle lifeCycle) {
		assert config != null && config.isValid() : "poolConfig is invalid";
		assert factory != null : "object factory can't be null";
		poolConfig = config;
		objectFactory = factory;
		objectLifeCycle = lifeCycle;
		idleObjects = new LinkedList();
		busyObjects = new HashMap();
		lock = new Object();
		initPool();
	}

	protected void initPool() {
		int minSize = poolConfig.getMinSize();
		int maxSize = poolConfig.getMaxSize();
		for (int i = 0; i < minSize; i++) {
			createObject();
		}
		idleSize = minSize;
		if (poolConfig.isKeepAlive()) {
			keepAliveThread = new KeepAliveThread(this);
			keepAliveThread.start();
		}
	}

	protected void createObject() {
		Object value = objectFactory.create();
		ObjectFacade facade = new ObjectFacade(value);
		idleObjects.addLast(facade);
		currentSize++;
	}

	protected void ensureEnoughObjects() {
		int cur = currentSize;
		int max = poolConfig.getMaxSize();
		if (idleObjects.isEmpty() && cur < max) {
			int inc = poolConfig.getIncrement();
			if (inc + cur > max) {
				inc = max - cur;
			}
			for (int i = 0; i < inc; i++) {
				createObject();
			}
		}
	}

	protected void waitForObject() {
		try {
			lock.wait();
		} catch (InterruptedException e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pool.ObjectPool#get()
	 */
	public Object get() {
		synchronized (lock) {
			ensureEnoughObjects();
			ObjectFacade facade = null;
			while (idleObjects.isEmpty() && alive) {
				waitForObject();
			}
			if (!alive) {
				throw new IllegalStateException("pool has been destroyed");
			}
			facade = (ObjectFacade) idleObjects.removeFirst();
			Object obj = facade.get();
			busyObjects.put(obj, facade);
			idleSize--;
			if (objectLifeCycle != null) {
				objectLifeCycle.active(obj);
			}
			return obj;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pool.ObjectPool#back(java.lang.Object)
	 */
	public void back(Object obj) {
		synchronized (lock) {
			ObjectFacade facade = (ObjectFacade) busyObjects.get(obj);
			if (facade != null) {
				if (facade.getValue() != obj) {
					return;
				}
				if (alive) {
					if (objectLifeCycle != null) {
						objectLifeCycle.passive(obj);
					}
					facade.back(obj);
					idleObjects.addLast(facade);
					idleSize++;
					lock.notify();
				} else {
					if (objectLifeCycle != null) {
						objectLifeCycle.destroy(obj);
						busyObjects.remove(obj);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pool.ObjectPool#isAlive()
	 */
	public boolean isAlive() {
		return alive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pool.ObjectPool#getCurrentSize()
	 */
	public int getCurrentSize() {
		return currentSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pool.ObjectPool#getIdleSize()
	 */
	public int getIdleSize() {
		return idleSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pool.ObjectPool#getPoolConfig()
	 */
	public PoolConfig getPoolConfig() {
		return poolConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pool.ObjectPool#destroy()
	 */
	public void destroy() {
		if (!alive) {
			return;
		}
		if (keepAliveThread != null) {
			keepAliveThread.destroy();
			keepAliveThread = null;
		}
		synchronized (lock) {
			Iterator iterator = idleObjects.iterator();
			ObjectFacade facade = null;
			while (iterator.hasNext()) {
				facade = (ObjectFacade) iterator.next();
				if (objectLifeCycle != null) {
					objectLifeCycle.destroy(facade.getValue());
				}
			}
			idleObjects.clear();
		}
		alive = false;
	}

	static class KeepAliveThread implements Runnable {
		private ObjectPoolHashImpl pool;

		private volatile boolean stop = false;

		public KeepAliveThread(ObjectPoolHashImpl pool) {
			this.pool = pool;
		}

		public void start() {
			Thread t = new Thread(this);
			t.start();
		}

		public void run() {
			ObjectLifeCycle lifeCycle = pool.objectLifeCycle;
			while (!stop && lifeCycle != null) {
				int idleSize = pool.getIdleSize();
				for (int i = 0; i < idleSize; i++) {
					synchronized (pool.lock) {
						if (!pool.idleObjects.isEmpty()) {
							Object obj = pool.get();
							lifeCycle.keepAlive(obj);
							pool.back(obj);
						}
					}
				}
				try {
					int keepAliveTime = pool.poolConfig.getKeepAliveIdleTime();
					if (keepAliveTime <= 0) {
						keepAliveTime = 10;
					}
					Thread.sleep(keepAliveTime);
				} catch (InterruptedException e) {
				}
			}
		}

		public void destroy() {
			stop = true;
		}

	}
}
