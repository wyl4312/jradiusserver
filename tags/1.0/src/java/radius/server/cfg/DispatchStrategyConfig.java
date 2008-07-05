package radius.server.cfg;

import org.dom4j.Element;

import radius.util.Dom4jUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class DispatchStrategyConfig {

    private boolean singleThread = false;

    private boolean autoConfig = true;

    private int minThread;

    private int maxThread;

    private int increment;
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("singleThread=").append(singleThread).append(",");
        sb.append("autoConfig=").append(autoConfig);
        if (!autoConfig) {
            sb.append(",");
            sb.append("minThread=").append(minThread).append(",");
            sb.append("maxThread=").append(maxThread).append(",");
            sb.append("increment=").append(increment);
        }
        return sb.toString();
    }

    public static DispatchStrategyConfig getDispatchStrategyConfig(Element node) {
        DispatchStrategyConfig config = new DispatchStrategyConfig();
        config.singleThread = Dom4jUtil.getAttribute2Boolean(node,"singleThread",false);
        config.autoConfig  = Dom4jUtil.getAttribute2Boolean(node,"autoConfig",true);
        config.minThread = Dom4jUtil.getAttribute2Int(node,"minThread",2);
        config.maxThread = Dom4jUtil.getAttribute2Int(node,"maxThread",10);
        config.increment = Dom4jUtil.getAttribute2Int(node,"increment",2);
        return config;
    }

    /**
     * @return Returns the autoConfig.
     */
    public boolean isAutoConfig() {
        return autoConfig;
    }

    /**
     * @param autoConfig
     *            The autoConfig to set.
     */
    public void setAutoConfig(boolean autoConfig) {
        this.autoConfig = autoConfig;
    }

    /**
     * @return Returns the increment.
     */
    public int getIncrement() {
        return increment;
    }

    /**
     * @param increment
     *            The increment to set.
     */
    public void setIncrement(int increment) {
        this.increment = increment;
    }

    /**
     * @return Returns the maxThread.
     */
    public int getMaxThread() {
        return maxThread;
    }

    /**
     * @param maxThread
     *            The maxThread to set.
     */
    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    /**
     * @return Returns the minThread.
     */
    public int getMinThread() {
        return minThread;
    }

    /**
     * @param minThread
     *            The minThread to set.
     */
    public void setMinThread(int minThread) {
        this.minThread = minThread;
    }

    /**
     * @return Returns the singleThread.
     */
    public boolean isSingleThread() {
        return singleThread;
    }

    /**
     * @param singleThread
     *            The singleThread to set.
     */
    public void setSingleThread(boolean singleThread) {
        this.singleThread = singleThread;
    }

}