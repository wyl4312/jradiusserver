package radius.util;

/**
 * before or when task thread start,you can add task.<br>
 * if stop() method been called,thread will finish all task then quit. 
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class TaskThread implements Runnable {
    private Queue taskQueue = new Queue();

    private volatile boolean stop = false;

    private boolean started = false;
    
    private Thread thread;
    
    private static TaskThread defaultInstance = null;//default back thread
    
    public static TaskThread getDefaultInstance() {
        if (defaultInstance==null) {
            synchronized(TaskThread.class) {
                defaultInstance = new TaskThread();
            }
        }
        return defaultInstance;
    }

    public void addTask(Runnable task) {
        if (stop) {
            return;
        }
        synchronized (taskQueue) {
            taskQueue.push(task);
            taskQueue.notify();
        }
    }

    public void start() {
        if (!started) {
            thread = new Thread(this);
            thread.start();
            started = true;
        }
    }

    public void run() {
        Runnable task = null;
        while (!stop || !taskQueue.isEmpty()) {
            synchronized (taskQueue) {
                if (!taskQueue.isEmpty()) {
                    task = (Runnable) taskQueue.shift();
                }
            }
            try {
                if (task != null)
                    task.run();
            } catch (Throwable e) {
                e.printStackTrace();
                //ignore??
            }
            if (taskQueue.isEmpty() && !stop) {
                synchronized(taskQueue) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        //do something?
                    }
                }
            }
        }
    }
    
    public int getCurrentTaskCount() {
        return taskQueue.size();
    }
    
    public boolean isRunning() {
        return thread!=null && thread.isAlive();
    }

    public void stop() {
        this.stop = true;
        synchronized(taskQueue) {
            taskQueue.notify();
        }
    }
}