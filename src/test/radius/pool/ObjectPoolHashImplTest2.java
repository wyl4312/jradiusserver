package radius.pool;

import radius.pool.ObjectFactory;
import radius.pool.ObjectLifeCycle;
import radius.pool.ObjectPoolHashImpl;
import radius.pool.PoolConfig;
import junit.framework.TestCase;

public class ObjectPoolHashImplTest2 extends TestCase {
	private ObjectPoolHashImpl pool;

	private int max = 5;

	private int min = 1;

	private int inc = 1;

	static class TestClass {

		static int nextHashCode = 0;

		static int incHashCode = 67;

		private int hash;
		
		private int value;

		TestClass() {
			hash = nextHashCode + incHashCode;
			nextHashCode = hash;
			value = 17;
		}

		public int hashCode() {
			return hash;
		}

		public int getValue() {
			return value;
		}
		

		public void setValue(int value) {
			this.value = value;
		}
		
		public void incValue() {
			value++;
		}
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		PoolConfig config = new PoolConfig();
		config.setMinSize(min);
		config.setMaxSize(max);
		config.setIncrement(inc);
		config.setKeepAlive(true);
		config.setKeepAliveIdleTime(10);
		ObjectFactory factory = new ObjectFactory() {

			public Object create() {
				TestClass tc = new TestClass();
				tc.setValue(18);
				return tc;
			}

		};
		ObjectLifeCycle lifeCycle = new ObjectLifeCycle() {

			public void active(Object obj) {
			}

			public void passive(Object obj) {
			}

			public void keepAlive(Object obj) {
				TestClass tc = (TestClass) obj;
				tc.incValue();
			}

			public void destroy(Object obj) {
				TestClass tc = (TestClass) obj;
				tc.setValue(17);
			}

		};
		pool = new ObjectPoolHashImpl(config, factory, lifeCycle);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		pool.destroy();
	}

	public void testLifeCycle() {
		TestClass tc = (TestClass) pool.get();
		assertEquals(18, tc.getValue());
		pool.back(tc);
		assertEquals(1, pool.getIdleSize());
		tc = (TestClass) pool.get();
		assertEquals(18, tc.getValue());
		pool.back(tc);

		assertEquals(1, pool.getIdleSize());
		while (true) {
			try {
				Thread.sleep(100);
				break;
			} catch (InterruptedException e) {
			}
		}

		tc = (TestClass) pool.get();
		assertTrue(tc.getValue()>=18+100/10);
		pool.back(tc);
		
		while (true) {
			try {
				Thread.sleep(100);
				break;
			} catch (InterruptedException e) {
			}
		}
		
		long start = System.currentTimeMillis();
		long cur = start;
		int count = 0;
		while ((cur-start)<100) {
			count++;
			tc = (TestClass) pool.get();
			pool.back(tc);
			cur = System.currentTimeMillis();
		}
		assertTrue(pool.getCurrentSize()>=1);
	}

}
