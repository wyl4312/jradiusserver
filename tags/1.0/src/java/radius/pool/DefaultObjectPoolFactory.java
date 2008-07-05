/**
 * 
 */
package radius.pool;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class DefaultObjectPoolFactory implements ObjectPoolFactory {

	private static ObjectPoolFactory instance;
	
	static {
		instance = new DefaultObjectPoolFactory();
	}
	
	public static ObjectPoolFactory getInstance() {
		return instance;
	}
	
	public ObjectPool createPool(PoolConfig config, ObjectFactory factory) {
		return new ObjectPoolHashImpl(config, factory);
	}

	public ObjectPool createPool(PoolConfig config, ObjectFactory factory,
			ObjectLifeCycle lifeCycle) {
		return new ObjectPoolHashImpl(config, factory, lifeCycle);
	}

}
