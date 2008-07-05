package radius.server.impl;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

import radius.NAS;
import radius.RadiusPacket;
import radius.server.RadiusContext;
import radius.server.RadiusDataConsumer;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class RadiusContextImpl implements RadiusContext {
	private static int NEXT_HASH = 0;

	private static final int HASH_INCREMENT = 0x71c88643;

	private int hash;

	private RadiusDataConsumer radiusDataConsumer;

	private SocketAddress sourceAddress;

	private SocketAddress localAddress;

	private SocketAddress targetAddress;

	private ByteBuffer sourceBuffer;

	private ByteBuffer targetBuffer;

	private RadiusPacket sourcePacket;

	private RadiusPacket targetPacket;

	private NAS nas;
	
	private int serviceType;

	private HashMap attaches;

	private RadiusDataConsumer consumer;

	private RadiusContextFactoryImpl factoryImpl;

	RadiusContextImpl(RadiusContextFactoryImpl factoryImpl) {
		synchronized (RadiusContextImpl.class) {
			hash = NEXT_HASH;
			NEXT_HASH += HASH_INCREMENT;
		}
		this.factoryImpl = factoryImpl;
		sourceBuffer = ByteBuffer.allocate(RadiusPacket.MAX_PACKET_LENGTH);
		targetBuffer = ByteBuffer.allocate(RadiusPacket.MAX_PACKET_LENGTH);
		sourcePacket = new RadiusPacket();
		targetPacket = new RadiusPacket();

		attaches = new HashMap();
	}

	public RadiusDataConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(RadiusDataConsumer consumer) {
		this.consumer = consumer;
	}

	/*
	 * @see radius.server.impl.RadiusContext#attachValue(java.lang.String,
	 *      java.lang.Object)
	 */
	public void attachValue(String key, Object value) {
		attaches.put(key, value);
	}

	/*
	 * @see radius.server.impl.RadiusContext#isAttachExist(java.lang.String)
	 */
	public boolean isAttachExist(String key) {
		return attaches.containsKey(key);
	}

	/*
	 * @see radius.server.impl.RadiusContext#removeAttach(java.lang.String)
	 */
	public void removeAttach(String key) {
		attaches.remove(key);
	}

	/*
	 * @see radius.server.impl.RadiusContext#getAttach(java.lang.String)
	 */
	public Object getAttach(String key) {
		return attaches.get(key);
	}

	/*
	 * @see radius.server.impl.RadiusContext#getRadiusDataConsumer()
	 */
	public RadiusDataConsumer getRadiusDataConsumer() {
		return radiusDataConsumer;
	}

	/*
	 * @see radius.server.impl.RadiusContext#setRadiusDataConsumer(radius.server.RadiusDataConsumer)
	 */
	public void setRadiusDataConsumer(RadiusDataConsumer radiusDataConsumer) {
		this.radiusDataConsumer = radiusDataConsumer;
	}

	/*
	 * @see radius.server.impl.RadiusContext#getLocalAddress()
	 */
	public SocketAddress getLocalAddress() {
		return localAddress;
	}

	/*
	 * @see radius.server.impl.RadiusContext#setLocalAddress(java.net.SocketAddress)
	 */
	public void setLocalAddress(SocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	/*
	 * @see radius.server.impl.RadiusContext#getSourceAddress()
	 */
	public SocketAddress getSourceAddress() {
		return sourceAddress;
	}

	/*
	 * @see radius.server.impl.RadiusContext#setSourceAddress(java.net.SocketAddress)
	 */
	public void setSourceAddress(SocketAddress sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	/*
	 * @see radius.server.impl.RadiusContext#getSourceBuffer()
	 */
	public ByteBuffer getSourceBuffer() {
		return sourceBuffer;
	}

	public void setSourceBuffer(ByteBuffer sourceBuffer) {
		this.sourceBuffer = sourceBuffer;
	}

	/*
	 * @see radius.server.impl.RadiusContext#getSourcePacket()
	 */
	public RadiusPacket getSourcePacket() {
		return sourcePacket;
	}

	public void setSourcePacket(RadiusPacket sourcePacket) {
		this.sourcePacket = sourcePacket;
	}

	/*
	 * @see radius.server.impl.RadiusContext#getTargetAddress()
	 */
	public SocketAddress getTargetAddress() {
		return targetAddress;
	}

	/*
	 * @see radius.server.impl.RadiusContext#setTargetAddress(java.net.SocketAddress)
	 */
	public void setTargetAddress(SocketAddress targetAddress) {
		this.targetAddress = targetAddress;
	}

	/*
	 * @see radius.server.impl.RadiusContext#getTargetBuffer()
	 */
	public ByteBuffer getTargetBuffer() {
		return targetBuffer;
	}

	public void setTargetBuffer(ByteBuffer targetBuffer) {
		this.targetBuffer = targetBuffer;
	}

	/*
	 * @see radius.server.impl.RadiusContext#getTargetPacket()
	 */
	public RadiusPacket getTargetPacket() {
		return targetPacket;
	}

	public void setTargetPacket(RadiusPacket targetPacket) {
		this.targetPacket = targetPacket;
	}

	void clear() {
		sourceAddress = null;
		localAddress = null;
		targetAddress = null;
		sourceBuffer.clear();
		targetBuffer.clear();
		sourcePacket.clear();
		targetPacket.clear();
		attaches.clear();
	}

    /**
     * @return 返回 serviceType。
     */
    public int getServiceType() {
        return serviceType;
    }
    /**
     * @param serviceType 要设置的 serviceType。
     */
    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
	/*
	 * @see radius.server.impl.RadiusContext#close()
	 */
	public void close() {
		clear();
		factoryImpl.back(this);
	}

	public NAS getNAS() {
		return nas;
	}

	public void setNAS(NAS nas) {
		this.nas = nas;
	}

	public int hashCode() {
		return hash;
	}
}
