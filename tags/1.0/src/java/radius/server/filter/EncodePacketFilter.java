package radius.server.filter;

import java.nio.ByteBuffer;

import radius.RadiusPacket;
import radius.chain.FilterChain;
import radius.parser.DefaultRadiusParser;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class EncodePacketFilter extends AbstractRadiusFilter {
	private DefaultRadiusParser parser;

	public EncodePacketFilter() {
		super(RadiusFilter.ENCODE_TYPE);
		parser = new DefaultRadiusParser();
	}

	/*
	 * @see radius.server.filter.AbstractRadiusFilter#doRadiusFilter(radius.server.RadiusContext,
	 *      chain.FilterChain)
	 */
	public void doRadiusFilter(RadiusContext context, FilterChain chain) {
		chain.doFilter(context);
		RadiusPacket source = context.getSourcePacket();
		RadiusPacket targetPacket = context.getTargetPacket();
		ByteBuffer target = context.getTargetBuffer();

		if (targetPacket.getCode() == 0) {
			return;
		}

		boolean result = parser.encode(context.getNAS(),targetPacket, target, source
				.getAuthenticator(), context.getNAS().getSecret());

		if (log.isDebugEnabled()) {
			log.debug("result=\n" + targetPacket);
		}
	}

}
