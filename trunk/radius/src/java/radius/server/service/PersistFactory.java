/*
 * 创建日期 2005-5-4
 *
 */
package radius.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.server.service.impl.PersistProxyImpl;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class PersistFactory {

    private static Log log = LogFactory.getLog(PersistFactory.class);

    public static final String PERSIST_IMPL_KEY = "radius.server.service.persist.impl";

    private static final String DEFAULT_PERSIST_IMPL = PersistProxyImpl.class.getName();

    private static Persist persist;

    private static Object lock = new Object();

    public static void init(String persistImplClass) {
        synchronized (lock) {
            persist = loadPersist(persistImplClass);
        }
    }
    
    public static void init() {
        synchronized (lock) {
            persist = loadPersist(DEFAULT_PERSIST_IMPL);
        }
    }

    private static Persist loadPersist(String clazz) {
        try {
            return (Persist) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            log
                    .info("invalid persist impl:" + clazz
                            + ",use default implement");
            return loadPersist(DEFAULT_PERSIST_IMPL);
        }
    }

    public static Persist getPersist() {
        if (persist == null) {
            synchronized (lock) {
                if (persist != null) {
                    String clazz = System.getProperty(PERSIST_IMPL_KEY,
                            DEFAULT_PERSIST_IMPL);
                    persist = loadPersist(clazz);
                }
            }
        }
        return persist;
    }

}