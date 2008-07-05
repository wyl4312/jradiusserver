package radius.pool;

import radius.pool.ObjectFactory;
import radius.pool.ObjectPoolHashImpl;
import radius.pool.PoolConfig;
import junit.framework.TestCase;

public class ObjectPoolHashImplTest extends TestCase {
    private ObjectPoolHashImpl pool;

    private int test = 0;

    private int max = 5;

    private int min = 1;

    private int inc = 1;

    protected void setUp() throws Exception {
        super.setUp();
        PoolConfig config = new PoolConfig();
        config.setMinSize(min);
        config.setMaxSize(max);
        config.setIncrement(inc);
		config.setKeepAlive(false);
        ObjectFactory factory = new ObjectFactory() {

            public Object create() {
                return new Integer(test++);
            }

        };
        pool = new ObjectPoolHashImpl(config, factory);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        pool.destroy();
    }

    public void testGet() {
        Integer num = (Integer) pool.get();
        assertEquals(new Integer(0), num);
        pool.back(num);
        num = (Integer) pool.get();
        assertEquals(new Integer(0), num);
        pool.back(num);
        Integer[] n = new Integer[max];
        for (int i = 0; i < max; i++) {
            n[i] = (Integer) pool.get();
        }
        final long start = System.currentTimeMillis();
        new Thread() {
            public void run() {
                Integer i = (Integer) pool.get();
                long time = System.currentTimeMillis()-start;
                assertTrue((System.currentTimeMillis()-start)>=500);
                assertEquals(new Integer(0), i);
                pool.back(i);
            }
        }.start();
        while (true) {
            try {
                Thread.sleep(500);
                break;
            } catch (InterruptedException e) {

            }
        }
        for (int i = 0; i < max; i++) {
            pool.back(n[i]);
        }
    }
    
    public void testPerformence() {
        int count = 100000;
        Object obj;
        long start = System.currentTimeMillis();
        for (int i=0;i<count;i++) {
            obj = pool.get();
            pool.back(obj);
        }
        long time = System.currentTimeMillis()-start;
        double speed = (count*1.0/time);
        //System.out.println("speed:"+speed+"/ms");
        assertTrue(speed>1);
    }

    public void testBack() {
        Integer i = new Integer(0);
        Integer ii = (Integer) pool.get();
        assertEquals(0,pool.getIdleSize());
        pool.back(ii);
        assertEquals(1,pool.getIdleSize());
        Integer iii = (Integer) pool.get();
        assertNotSame(i,iii);
    }

    public void testDestroy() {
        pool.destroy();
        assertFalse(pool.isAlive());
        try {
            pool.get();
            fail();
        }catch (Exception e) {
            
        }
    }

}
