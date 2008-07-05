package radius.server.filter;

import radius.RadiusPacket;
import radius.chain.FilterChain;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;
import radius.server.RadiusService;
import radius.server.ServiceManager;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ServiceFilter extends AbstractRadiusFilter {

	public ServiceFilter() {
		super(RadiusFilter.SERVICE_TYPE);
	}

	/*
	 * @see chain.Filter#doFilter(chain.Context, chain.FilterChain)
	 */
	public void doRadiusFilter(RadiusContext context, FilterChain chain) {
		RadiusPacket packet = context.getSourcePacket();
		RadiusService service = ServiceManager.getService(packet.getCode());
		if (service!=null) {
			boolean result = service.service(context);
			if (result) {
				log.info("service failed");
			}
			chain.doFilter(context);
		}
		else {
			log.warn("no service for "+packet);
		}
	}

}
