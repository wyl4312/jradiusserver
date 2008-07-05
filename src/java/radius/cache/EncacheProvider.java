/*
 * 创建日期 2005-5-5
 *
 */
package radius.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class EncacheProvider implements CacheProvider {

    /*
     * （非 Javadoc）
     * 
     * @see radius.cache.CacheProvider#getCache(java.lang.String)
     */
    public ICache getCache(String name) {
        return EncacheImpl.getEncacheImpl(name);
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.cache.CacheProvider#dispose()
     */
    public void dispose() {
        EncacheImpl.releaseAllCache();
    }

    public static class EncacheImpl implements ICache {
        private static final Log log = LogFactory.getLog(EncacheImpl.class);

        private String name;

        private Cache cache;

        private static Map impls = new HashMap();

        private EncacheImpl(String name, Cache cache) {
            this.name = name;
            this.cache = cache;
        }

        public static void releaseAllCache() {
            synchronized (impls) {
                Iterator iterator = impls.keySet().iterator();
                try {
                    while (iterator.hasNext()) {
                        String name = (String) iterator.next();
                        CacheManager.getInstance().removeCache(name);
                    }
                } catch (Exception e) {
                    log.info("release all cache failed", e);
                }
                impls.clear();
            }
        }

        public static EncacheImpl getEncacheImpl(String name) {
            try {
                synchronized (impls) {
                    EncacheImpl impl = (EncacheImpl) impls.get(name);
                    if (impl == null) {
                        CacheManager manager = CacheManager.getInstance();
                        Cache cache = manager.getCache(name);
                        if (cache == null) {
                            log
                                    .warn("Could not find configuration for "
                                            + name
                                            + ". Configuring using the defaultCache settings.");
                            manager.addCache(name);
                            cache = manager.getCache(name);
                        }
                        impl = new EncacheImpl(name, cache);
                    }
                    return impl;
                }
            } catch (CacheException e) {
                log.error("can't get cache for " + name, e);
                return null;
            }
        }

        /*
         * （非 Javadoc）
         * 
         * @see radius.cache.Cache#put(java.lang.Object, java.lang.Object)
         */
        public void put(Object key, Object value) {
            try {
                Element element = new Element((Serializable) key,
                        (Serializable) value);
                cache.put(element);
            } catch (Exception e) {
                log.error("put value failed,key=" + key, e);
            }
        }

        /*
         * （非 Javadoc）
         * 
         * @see radius.cache.Cache#get(java.lang.Object)
         */
        public Object get(Object key) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("key: " + key);
                }
                if (key == null) {
                    return null;
                } else {
                    Element element = cache.get((Serializable) key);
                    if (element == null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Element for " + key + " is null");
                        }
                        return null;
                    } else {
                        return element.getValue();
                    }
                }
            } catch (net.sf.ehcache.CacheException e) {
                log.error("get value from cache failed,cache name=" + name
                        + ",key=" + key, e);
                return null;
            }
        }

        /*
         * （非 Javadoc）
         * 
         * @see radius.cache.Cache#remove(java.lang.Object)
         */
        public void remove(Object key) {
            try {
                cache.remove((Serializable) key);
            } catch (Exception e) {
                log.error("remove from cache failed,key=" + key
                        + ",cache name=" + name, e);

            }
        }

        /*
         * （非 Javadoc）
         * 
         * @see radius.cache.Cache#clear()
         */
        public void clear() {
            try {
                cache.removeAll();
            } catch (Exception e) {
                log.error("remove from cache failed,cache name=" + name, e);
            }
        }

    }
}