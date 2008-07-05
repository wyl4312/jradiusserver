/*
 * 创建日期 2005-5-5
 *
 */
package radius.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class CacheProviderFactory {

    public static final String CACHE_PROVIDER_IMPL_KEY = "radius.cache.provider.impl";

    public static final String DEFAULT_CACHE_PROVIDER_IMPL = "radius.cache.EncacheProvider";

    private static Log log = LogFactory.getLog(CacheProviderFactory.class);

    private static CacheProvider provider;

    private static Object lock = new Object();

    public static CacheProvider getCacheProvider() {
        if (provider == null) {
            init();
        }
        return provider;
    }

    public static void init(String clazz) {
        synchronized (lock) {
            log.info("init cache provider");
            provider = loadProvider(clazz);
        }
    }

    public static void init() {
        String impl = System.getProperty(CACHE_PROVIDER_IMPL_KEY,
                DEFAULT_CACHE_PROVIDER_IMPL);
        init(impl);
    }

    private static CacheProvider loadProvider(String clazz) {
        try {
            return (CacheProvider) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            log.warn("can't load cache provider impl:" + clazz);
            return loadProvider(DEFAULT_CACHE_PROVIDER_IMPL);
        }
    }
}