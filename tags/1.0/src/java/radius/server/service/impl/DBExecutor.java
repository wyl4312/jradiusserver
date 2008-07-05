/*
 * 创建日期 2005-5-4
 *
 */
package radius.server.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.connection.ConnectionProviderFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
class DBExecutor {
    private static Log log = LogFactory.getLog(PersistImpl.class);

    private Connection getConnection() throws SQLException {
        return ConnectionProviderFactory.getConnection();
    }

    public void execute(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList list = new ArrayList();
        try {
            if (log.isDebugEnabled()) {
                log.debug("execute sql:" + sql);
            }
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            fillRequestStatement(ps);
            boolean hasResultSet = ps.execute();
            if (hasResultSet) {
                processResultSet(ps.getResultSet());
            }
        } catch (SQLException e) {
            log.error("excute sql failed", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e1) {
                    log.error("close statement failed", e1);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    log.error("close connection failed", e1);
                }
            }
        }
    }

    protected void fillRequestStatement(PreparedStatement ps)
            throws SQLException {

    }

    protected void processResultSet(ResultSet rs) throws SQLException {

    }
}