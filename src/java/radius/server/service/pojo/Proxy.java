/*
 * 创建日期 2005-5-4
 *
 */
package radius.server.service.pojo;

import java.io.Serializable;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class Proxy implements Serializable {
    public static final String AUTH_TYPE = "auth";

    public static final String ACCOUNT_TYPE = "account";

    public static final String ALL_TYPE = "all";

    private String fromIp;

    private String toIp;

    private String type;

    private int fromPort;

    private int toPort;

    /**
     * @return 返回 fromIp。
     */
    public String getFromIp() {
        return fromIp;
    }

    /**
     * @param fromIp
     *            要设置的 fromIp。
     */
    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    /**
     * @return 返回 fromPort。
     */
    public int getFromPort() {
        return fromPort;
    }

    /**
     * @param fromPort
     *            要设置的 fromPort。
     */
    public void setFromPort(int fromPort) {
        this.fromPort = fromPort;
    }

    /**
     * @return 返回 targetPort。
     */
    public int getToPort() {
        return toPort;
    }

    /**
     * @param targetPort
     *            要设置的 targetPort。
     */
    public void setToPort(int toPort) {
        this.toPort = toPort;
    }

    /**
     * @return 返回 toIp。
     */
    public String getToIp() {
        return toIp;
    }

    /**
     * @param toIp
     *            要设置的 toIp。
     */
    public void setToIp(String toIp) {
        this.toIp = toIp;
    }

    /**
     * @return 返回 type。
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            要设置的 type。
     */
    public void setType(String type) {
        this.type = type;
    }
}