package radius.server;

import java.net.SocketAddress;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface RadiusServer extends RadiusDataProducer, RadiusDataConsumer {

	public boolean start();

	public boolean pause();

	public boolean goOn();

	public boolean restart();

	public boolean stop();

	public boolean isStarted();

	public boolean isPaused();

	public boolean isStopped();

	public void enableAuth(SocketAddress address);

	public void enableAccount(SocketAddress address);

	public void setRadiusContextFactory(RadiusContextFactory factory);

}
