/*
 * 创建日期 2005-5-5
 *
 */
package radius.cache;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public interface ICache {
    
    void put(Object key,Object value);
    
    Object get(Object key);
    
    void remove(Object key);
    
    void clear();
    
}
