package radius.util;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class TaskThreadTest extends TestCase {
    private TaskThread tt;

    private ArrayList list;

    private int count = 50;

    protected void setUp() throws Exception {
        super.setUp();
        tt = new TaskThread();
        list = new ArrayList();
        for (int i = 0; i < count; i++) {
            addTask();
        }
    }
    
    private void addTask() {
        tt.addTask(new Runnable() {

            public void run() {
                list.add(new Object());
            }

        });
    }

    public void testStart() {
        assertTrue(tt.getCurrentTaskCount() == count);
        tt.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        assertEquals(count, list.size());
        for (int i = 0; i < count; i++) {
            addTask();
        }
        tt.stop();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        assertEquals(count*2, list.size());
        assertFalse(tt.isRunning());
    }

    public void testStop() {
        tt.start();
        tt.stop();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        assertFalse(tt.isRunning());
    }
    
    public void testSpeed() {
        long start = System.currentTimeMillis();
        tt.start();
        for (int i=0;i<10000;i++) {
            addTask();
        }
        long end = System.currentTimeMillis();
        int size = list.size(); 
        System.out.println(size/((end-start)));//84 tasks/ms in my computer
        assertTrue(size/((end-start))>1);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        tt.stop();
    }
}