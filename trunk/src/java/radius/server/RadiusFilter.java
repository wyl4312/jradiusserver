package radius.server;

import radius.chain.Filter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface RadiusFilter extends Filter {
	public final static int FINALIZE_TYPE = 1;

	public final static int PREPARE_CHECK_TYPE = 2;

	public final static int DECODE_TYPE = 3;

	public final static int AFTER_CHECK_TYPE = 4;

	public final static int PROXY_TYPE = 5;

	public final static int CONSUME_TYPE = 6;

	public final static int ENCODE_TYPE = 7;

	public final static int SERVICE_TYPE = 8;

	public final static int UNKNOWN_TYPE = -1;

	public int getType();

}
