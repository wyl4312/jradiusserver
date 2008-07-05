
package radius.pool;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface ObjectPool {

	public Object get();

	public void back(Object obj);

	public boolean isAlive();

	public int getCurrentSize();

	public int getIdleSize();

	public PoolConfig getPoolConfig();

	public void destroy();

}