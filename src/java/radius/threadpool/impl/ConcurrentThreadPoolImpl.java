/*
 * 创建日期 2005-5-4
 *
 */
package radius.threadpool.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.threadpool.ThreadPool;
import radius.threadpool.ThreadPoolConfig;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public class ConcurrentThreadPoolImpl implements ThreadPool {
    private Log log = LogFactory.getLog(ConcurrentThreadPoolImpl.class);
    private ThreadPoolConfig config;
    private PooledExecutor executor;

    /* （非 Javadoc）
     * @see radius.threadpool.ThreadPool#getConfig()
     */
    public ThreadPoolConfig getConfig() {
        return config;
    }

    /* （非 Javadoc）
     * @see radius.threadpool.ThreadPool#getCurrentThreads()
     */
    public int getCurrentThreads() {
        return config.getMaxSize();
    }

    /* （非 Javadoc）
     * @see radius.threadpool.ThreadPool#open(radius.threadpool.ThreadPoolConfig)
     */
    public void open(ThreadPoolConfig config) {
        if (executor!=null)
            return;
        log.info("open concurrent threadpool");
        assert config!=null;
        this.config = config;
        executor = new PooledExecutor(new LinkedQueue());
        executor.setMinimumPoolSize(config.getMinSize());
        executor.setMaximumPoolSize(config.getMaxSize());
        executor.setKeepAliveTime(-1);
        executor.createThreads(config.getMaxSize());
    }

    /* （非 Javadoc）
     * @see radius.threadpool.ThreadPool#execute(java.lang.Runnable)
     */
    public void execute(Runnable task) {
        try {
            executor.execute(task);
        } catch (InterruptedException e) {
            log.warn("execute task failed:"+e.getMessage(),e);
        }
    }

    /* （非 Javadoc）
     * @see radius.threadpool.ThreadPool#dispose()
     */
    public void dispose() {
        executor.shutdownNow();
        executor = null;
        config = null;
    }

}
