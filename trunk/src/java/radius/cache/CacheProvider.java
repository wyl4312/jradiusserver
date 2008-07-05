/*
 * 创建日期 2005-5-5
 *
 */
package radius.cache;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public interface CacheProvider {

    ICache getCache(String name);
    
    void dispose();
}
