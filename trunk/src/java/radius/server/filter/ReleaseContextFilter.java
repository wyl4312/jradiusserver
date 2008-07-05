package radius.server.filter;

import radius.chain.FilterChain;
import radius.server.*;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ReleaseContextFilter extends AbstractRadiusFilter {

	public ReleaseContextFilter() {
		super(RadiusFilter.FINALIZE_TYPE);
	}
	
	public void doRadiusFilter(RadiusContext context,FilterChain chain) {
		chain.doFilter(context);
		context.close();
	}

}
