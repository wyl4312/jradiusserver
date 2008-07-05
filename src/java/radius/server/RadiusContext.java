package radius.server;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import radius.NAS;
import radius.RadiusPacket;

public interface RadiusContext {

    void attachValue(String key, Object value);

    boolean isAttachExist(String key);

    void removeAttach(String key);

    Object getAttach(String key);

    RadiusDataConsumer getRadiusDataConsumer();

    void setRadiusDataConsumer(RadiusDataConsumer radiusDataConsumer);

    SocketAddress getLocalAddress();

    void setLocalAddress(SocketAddress localAddress);

    SocketAddress getSourceAddress();

    void setSourceAddress(SocketAddress sourceAddress);

    ByteBuffer getSourceBuffer();

    RadiusPacket getSourcePacket();

    SocketAddress getTargetAddress();

    void setTargetAddress(SocketAddress targetAddress);

    ByteBuffer getTargetBuffer();

    RadiusPacket getTargetPacket();

    RadiusDataConsumer getConsumer();

    NAS getNAS();

    void setNAS(NAS nas);

    /**
     * @return 返回 serviceType。
     */
    public int getServiceType();

    /**
     * @param serviceType
     *            要设置的 serviceType。
     */
    public void setServiceType(int serviceType);

    void close();

}