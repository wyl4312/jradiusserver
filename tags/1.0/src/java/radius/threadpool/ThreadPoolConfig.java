/*
 * 创建日期 2004-8-20
 * 
 */
package radius.threadpool;

/**
 * @author starfire
 *  
 */
public class ThreadPoolConfig {
    public static int DEFAULT_MAX_SIZE = 10;

    public static int DEFAULT_MIN_SIZE = 4;

    public static int DEFAULT_INCREAMENT = 2;

    private int maxSize = DEFAULT_MAX_SIZE;

    private int minSize = DEFAULT_MIN_SIZE;

    private int increament = DEFAULT_INCREAMENT;

    public ThreadPoolConfig() {

    }

    /**
     * @return Returns the increament.
     */
    public int getIncreament() {
        return increament;
    }

    /**
     * @param increament
     *            The increament to set.
     */
    public void setIncreament(int increament) {
        if (increament > 0)
            this.increament = increament;
    }

    /**
     * @return Returns the maxSize.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @param maxSize
     *            The maxSize to set.
     */
    public void setMaxSize(int maxSize) {
        if (maxSize > 0)
            this.maxSize = maxSize;
    }

    /**
     * @return Returns the minSize.
     */
    public int getMinSize() {
        return minSize;
    }

    /**
     * @param minSize
     *            The minSize to set.
     */
    public void setMinSize(int minSize) {
        if (minSize > 0)
            this.minSize = minSize;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ThreadPoolConfig:[minSize=").append(minSize).append(",");
        sb.append("increment=").append(this.increament).append(",");
        sb.append("maxSize=").append(this.maxSize).append("]");
        return sb.toString();
    }
}