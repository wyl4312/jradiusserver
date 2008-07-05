package radius.server;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface RadiusContextFactory {

	RadiusContext getContext(ByteBuffer data, SocketAddress sourceAddress,
			SocketAddress localAddress,RadiusDataConsumer consumer);

}
