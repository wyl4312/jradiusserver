package radius.server.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.chain.FilterChain;
import radius.chain.FilterConfig;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public abstract class AbstractRadiusFilter implements RadiusFilter {

	private int type;

	protected FilterConfig config;

	protected Log log = LogFactory.getLog(getClass());

	public AbstractRadiusFilter(int type) {
		assert type >= RadiusFilter.FINALIZE_TYPE
				&& type <= RadiusFilter.SERVICE_TYPE : "unknown type";
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void init(FilterConfig config) {
		this.config = config;
		log.info("init filter,class=" + getClass().getName());
	}

	public void doFilter(Object context, FilterChain chain) {
		assert context instanceof RadiusContext : "not a RadiusContext";
		doRadiusFilter((RadiusContext) context, chain);
	}

	protected abstract void doRadiusFilter(RadiusContext context,
			FilterChain chain);

	public void destroy() {

	}

	public String toString() {
		return typeInfo() + " filter(" + getClass().getName() + ")@"
				+ hashCode();
	}

	private String typeInfo() {
		switch (type) {
		case PREPARE_CHECK_TYPE:
			return "prepare check";
		case DECODE_TYPE:
			return "decode";
		case AFTER_CHECK_TYPE:
			return "after check";
		case PROXY_TYPE:
			return "proxy";
		case SERVICE_TYPE:
			return "service";
		case ENCODE_TYPE:
			return "encode";
		case CONSUME_TYPE:
			return "consume";
		case FINALIZE_TYPE:
			return "finalize";
		default:
			return "unknown";
		}
	}
}
