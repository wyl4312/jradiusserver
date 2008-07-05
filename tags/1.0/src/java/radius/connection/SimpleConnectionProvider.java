/*
 * 创建日期 2005-5-2
 *
 */
package radius.connection;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.connection.simplepool.SimpleConnectionPool;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class SimpleConnectionProvider implements ConnectionProvider {
    private Log log = LogFactory.getLog(SimpleConnectionProvider.class);

    private SimpleConnectionPool pool = new SimpleConnectionPool();

    /*
     * （非 Javadoc）
     * 
     * @see radius.connection.ConnectionProvider#init()
     */
    public void init(Properties prop) {
        if (!pool.init(prop)) {
            log.error("init SimpleConnectionProvider failed");
        }
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.connection.ConnectionProvider#getConnection()
     */
    public Connection getConnection() {
        return pool.getConnection();
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.connection.ConnectionProvider#dispose()
     */
    public void dispose() {
        pool.shutdown();
    }

}