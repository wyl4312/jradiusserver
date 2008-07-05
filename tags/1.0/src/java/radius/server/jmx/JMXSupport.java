/*
 * 创建日期 2005-5-4
 *
 */
package radius.server.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.adaptor.http.XSLTProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class JMXSupport {
    private static Log log = LogFactory.getLog(JMXSupport.class);

    private static MBeanServer server = MBeanServerFactory.createMBeanServer();

    private static boolean started;

    public static void registerMBean(Object mbean,ObjectName name) {
        try {
            server.registerMBean(mbean,name);
        } catch (Exception e) {
            log.error("registerMbean("+mbean+") on ("+name+") failed:"+e.getMessage(),e);
        }
    }

    public static void start(String type, String host, int port) {
        if (started) {
            return;
        }
        //now type is been ignored
        try {
            log.info("start http adapter on "+host+":"+port);
            HttpAdaptor adapter = new HttpAdaptor();
            ObjectName name = new ObjectName("Server:name=HttpAdaptor");
            server.registerMBean(adapter, name);
            if (port > 0)
                adapter.setPort(port);
            if (host != null)
                adapter.setHost(host);
            XSLTProcessor processor = new XSLTProcessor();
            adapter.setProcessor(processor);
            adapter.start();
            started = true;
        } catch (Exception e) {
            log.error("start MBeanServer Adapter failed:" + e.getMessage(), e);
        }
    }

}