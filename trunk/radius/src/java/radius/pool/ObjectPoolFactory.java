package radius.pool;

public interface ObjectPoolFactory {

	ObjectPool createPool(PoolConfig config,ObjectFactory factory);
	
	ObjectPool createPool(PoolConfig config,ObjectFactory factory,ObjectLifeCycle lifeCycle);
	
}
