package radius.server;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public abstract class AbstractRadiusServer implements RadiusServer {
    protected Log log = LogFactory.getLog(getClass());
	protected volatile boolean started;

	protected volatile boolean paused;

	protected volatile boolean stopped;

	protected RadiusContextFactory contextFactory;

	protected RadiusDataConsumer consumer;

	protected SocketAddress authAddress;

	protected SocketAddress accountAddress;

	/*
	 * @see radius.server.RadiusServer#pause()
	 */
	public boolean pause() {
	    log.info("pause server");
		paused = true;
		return true;
	}

	/*
	 * @see radius.server.RadiusServer#goOn()
	 */
	public boolean goOn() {
	    log.info("server goon");
	    paused = false;
		return true;
	}

	/*
	 * @see radius.server.RadiusServer#restart()
	 */
	public boolean restart() {
	    log.info("restart server");
		if (stop()) {
			return start();
		} else {
			return false;
		}
	}

	/*
	 * @see radius.server.RadiusServer#stop()
	 */
	public boolean stop() {
	    log.info("stop server");
		stopped = true;
		// TODO wait for server to stop
		return true;
	}

	/*
	 * @see radius.server.RadiusServer#isStarted()
	 */
	public boolean isStarted() {
		return started;
	}

	/*
	 * @see radius.server.RadiusServer#isPaused()
	 */
	public boolean isPaused() {
		return paused;
	}

	/*
	 * @see radius.server.RadiusServer#isStopped()
	 */
	public boolean isStopped() {
		return stopped;
	}

	/*
	 * @see radius.server.RadiusServer#enableAuth(java.net.SocketAddress)
	 */
	public void enableAuth(SocketAddress address) {
		this.authAddress = address;
	}

	/*
	 * @see radius.server.RadiusServer#enableAccount(java.net.SocketAddress)
	 */
	public void enableAccount(SocketAddress address) {
		this.accountAddress = address;
	}

	/*
	 * @see radius.server.RadiusDataProducer#addConsumer(radius.server.RadiusDataConsumer)
	 */
	public void setConsumer(RadiusDataConsumer consumer) {
		this.consumer = consumer;
	}

	/*
	 * @see radius.server.RadiusDataConsumer#consume(radius.server.RadiusContext)
	 */
	public void consume(RadiusContext context) {
		SocketAddress targetAddress = context.getTargetAddress();
		ByteBuffer resultBuffer = context.getTargetBuffer();
		SocketAddress localAddress = context.getLocalAddress();
		doConsume(targetAddress, resultBuffer, localAddress);
	}

	protected abstract void doConsume(SocketAddress targetAddress,
			ByteBuffer resultBuffer, SocketAddress localAddress);

	public void setRadiusContextFactory(RadiusContextFactory factory) {
		this.contextFactory = factory;
	}

}
