package radius.connection.simplepool;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class ProxyConnection implements Connection {
    private Log log = LogFactory.getLog(ProxyConnection.class);

    private Connection target;
    private ConnectionIntercepter intercepter;
    private Collection stmts;
    private boolean closed;

    public ProxyConnection(Connection target,ConnectionIntercepter intercepter) {
        this.target = target;
        this.intercepter = intercepter;
        stmts = new LinkedList();
        if (log.isDebugEnabled()) {
            log.debug("create proxy connection");
        }
    }

    public void setReadOnly(boolean arg0) throws java.sql.SQLException {
        target.setReadOnly(arg0);
    }

    public boolean isReadOnly() throws java.sql.SQLException {
        return target.isReadOnly();
    }
    
    private void clearStatement() {
        Iterator iterator = stmts.iterator();
        while (iterator.hasNext()) {
            Statement stmt = (Statement) iterator.next();
            try {
                stmt.close();
            } catch (SQLException e) {
                log.warn("clear Statement failed:"+e.getMessage(),e);
            }
        }
        stmts.clear();
    }

    public void close() throws java.sql.SQLException {
        clearStatement();
        intercepter.onClose(target);
        closed = true;
    }

    public void clearWarnings() throws java.sql.SQLException {
        target.clearWarnings();
    }

    public void commit() throws java.sql.SQLException {
        target.commit();
    }

    public java.sql.Statement createStatement(int arg0, int arg1)
            throws java.sql.SQLException {
        java.sql.Statement stmt = target.createStatement(arg0, arg1);
        stmts.add(stmt);
        return stmt;
    }

    public java.sql.Statement createStatement(int arg0, int arg1, int arg2)
            throws java.sql.SQLException {
        java.sql.Statement stmt = target.createStatement(arg0, arg1, arg2);
        stmts.add(stmt);
        return stmt;
    }

    public java.sql.Statement createStatement() throws java.sql.SQLException {
        java.sql.Statement stmt =  target.createStatement();
        stmts.add(stmt);
        return stmt;
    }

    public boolean getAutoCommit() throws java.sql.SQLException {
        return target.getAutoCommit();
    }

    public java.lang.String getCatalog() throws java.sql.SQLException {
        return target.getCatalog();
    }

    public int getHoldability() throws java.sql.SQLException {
        return target.getHoldability();
    }

    public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {
        return target.getMetaData();
    }

    public int getTransactionIsolation() throws java.sql.SQLException {
        return target.getTransactionIsolation();
    }

    public java.util.Map getTypeMap() throws java.sql.SQLException {
        return target.getTypeMap();
    }

    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        return target.getWarnings();
    }

    public boolean isClosed() throws java.sql.SQLException {
        //return target.isClosed();
        return closed;
    }

    public java.lang.String nativeSQL(String arg0) throws java.sql.SQLException {
        return target.nativeSQL(arg0);
    }

    public java.sql.CallableStatement prepareCall(String arg0, int arg1,
            int arg2, int arg3) throws java.sql.SQLException {
        java.sql.CallableStatement cs = target.prepareCall(arg0, arg1, arg2, arg3);
        stmts.add(cs);
        return cs;
    }

    public java.sql.CallableStatement prepareCall(String arg0)
            throws java.sql.SQLException {
        java.sql.CallableStatement cs = target.prepareCall(arg0);
        stmts.add(cs);
        return cs;
    }

    public java.sql.CallableStatement prepareCall(String arg0, int arg1,
            int arg2) throws java.sql.SQLException {
        java.sql.CallableStatement cs = target.prepareCall(arg0, arg1, arg2);
        stmts.add(cs);
        return cs;
    }

    public java.sql.PreparedStatement prepareStatement(String arg0, int[] arg1)
            throws java.sql.SQLException {
        java.sql.PreparedStatement ps = target.prepareStatement(arg0, arg1);
        stmts.add(ps);
        return ps;
    }

    public java.sql.PreparedStatement prepareStatement(String arg0, int arg1,
            int arg2, int arg3) throws java.sql.SQLException {
        java.sql.PreparedStatement ps = target.prepareStatement(arg0, arg1, arg2, arg3);
        stmts.add(ps);
        return ps;
    }

    public java.sql.PreparedStatement prepareStatement(String arg0, int arg1,
            int arg2) throws java.sql.SQLException {
        java.sql.PreparedStatement ps = target.prepareStatement(arg0, arg1, arg2);
        stmts.add(ps);
        return ps;
    }

    public java.sql.PreparedStatement prepareStatement(String arg0,
            String[] arg1) throws java.sql.SQLException {
        java.sql.PreparedStatement ps =  target.prepareStatement(arg0, arg1);
        stmts.add(ps);
        return ps;
    }

    public java.sql.PreparedStatement prepareStatement(String arg0)
            throws java.sql.SQLException {
        java.sql.PreparedStatement ps =  target.prepareStatement(arg0);
        stmts.add(ps);
        return ps;
    }

    public java.sql.PreparedStatement prepareStatement(String arg0, int arg1)
            throws java.sql.SQLException {
        java.sql.PreparedStatement ps =  target.prepareStatement(arg0, arg1);
        stmts.add(ps);
        return ps;
    }

    public void releaseSavepoint(Savepoint arg0) throws java.sql.SQLException {
        target.releaseSavepoint(arg0);
    }

    public void rollback() throws java.sql.SQLException {
        target.rollback();
    }

    public void rollback(Savepoint arg0) throws java.sql.SQLException {
        target.rollback(arg0);
    }

    public void setAutoCommit(boolean arg0) throws java.sql.SQLException {
        if (getAutoCommit() != arg0) {
            target.setAutoCommit(arg0);
        }
    }

    public void setCatalog(String arg0) throws java.sql.SQLException {
        target.setCatalog(arg0);
    }

    public void setHoldability(int arg0) throws java.sql.SQLException {
        target.setHoldability(arg0);
    }

    public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
        return target.setSavepoint();
    }

    public java.sql.Savepoint setSavepoint(String arg0)
            throws java.sql.SQLException {
        return target.setSavepoint(arg0);
    }

    public void setTransactionIsolation(int arg0) throws java.sql.SQLException {
        target.setTransactionIsolation(arg0);
    }

    public void setTypeMap(Map arg0) throws java.sql.SQLException {
        target.setTypeMap(arg0);
    }

    public static void main(String[] args) {
        Method[] ms = Connection.class.getDeclaredMethods();
        for (int i = 0; i < ms.length; i++) {
            Method m = ms[i];
            String r = m.getReturnType().getName();
            System.out.print("public " + r + " " + m.getName() + "(");
            Class[] pc = m.getParameterTypes();
            for (int j = 0; j < pc.length; j++) {
                if (j > 0) {
                    System.out.print(",");
                }
                String p = getArg(pc[j]);
                String low = getArgParam(pc[j]);
                System.out.print(p + " arg" + j);
            }
            System.out.println(") ");
            Class[] ec = m.getExceptionTypes();
            if (ec.length > 0) {
                System.out.print(" throws ");
                for (int j = 0; j < ec.length; j++) {
                    if (j > 0)
                        System.out.println(",");
                    System.out.print(ec[j].getName());
                }
            }
            System.out.println(" {");
            if (!"void".equalsIgnoreCase(r)) {
                System.out.print("return ");
            }
            System.out.print("target." + m.getName() + "(");
            for (int j = 0; j < pc.length; j++) {
                if (j > 0) {
                    System.out.print(",");
                }
                System.out.print("arg" + j);
            }
            System.out.println(");");
            System.out.println("}\n");
        }
    }

    private static String getArg(Class clazz) {
        String f = clazz.getName();
        if (f.endsWith(";")) {
            f = f.substring(0, f.length() - 1);
        }
        if (f.equals("[I")) {
            return "int[]";
        }
        if (f.startsWith("["))
            f = f + "[]";
        int idx = f.lastIndexOf(".");
        if (idx != -1) {
            f = f.substring(idx + 1);
            return f;
        }

        return f;
    }

    private static String getArgParam(Class clazz) {
        String f = getArg(clazz);
        if (clazz.getName().startsWith("java.lang.")) {
            return f.substring(0, 1).toLowerCase();
        }
        if (clazz.getName().indexOf(".") == -1) {
            return f.substring(0, 1).toLowerCase();
        }
        return f.toLowerCase();
    }
}