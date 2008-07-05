package radius.server.filter;

import radius.chain.FilterChain;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class FormatCheckFilter extends AbstractRadiusFilter {

	public FormatCheckFilter() {
		super(RadiusFilter.PREPARE_CHECK_TYPE);
	}

	/*
	 * @see chain.Filter#doFilter(chain.Context, chain.FilterChain)
	 */
	public void doRadiusFilter(RadiusContext context, FilterChain chain) {
		// TODO Auto-generated method stub
		chain.doFilter(context);
	}
}
