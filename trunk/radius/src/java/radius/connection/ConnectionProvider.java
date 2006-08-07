/*
 * 创建日期 2005-5-2
 *
 */
package radius.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public interface ConnectionProvider {

    void init(Properties prop);

    Connection getConnection() throws SQLException;

    void dispose();

}