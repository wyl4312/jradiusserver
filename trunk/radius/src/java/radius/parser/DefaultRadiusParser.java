package radius.parser;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.NAS;
import radius.RadiusAttribute;
import radius.RadiusPacket;
import radius.RadiusParser;
import radius.util.AuthenticatorUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class DefaultRadiusParser implements RadiusParser {
    private Log log = LogFactory.getLog(getClass());

    /**
     * .
     * 
     * @see radius.RadiusParser#decode(java.nio.ByteBuffer, radius.RadiusPacket)
     */
    public boolean decode(NAS nas, ByteBuffer data, RadiusPacket packet) {
        int actualLength = data.position();
        if (actualLength < RadiusPacket.MIN_PACKET_LENGTH) {// check length
            log.info("decode failed,data is too small,only " + actualLength
                    + " bytes");
            return false;
        }
        if (actualLength > RadiusPacket.MAX_PACKET_LENGTH) {// check length
            log.info("decode failed,data is too larger,has " + actualLength
                    + " bytes");
            return false;
        }
        try {
            data.flip();
            // read header
            int code = data.get();
            int identifier = data.get();
            int length = data.getShort();
            if (length < RadiusPacket.MIN_PACKET_LENGTH
                    || actualLength < length) {
                // check length again
                log.info("decode failed,data length is too small,only "
                        + length + " bytes");
                return false;
            }
            packet.setCode(code);
            packet.setIdentifier(identifier);
            packet.setLength(length);
            byte[] authenticator = new byte[16];
            data.get(authenticator);
            packet.setAuthenticator(authenticator);

            // read type-length-value tuple
            int temp;
            while (data.position() < packet.getLength()) {
                code = data.get();
                if (code <= 0 || code >= 256) {
                    return false;
                }
                length = data.get();
                temp = length - 2;
                if (temp <= 0) {// only value length>0 is valid
                    log.debug("attribute length must>0,but was " + temp);
                    return false;
                }
                byte[] value = new byte[temp];
                data.get(value);
                RadiusAttribute attribute = RadiusAttribute.getAttribute(nas,
                        code);
                attribute.setLength(length);
                attribute.setValue(value);
                if (!packet.addAttribute(attribute)) {
                    // if can't add this attribute to packet
                    log.warn("decode failed,can't add attribute(" + attribute
                            + ") to RadiusPacket");
                    return false;
                }
            }
            return true;
        } finally {
            data.clear();
            data.position(actualLength);
        }
    }

    /**
     * 
     * @see radius.RadiusParser#encode(radius.RadiusPacket, java.nio.ByteBuffer)
     */
    public boolean encode(NAS nas, RadiusPacket packet, ByteBuffer data,
            byte[] requestAuthenticator, String secret) {
        data.clear();
        data.put((byte) packet.getCode());
        data.put((byte) packet.getIdentifier());
        data.putShort((short) packet.getLength());
        data.put(requestAuthenticator);

        Map atts = packet.getAttributes();
        Iterator ite = atts.values().iterator();
        while (ite.hasNext()) {
            RadiusAttribute att = (RadiusAttribute) ite.next();
            if (att != null) {
                do {
                    data.put((byte) att.getType());
                    data.put((byte) att.getLength());
                    data.put(att.getValue());
                } while ((att = att.getNext()) != null);
            }
        }

        int pos = data.position();
        data.position(2);
        data.putShort((short) pos);
        packet.setLength(pos);

        data.position(pos);
        byte[] authenticator = AuthenticatorUtil.createAuthenticator(data,
                secret);
        if (authenticator != null) {
            packet.setAuthenticator(authenticator);

            data.position(4);
            data.put(authenticator);

        } else {
            log.error("encode failed: create authenticator failed");
            data.clear();
            return false;
        }
        data.position(pos);
        return true;
    }
}