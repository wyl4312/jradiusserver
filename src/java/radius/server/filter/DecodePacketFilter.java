package radius.server.filter;

import radius.RadiusPacket;
import radius.chain.FilterChain;
import radius.parser.DefaultRadiusParser;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class DecodePacketFilter extends AbstractRadiusFilter {

	private DefaultRadiusParser parser;

	public DecodePacketFilter() {
		super(RadiusFilter.DECODE_TYPE);
		parser = new DefaultRadiusParser();
	}

	/*
	 * @see chain.Filter#doFilter(chain.Context, chain.FilterChain)
	 */
	public void doRadiusFilter(RadiusContext context, FilterChain chain) {
		RadiusPacket packet = context.getSourcePacket();
		boolean ok = parser.decode(context.getNAS(),context.getSourceBuffer(), packet);
		if (ok == false) {
			log.warn("decode packet failed,data=" + context.getSourceBuffer());
			return;
		}
		context.getSourceBuffer().clear();

		if (log.isDebugEnabled()) {
			log.debug("source address=" + context.getSourceAddress());
			log.debug(packet.toXml());
		}
		chain.doFilter(context);
	}

}
