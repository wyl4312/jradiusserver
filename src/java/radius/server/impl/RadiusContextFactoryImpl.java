package radius.server.impl;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import radius.server.RadiusContext;
import radius.server.RadiusContextFactory;
import radius.server.RadiusDataConsumer;
import radius.util.Queue;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class RadiusContextFactoryImpl implements RadiusContextFactory {

	private Queue queue;

	public RadiusContextFactoryImpl() {
		queue = new Queue();
	}

	/*
	 * @see radius.server.RadiusContextFactory#getContext(java.nio.ByteBuffer,
	 *      java.net.SocketAddress, java.net.SocketAddress)
	 */
	public RadiusContext getContext(ByteBuffer data,
			SocketAddress sourceAddress, SocketAddress localAddress,
			RadiusDataConsumer consumer) {
		RadiusContextImpl context = getContextImpl();

		ByteBuffer buf = context.getSourceBuffer();
		
		data.flip();
		buf.put(data);

		context.setSourceAddress(sourceAddress);
		context.setLocalAddress(localAddress);
		context.setTargetAddress(sourceAddress);
		context.setConsumer(consumer);

		return context;
	}

	RadiusContextImpl getContextImpl() {
		if (!queue.isEmpty()) {
			synchronized (queue) {
				return (RadiusContextImpl) queue.shift();
			}
		} else {
			return new RadiusContextImpl(RadiusContextFactoryImpl.this);
		}
	}

	void back(RadiusContextImpl impl) {
		synchronized (queue) {
			queue.push(impl);
		}
	}

}
