/*
 * 创建日期 2005-5-2
 *
 */
package radius.connection.simplepool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class SimpleConnectionPool implements ConnectionIntercepter {
    private Log log = LogFactory.getLog(SimpleConnectionPool.class);

    private Properties config;

    private int maxSize = 10;

    private LinkedList conns = new LinkedList();

    public boolean init(Properties prop) {
        log.info("init SimpleConnectionPool...");
        assert config != null;
        this.config = prop;
        try {
            Class.forName(config.getProperty("driver")).newInstance();
            log.info("init SimpleConnectionPool ok");
            String s = config.getProperty("maxSize", "10");
            try {
                maxSize = Integer.parseInt(s);
                if (maxSize<1)
                    maxSize = 10;
            } catch (Exception e) {
            }
            return true;
        } catch (ClassNotFoundException e) {
            log.error("init failed,can't find driver class:" + e.getMessage(),
                    e);
        } catch (Exception e) {
            log.error("create driver's instance failed:" + e.getMessage(), e);
        }
        return false;
    }

    public Connection getConnection() {
        Connection conn = null;
        synchronized (conns) {
            if (!conns.isEmpty()) {
                if (log.isDebugEnabled()) {
                    log.debug("get connection from pool");
                }
                conn = (Connection) conns.removeFirst();
            }
        }
        if (conn == null) {
            if (log.isDebugEnabled()) {
                log.debug("get connection by open new connection");
            }
            try {
                String user = config.getProperty("user");
                if (user != null) {
                    conn = DriverManager.getConnection(config
                            .getProperty("url"), config.getProperty("user"),
                            config.getProperty("password"));
                } else {
                    conn = DriverManager.getConnection(config
                            .getProperty("url"));
                }
            } catch (SQLException e) {
                log.error("create new connection failed:" + e.getMessage(), e);
            }
        }
        if (conn != null) {
            return new ProxyConnection(conn, this);
        } else {
            return null;
        }
    }

    public void shutdown() {
        synchronized (conns) {
            Iterator iterator = conns.iterator();
            while (iterator.hasNext()) {
                Connection conn = (Connection) iterator.next();
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.warn("close connection failed:" + e.getMessage());
                }
            }
        }
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.connection.simplepool.ConnectionIntercepter#onClose(java.sql.Connection)
     */
    public void onClose(Connection conn) {
        synchronized (conns) {
            if (conns.size() < maxSize) {
                if (log.isDebugEnabled()) {
                    log.debug("get connection by open new connection");
                }
                conns.addLast(conn);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("pool is full,close back connection");
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        log.warn("close connection failed:" + e.getMessage());
                    }
                }
            }
        }
    }

}