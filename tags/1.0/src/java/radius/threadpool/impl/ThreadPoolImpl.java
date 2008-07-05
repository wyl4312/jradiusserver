/*
 * Created on 2004-8-20
 *
 */
package radius.threadpool.impl;

import java.util.HashMap;
import java.util.Iterator;

import radius.threadpool.ThreadPool;
import radius.threadpool.ThreadPoolConfig;
import radius.util.Queue;

/**
 * @author <a href="mailto:starfire@bit.edu.cn">starfire </a>
 *  
 */
public class ThreadPoolImpl implements ThreadPool,BackWorker {
    private ThreadPoolConfig config;

    private HashMap busyWorkers;

    private Queue idleWorkers;

    private Queue tasks;

    private Object poolLock;

    private volatile int threads;

    private volatile boolean opened = false;

    private volatile boolean disposed = false;

    public ThreadPoolImpl() {

    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.threadpool.ThreadPool#execute(java.lang.Runnable)
     */
    public void execute(Runnable task) {
        if (!opened || disposed) {
            //FIXME log or throw runtime exception
            return;
        }
        synchronized (poolLock) {
            ensureCapacity();
            if (!idleWorkers.isEmpty()) {
                Worker worker = (Worker) idleWorkers.shift();
                busyWorkers.put(worker,worker);
                worker.work(task);
            } else {
                tasks.push(task);
            }
        }
    }

    private void ensureCapacity() {
        if (idleWorkers.isEmpty() && threads < config.getMaxSize()) {
            for (int i = 0; i < config.getIncreament()
                    && threads < config.getMaxSize(); i++) {
                Worker worker = new Worker(this);
                idleWorkers.push(worker);
                threads++;
            }
        }
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.threadpool.ThreadPool#setConfig(radius.threadpool.ThreadPoolConfig)
     */
    public void open(ThreadPoolConfig config) {
        if (opened)
            throw new IllegalStateException(
                    "thread pool has been opened,can't open again");
        if (config.getMaxSize() < config.getMinSize()) {
            throw new IllegalArgumentException(
                    "ThreadPoolConfig is invalid,maxsize must >= minsize");
        }
        if (config.getMinSize() <= 0) {
            throw new IllegalArgumentException(
                    "ThreadPoolConfig is invalid,minsize must > 0");
        }
        this.config = config;
        init();
    }

    private void init() {
        opened = true;
        poolLock = new Object();
        busyWorkers = new HashMap();
        idleWorkers = new Queue();
        tasks = new Queue();
        for (int i = 0; i < config.getMinSize(); i++) {
            idleWorkers.push(new Worker(this));
            threads++;
        }
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.threadpool.ThreadPool#getConfig()
     */
    public ThreadPoolConfig getConfig() {
        return config;
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.threadpool.ThreadPool#getCurrentThreads()
     */
    public int getCurrentThreads() {
        return threads;
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.threadpool.ThreadPool#dispose()
     */
    public void dispose() {
        synchronized (poolLock) {
            while (!idleWorkers.isEmpty()) {
                Worker worker = (Worker) idleWorkers.shift();
                worker.dispose();
                threads--;
            }
            Iterator iterator = busyWorkers.values().iterator();
            while (iterator.hasNext()) {
                Worker worker = (Worker) iterator.next();
                worker.dispose();
                threads--;
            }
            busyWorkers.clear();
        }
        disposed = true;
    }

    public void backWorker(Worker worker) {
        synchronized (poolLock) {
            if (tasks.isEmpty()) {
                busyWorkers.remove(worker);
                idleWorkers.push(worker);
            } else {
                busyWorkers.put(worker,worker);
                worker.work((Runnable) tasks.shift());
            }
        }
    }

    

}