package radius.threadpool;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public class ThreadPoolImplTest extends TestCase {

    private ThreadPool tp;
    private List list = new ArrayList();
    
    protected void setUp() {
        tp = ThreadPoolFactory.createThreadPool(new ThreadPoolConfig());
    }
    
    public void testExecute() {
        long start = System.currentTimeMillis();
        for (int i=0;i<10000;i++) {
            addTask();
        }
        long end = System.currentTimeMillis();
        assertEquals(tp.getConfig().getMaxSize(),tp.getCurrentThreads());
        assertTrue(list.size()/(end-start)>0);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
        end = System.currentTimeMillis();
        assertEquals(tp.getConfig().getMaxSize(),tp.getCurrentThreads());
        for (int i=0;i<10000;i++) {
            addTask();
        }
        assertEquals(tp.getConfig().getMaxSize(),tp.getCurrentThreads());
        tp.dispose();
        assertEquals(0,tp.getCurrentThreads());
    }
    
    private void addTask() {
        tp.execute(new Runnable() {

            public void run() {
                list.add(new Object());
            }
        
        });
    }

}
