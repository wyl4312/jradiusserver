/*
 * 创建日期 2005-5-4
 *
 */
package radius.server.jmx;

import radius.server.RadiusServer;
import org.apache.commons.logging.*;
/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public class ServerDecorator implements ServerDecoratorMBean {
    private Log log = LogFactory.getLog(ServerDecorator.class);
    private RadiusServer server;
    
    public ServerDecorator(RadiusServer server) {
        this.server = server;
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#stop()
     */
    public boolean stop() {
        return server.stop();
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#pause()
     */
    public boolean pause() {
        return server.pause();
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#restart()
     */
    public boolean restart() {
        return server.restart();
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#goOn()
     */
    public boolean goOn() {
        return server.goOn();
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#isStarted()
     */
    public boolean isStarted() {
        return server.isStarted();
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#isPaused()
     */
    public boolean isPaused() {
        return server.isPaused();
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#isStopped()
     */
    public boolean isStopped() {
        return server.isStopped();
    }

    /**
     * 
     * @see radius.server.jmx.ServerDecoratorMBean#shutdown()
     */
    public void shutdown() {
        server.stop();
        log.info("shutdown server!");
        System.exit(0);
    }

}
