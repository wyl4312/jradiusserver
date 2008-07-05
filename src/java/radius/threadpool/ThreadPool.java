package radius.threadpool;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public interface ThreadPool {
    
    ThreadPoolConfig getConfig();
    
    int getCurrentThreads();
    
    void open(ThreadPoolConfig config);

    /**
     * if pool has idle thread,task will been executed immediately,<br>
     * else task will been executed when another thread finished his task.
     * 
     * @param task
     */
    void execute(Runnable task);
    
    void dispose();
    
}
