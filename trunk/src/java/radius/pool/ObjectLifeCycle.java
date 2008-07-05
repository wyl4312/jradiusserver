package radius.pool;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public interface ObjectLifeCycle {

    void active(Object obj);
    
    void passive(Object obj);
    
    void keepAlive(Object obj);
    
    void destroy(Object obj);
}
