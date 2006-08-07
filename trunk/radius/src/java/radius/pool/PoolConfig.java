package radius.pool;

import java.util.HashMap;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class PoolConfig {

	private int minSize;

	private int maxSize;

	private int increment;

	private boolean keepAlive;

	private int keepAliveIdleTime;// ms

	private HashMap properties;

	public PoolConfig() {
		properties = new HashMap();
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		assert increment > 0 : "increment must >0";
		this.increment = increment;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public int getKeepAliveIdleTime() {
		return keepAliveIdleTime;
	}

	public void setKeepAliveIdleTime(int keepAliveIdleTime) {
		this.keepAliveIdleTime = keepAliveIdleTime;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		assert maxSize > 0 : "maxSize must >0";
		this.maxSize = maxSize;
	}

	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		assert minSize >= 0 : "minSize must >=0";
		this.minSize = minSize;
	}

	public void setProperty(Object key, Object value) {
		properties.put(key, value);
	}

	public Object getProperty(Object key) {
		return properties.get(key);
	}

	public void removeProperty(Object key) {
		properties.remove(key);
	}

	public boolean isValid() {
		return getMinSize() >= 0
				&& getMaxSize() >= 0
				&& getIncrement() > 0
				&& getMinSize() <= getMaxSize()
				&& ((isKeepAlive() && getKeepAliveIdleTime() > 0) || isKeepAlive() == false);
	}

}
