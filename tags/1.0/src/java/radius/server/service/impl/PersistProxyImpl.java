/*
 * 创建日期 2005-5-4
 *
 */
package radius.server.service.impl;

import radius.NAS;
import radius.cache.CacheProviderFactory;
import radius.cache.ICache;
import radius.server.service.Persist;
import radius.server.service.pojo.Account;
import radius.server.service.pojo.Proxy;
import radius.server.service.pojo.ServiceType;
import radius.server.service.pojo.User;

/**
 * provide cache support for persist.
 * 
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class PersistProxyImpl implements Persist {
    public static final String NAS_CACHE_NAME = "nas";

    public static final String PROXY_CACHE_NAME = "proxy";

    public static final String SERVICETYPE_CACHE_NAME = "servicetype";

    private Persist impl;

    public PersistProxyImpl() {
        impl = new PersistImpl();
    }

    public PersistProxyImpl(Persist impl) {
        this.impl = impl;
    }

    /*
     * @see radius.server.service.Persist#getUser(java.lang.String)
     */
    public User getUser(String name) {
        return impl.getUser(name);
    }

    /*
     * @see radius.server.service.Persist#saveAccount(radius.server.service.pojo.Account)
     */
    public void saveAccount(Account account) {
        impl.saveAccount(account);
    }

    /*
     * @see radius.server.service.Persist#loadServiceType(radius.server.service.pojo.User)
     */
    public ServiceType[] loadServiceType(User user) {
        if (user == null)
            return null;
        ICache cache = CacheProviderFactory.getCacheProvider().getCache(
                SERVICETYPE_CACHE_NAME);
        String key = user.getGroupId() + "";
        Object value = cache.get(key);
        if (value != null)
            return (ServiceType[]) value;
        else {
            ServiceType[] sts = impl.loadServiceType(user);
            cache.put(key, sts);
            return sts;
        }
    }

    /*
     * @see radius.server.service.Persist#getProxy(java.lang.String)
     */
    public Proxy[] getProxy(String fromIp) {
        ICache cache = CacheProviderFactory.getCacheProvider().getCache(
                PROXY_CACHE_NAME);
        String key = fromIp;
        Object value = cache.get(key);
        if (value != null)
            return (Proxy[]) value;
        else {
            Proxy[] proxies = impl.getProxy(fromIp);
            cache.put(key, proxies);
            return proxies;
        }
    }

    /*
     * @see radius.server.service.Persist#getNAS(java.lang.String)
     */
    public NAS getNAS(String ip) {
        ICache cache = CacheProviderFactory.getCacheProvider().getCache(
                NAS_CACHE_NAME);
        String key = ip;
        Object value = cache.get(key);
        if (value != null)
            return (NAS) value;
        else {
            NAS nas = impl.getNAS(ip);
            cache.put(key,nas);
            return nas;
        }
    }

}