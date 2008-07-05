package radius.server.service;

import radius.NAS;
import radius.server.service.pojo.Account;
import radius.server.service.pojo.Proxy;
import radius.server.service.pojo.ServiceType;
import radius.server.service.pojo.User;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface Persist {
	
	User getUser(String name);
	
	void saveAccount(Account account);
	
	ServiceType[] loadServiceType(User user);

	Proxy[] getProxy(final String fromIp);
	
	NAS getNAS(final String ip);
}
