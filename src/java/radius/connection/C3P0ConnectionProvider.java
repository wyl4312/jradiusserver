/*
 * 创建日期 2005-5-2
 *
 */
package radius.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.util.PropertyUtil;

import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PoolConfig;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class C3P0ConnectionProvider implements ConnectionProvider {
    private DataSource ds;

    private Log log = LogFactory.getLog(C3P0ConnectionProvider.class);

    /*
     * （非 Javadoc）
     * 
     * @see radius.connection.ConnectionProvider#init(java.util.Properties)
     */
    public void init(Properties prop) {
        try {
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            int minPoolSize = PropertyUtil.getInt(prop, "minSize", 1);
            int maxPoolSize = PropertyUtil.getInt(prop, "maxSize", 100);
            int maxIdleTime = PropertyUtil.getInt(prop, "idleTime", 0);
            int maxStatements = PropertyUtil.getInt(prop, "maxStatements", 0);
            int acquireIncrement = PropertyUtil.getInt(prop,
                    "acquireIncrement", 1);
            int idleTestPeriod = PropertyUtil.getInt(prop, "idleTestPeriod", 0);
            boolean testConnectionOnCheckout = PropertyUtil.getBoolean(prop,
                    "testConnectionOnCheckout");

            PoolConfig pcfg = new PoolConfig();
            pcfg.setInitialPoolSize(minPoolSize);
            pcfg.setMinPoolSize(minPoolSize);
            pcfg.setMaxPoolSize(maxPoolSize);
            pcfg.setAcquireIncrement(acquireIncrement);
            pcfg.setMaxIdleTime(maxIdleTime);
            pcfg.setMaxStatements(maxStatements);
            pcfg.setTestConnectionOnCheckout(testConnectionOnCheckout);
            pcfg.setIdleConnectionTestPeriod(idleTestPeriod);

            DataSource unpooled = null;
            if (user != null) {
                unpooled = DataSources.unpooledDataSource(url, user, password);
            } else {
                unpooled = DataSources.unpooledDataSource(url);
            }
            ds = DataSources.pooledDataSource(unpooled, pcfg);
        } catch (Exception e) {
            log.fatal("could not instantiate C3P0 connection pool", e);
            throw new RuntimeException(
                    "could not instantiate C3P0 connection pool");
        }
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.connection.ConnectionProvider#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /*
     * （非 Javadoc）
     * 
     * @see radius.connection.ConnectionProvider#dispose()
     */
    public void dispose() {
        try {
            DataSources.destroy(ds);
            ds = null;
        } catch (SQLException sqle) {
            log.warn("could not destroy C3P0 connection pool", sqle);
        }
    }

}