/*
 * 创建日期 2005-5-2
 *
 */
package radius.connection.simplepool;

import java.sql.Connection;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public interface ConnectionIntercepter {
    
    void onClose(Connection conn);

}
