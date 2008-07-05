package radius;

import java.nio.ByteBuffer;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface RadiusParser {

	boolean decode(NAS nas,ByteBuffer data, RadiusPacket packet);

	boolean encode(NAS nas,RadiusPacket packet, ByteBuffer data,byte[] requestAuthenticator,String secret);

}
