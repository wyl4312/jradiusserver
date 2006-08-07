package radius.server.cfg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import radius.Radius;
import radius.util.Dom4jUtil;
import radius.util.NetUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class ListenConfig {

    public static final String AUTHENTICATION_TYPE = "auth";

    public static final String ACCOUNTING_TYPE = "account";

    private static Log log = LogFactory.getLog(ListenConfig.class);

    private String type = AUTHENTICATION_TYPE;

    private String ip;

    private int port;
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("type=").append(type).append(",");
        sb.append("ip=").append(ip).append(",");
        sb.append("port=").append(port).append(",");
        return sb.toString();
    }

    public static ListenConfig newAuthConfig() {
        ListenConfig config = new ListenConfig();
        config.setPort(Radius.DEFAULT_AUTH_PORT);
        config.setType(AUTHENTICATION_TYPE);
        return config;
    }

    public static ListenConfig newAccountConfig() {
        ListenConfig config = new ListenConfig();
        config.setPort(Radius.DEFAULT_ACCOUNT_PORT);
        config.setType(ACCOUNTING_TYPE);
        return config;
    }

    public ListenConfig() {

    }

    public static ListenConfig getListenConfig(Element node) {
        ListenConfig config = new ListenConfig();
        config.type = Dom4jUtil.getAttribute(node, "type");
        config.setIp(Dom4jUtil.getAttribute(node, "ip",null));
        config.port = Dom4jUtil.getAttribute2Int(node, "port",
                Radius.DEFAULT_AUTH_PORT);
        return config;
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