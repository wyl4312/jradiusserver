package radius.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.*;
/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public final class ServiceManager {
	private static Log log = LogFactory.getLog(ServiceManager.class);
    
    private ServiceManager(){}
	
	private static Map services;
	
	static {
		services = new HashMap();
	}
    
    public static void register(RadiusService service) {
		int type = service.getType();
		log.info("register new service for type "+type);
		Object old = services.put(new Integer(type),service);
		if (old!=null) {
			log.warn("type "+type+" has been registed,replaced");
		}
    }
    
    public static RadiusService getService(int type) {
        return (RadiusService) services.get(new Integer(type));
    }

}
