package radius.server.cfg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;

import radius.util.Dom4jUtil;
import radius.util.NetUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class JmxConfig {
    public static final String RMI_TYPE = "rmi";

    public static final String HTTP_TYPE = "http";

    public static final int DEFAULT_JMX_PORT = 0x0508;

    private static Log log = LogFactory.getLog(JmxConfig.class);

    private boolean enable;

    private String ip;

    private int port = DEFAULT_JMX_PORT;

    private String type = HTTP_TYPE;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("enable=").append(enable).append(",");
        sb.append("type=").append(type).append(",");
        sb.append("ip=").append(ip).append(",");
        sb.append("port=").append(port).append(",");
        return sb.toString();
    }

    public JmxConfig() {

    }

    public static JmxConfig getJmxConfig(Element node) {
        JmxConfig config = new JmxConfig();

        config.enable = Dom4jUtil.getAttribute2Boolean(node, "enable", true);
        config.setIp(Dom4jUtil.getAttribute(node, "ip",null));
        config.port = Dom4jUtil.getAttribute2Int(node, "port",
                DEFAULT_JMX_PORT);
        String s = Dom4jUtil.getAttribute(node, "type",HTTP_TYPE);
        if (RMI_TYPE.equalsIgnoreCase(s)) {
            config.type = RMI_TYPE;
        } else {
            config.type = HTTP_TYPE;
        }
        return config;
    }

    /**
     * @return Returns the enable.
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable
     *            The enable to set.
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @return Returns the ip.
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            The ip to set.
     */
    public void setIp(String ip) {
        if (NetUtil.isValidAddress(ip)) {
            this.ip = ip;
        }
        else {
            log.warn("invalid ip:"+ip);
        }
    }

    /**
     * @return Returns the port.
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port
     *            The port to set.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

}