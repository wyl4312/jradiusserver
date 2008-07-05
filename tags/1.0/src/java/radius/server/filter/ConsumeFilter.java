package radius.server.filter;

import radius.chain.FilterChain;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ConsumeFilter extends AbstractRadiusFilter {

	public ConsumeFilter() {
		super(RadiusFilter.CONSUME_TYPE);
	}

	/*
	 * @see radius.server.filter.AbstractRadiusFilter#doRadiusFilter(radius.server.process.RadiusContext,
	 *      chain.FilterChain)
	 */
	public void doRadiusFilter(RadiusContext context, FilterChain chain) {
		chain.doFilter(context);
		if (context.getTargetBuffer().position() > 0) {
			context.getConsumer().consume(context);
		}
	}

}
