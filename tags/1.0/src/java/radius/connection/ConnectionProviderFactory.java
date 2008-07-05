package radius.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class ConnectionProviderFactory {
    private static Log log = LogFactory.getLog(ConnectionProviderFactory.class);

    public static final String DEFAULT_CONNECTION_PROVIDER_IMPL = SimpleConnectionProvider.class
            .getName();

    private static ConnectionProvider provider;

    public static void init(String providerClass, Properties prop) {
        try {
            provider = (ConnectionProvider) Class.forName(providerClass)
                    .newInstance();
        } catch (Exception e) {
            log.error("init connection provider failed:"
                    + e.getMessage()+",use default connection provider implement", e);
            provider = new SimpleConnectionProvider();
        }
        provider.init(prop);
    }

    public static Connection getConnection() throws SQLException {
        return provider.getConnection();
    }

}