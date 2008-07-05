package radius.server.service.impl;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.NAS;
import radius.server.service.Persist;
import radius.server.service.pojo.Account;
import radius.server.service.pojo.Proxy;
import radius.server.service.pojo.ServiceType;
import radius.server.service.pojo.User;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class PersistImpl implements Persist {
    private static Log log = LogFactory.getLog(PersistImpl.class);

    private static final String GET_USER = "select * from user where name=?";

    private static final String GET_NAS  = "select * from nas where ip=?";
    
    private static final String GET_PROXY = "select * from proxy where from_ip=?";
    
    private static final String LOAD_SERVICE_TYPE = "select st.*,relation.value value "
            + "from service_type st,group_and_service_type relation "
            + "where relation.group_id=? "
            + "and st.id=relation.service_type_id";

    /*
     * @see radius.server.service.Persist#getUser(java.lang.String)
     */
    public User getUser(final String name) {
        if (name == null) {
            log.warn("user name can't be null");
            return null;
        }
        final User user = new User();
        DBExecutor executor = new DBExecutor() {
            protected void fillRequestStatement(PreparedStatement ps)
                    throws SQLException {
                ps.setString(1, name);
            }

            protected void processResultSet(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    user.setId(new Long(rs.getLong("id")));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setEncrypt(rs.getString("encrypt").charAt(0));
                    user.setAuthMethod(rs.getString("auth_method"));
                    user.setStatus(rs.getString("status").charAt(0));
                    user.setLoginCount(rs.getInt("login_count"));
                }
            }
        };
        executor.execute(GET_USER);
        return user.getName() == null ? null : user;
    }

    public ServiceType[] loadServiceType(final User user) {
        final ArrayList serviceTypes = new ArrayList();
        DBExecutor executor = new DBExecutor() {
            protected void fillRequestStatement(PreparedStatement ps)
                    throws SQLException {
                ps.setInt(1, user.getGroupId());
            }

            protected void processResultSet(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    ServiceType serviceType = new ServiceType();
                    serviceType.setName(rs.getString("name"));
                    serviceType.setAttId(new Integer(rs.getInt("att_id")));
                    serviceType.setType(rs.getString("type"));
                    serviceType.setUnit(rs.getString("unit"));
                    serviceType.setValue(rs.getString("value"));
                    serviceTypes.add(serviceType);
                }
            }
        };
        executor.execute(LOAD_SERVICE_TYPE);
        return (ServiceType[]) serviceTypes
                .toArray(new ServiceType[serviceTypes.size()]);
    }

    public NAS getNAS(final String ip) {
        if (ip==null)
            return null;
        final NAS nas =  new NAS();
        DBExecutor executor = new DBExecutor() {
            protected void fillRequestStatement(PreparedStatement ps)
                    throws SQLException {
                ps.setString(1, ip);
            }

            protected void processResultSet(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    nas.setIp(rs.getString("ip"));
                    nas.setSecret(rs.getString("secret"));
                    nas.setVendor(rs.getString("vendor_name"));
                }
            }
        };
        executor.execute(GET_NAS);
        return ip.equals(nas.getIp()) ?nas:null;
    }
    
    public Proxy[] getProxy(final String fromIp) {
        final ArrayList proxies = new ArrayList();
        DBExecutor executor = new DBExecutor() {
            protected void fillRequestStatement(PreparedStatement ps)
                    throws SQLException {
                ps.setString(1, fromIp);
            }

            protected void processResultSet(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    Proxy proxy = new Proxy();
                    proxy.setFromIp(rs.getString("from_ip"));
                    proxy.setFromPort(rs.getInt("from_port"));
                    proxy.setToIp(rs.getString("to_ip"));
                    proxy.setToPort(rs.getInt("to_port"));
                    proxy.setType(rs.getString("type"));
                    proxies.add(proxy);
                }
            }
        };
        executor.execute(LOAD_SERVICE_TYPE);
        return (Proxy[]) proxies
                .toArray(new Proxy[proxies.size()]);
    }
    
    /*
     * @see radius.server.service.Persist#saveAccount(radius.server.service.pojo.Account)
     */
    public void saveAccount(final Account account) {
        final ArrayList list = new ArrayList();
        String sql = getSqlForAccount(account, list);
        if (list.size()==0) {
            log.warn("attempt to save a account which has not any data,ignore it");
            return ;
        }
        DBExecutor executor = new DBExecutor() {
            protected void fillRequestStatement(PreparedStatement ps)
                    throws SQLException {
                for (int i = 0; i < list.size(); i++) {
                    ps.setObject(i, list.get(i));
                }
            }

        };
        executor.execute(sql);

    }

    private String getSqlForAccount(Account account, ArrayList params) {
        int count = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into account(");
        if (account.getId() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("id").append(" ");
            count++;
            params.add(account.getId());
        }
        if (account.getUserName() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("user_name").append(" ");
            count++;
            params.add(account.getUserName());
        }
        if (account.getStatusType() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("status_type").append(" ");
            count++;
            params.add(account.getStatusType());
        }
        if (account.getDelayTime() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("delay_time").append(" ");
            count++;
            params.add(account.getDelayTime());
        }
        if (account.getInputOctects() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("input_octects").append(" ");
            count++;
            params.add(account.getInputOctects());
        }
        if (account.getOutputOctects() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("output_octects").append(" ");
            count++;
            params.add(account.getOutputOctects());
        }
        if (account.getSessionId() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("session_id").append(" ");
            count++;
            params.add(account.getSessionId());
        }
        if (account.getAuthentic() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("authentic").append(" ");
            count++;
            params.add(account.getAuthentic());
        }
        if (account.getSessionTime() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("session_time").append(" ");
            count++;
            params.add(account.getSessionTime());
        }
        if (account.getInputPackets() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("input_packets").append(" ");
            count++;
            params.add(account.getInputPackets());
        }
        if (account.getOutputPackets() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("output_packets").append(" ");
            count++;
            params.add(account.getOutputPackets());
        }
        if (account.getTerminateCause() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("terminate_cause").append(" ");
            count++;
            params.add(account.getTerminateCause());
        }
        if (account.getMultiSessionId() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("multi_session_id").append(" ");
            count++;
            params.add(account.getMultiSessionId());
        }
        if (account.getLinkCount() != null) {
            if (count > 0) {
                sb.append(",");
            }
            sb.append("link_count").append(" ");
            count++;
            params.add(account.getLinkCount());
        }
        sb.append(")");

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Field[] fs = Account.class.getDeclaredFields();
        System.out.println("int count = 0;");
        System.out.println("StringBuffer sb = new StringBuffer();");
        System.out.println("sb.append(\"insert into radius_account(\");");
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            String name = f.getName();
            String up = Character.toUpperCase(name.charAt(0))
                    + name.substring(1);
            System.out.println("if (account.get" + up + "()!=null){");
            System.out.println("if (count>0){ sb.append(\",\");}");
            System.out.print("sb.append(\"" + name + "\").append(\" \");");
            System.out.println("count++;");
            System.out.println("params.add(account.get" + up + "());");
            System.out.println("}");
        }
        System.out.println("sb.append(\")\");");

    }
}