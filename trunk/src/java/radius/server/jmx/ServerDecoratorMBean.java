/*
 * 创建日期 2005-5-4
 *
 */
package radius.server.jmx;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public interface ServerDecoratorMBean {
    
    boolean stop();
    
    boolean pause();
    
    boolean restart();
    
    boolean goOn();

	boolean isStarted();

	boolean isPaused();

	boolean isStopped();
	
	void shutdown();
}
