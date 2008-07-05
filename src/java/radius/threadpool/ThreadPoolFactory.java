/*
 * Created on 2004-8-20
 *
 */
package radius.threadpool;

import radius.threadpool.impl.ThreadPoolImpl;

import org.apache.commons.logging.*;
/**
 * @author <a href="mailto:starfire@bit.edu.cn">starfire </a>
 *  
 */
public class ThreadPoolFactory {
    public static final String THREAD_POOL_IMPL_PROPERTY = "radius.threadpool.impl";
    public static final String DEFAULT_THREAD_POOL_IMPL = "radius.threadpool.impl.ConcurrentThreadPoolImpl";

	private static Log log = LogFactory.getLog(ThreadPoolFactory.class);
    private static ThreadPool pool;
    private static Object lock = new Object();
    
    private static Class poolImpl;
    static {
        String clazzName = System.getProperty(THREAD_POOL_IMPL_PROPERTY,DEFAULT_THREAD_POOL_IMPL);
		log.info("ThreadPool implement class:"+clazzName);
        try {
            poolImpl = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
			log.warn("can't load threadpool impl:"+e.getMessage());
            poolImpl = ThreadPoolImpl.class;
        }
    }
    
    private ThreadPoolFactory() {

    }
    
    public static ThreadPool getDefaultPool() {
        if (pool==null) {
            synchronized(lock) {
                if (pool==null) {
                    ThreadPoolConfig config = new ThreadPoolConfig();
                    pool = createThreadPool(config);
                }
            }
        }
        return pool;
    }

    public static ThreadPool createThreadPool(ThreadPoolConfig config) {
        ThreadPool pool = null;
        try {
            pool = (ThreadPool) poolImpl.newInstance();
        } catch (Exception e) {
            pool = new ThreadPoolImpl();
        } 
        pool.open(config);
        return pool;
    }

}

