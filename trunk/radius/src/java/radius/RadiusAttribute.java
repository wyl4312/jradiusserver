package radius;

import radius.attribute.AddressAttriubte;
import radius.attribute.IntegerAttribute;
import radius.attribute.StringAttribute;
import radius.attribute.TimeAttribute;
import radius.dict.AttributeDict;
import radius.dict.VendorDict;
import radius.dict.VendorDictManager;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class RadiusAttribute {
    /* ****************** Constant Attribute Types ************************ */
    public static final int USER_NAME = 1;

    public static final int USER_PASSWORD = 2;

    public static final int CHAP_PASSWORD = 3;

    public static final int NAS_IP_ADDRESS = 4;

    public static final int NAS_PORT = 5;

    public static final int SERVICE_TYPE = 6;

    public static final int FRAMED_PROTOCOL = 7;

    public static final int FRAMED_IP_ADDRESS = 8;

    public static final int FRAMED_IP_NETMASK = 9;

    public static final int FRAMED_ROUTING = 10;

    public static final int FILTER_ID = 11;

    public static final int FRAMED_MTU = 12;

    public static final int FRAMED_COMPRESSION = 13;

    public static final int LOGIN_IP_HOST = 14;

    public static final int LOGIN_SERVICE = 15;

    public static final int LOGIN_TCP_PORT = 16;

    // 17 (unassigned)
    public static final int REPLY_MESSAGE = 18;

    public static final int CALLBACK_NUMBER = 19;

    public static final int CALLBACK_ID = 20;

    // 21 (unassigned)
    public static final int FRAMED_ROUTE = 22;

    public static final int FRAMED_IPX_NETWORK = 23;

    public static final int STATE = 24;

    public static final int CLASS = 25;

    public static final int VENDOR_SPECIFIC = 26;

    public static final int SESSION_TIMEOUT = 27;

    public static final int IDLE_TIMEOUT = 28;

    public static final int TERMINATION_ACTION = 29;

    public static final int CALLED_STATION_ID = 30;

    public static final int CALLING_STATION_ID = 31;

    public static final int NAS_IDENTIFIER = 32;

    public static final int PROXY_STATE = 33;

    public static final int LOGIN_LAT_SERVICE = 34;

    public static final int LOGIN_LAT_NODE = 35;

    public static final int LOGIN_LAT_GROUP = 36;

    public static final int FRAMED_APPLETALK_LINK = 37;

    public static final int FRAMED_APPLETALK_NETWORK = 38;

    public static final int FRAMED_APPLETALK_ZONE = 39;

    // 40_59 (reserved for accounting)
    public static final int ACCT_STATUS_TYPE = 40;

    public static final int ACCT_DELAY_TIME = 41;

    public static final int ACCT_INPUT_OCTETS = 42;

    public static final int ACCT_OUTPUT_OCTETS = 43;

    public static final int ACCT_SESSION_ID = 44;

    public static final int ACCT_AUTHENTIC = 45;

    public static final int ACCT_SESSION_TIME = 46;

    public static final int ACCT_INPUT_PACKETS = 47;

    public static final int ACCT_OUTPUT_PACKETS = 48;

    public static final int ACCT_TERMINATE_CAUSE = 49;

    public static final int ACCT_MULTI_SESSION_ID = 50;

    public static final int ACCT_LINK_COUNT = 51;

    public static final int ACCT_INPUT_GIGAWORDS = 52;

    public static final int ACCT_OUTPUT_GIGAWORDS = 53;

    public static final int EVENT_TIMESTAMP = 55;

    public static final int CHAP_CHALLENGE = 60;

    public static final int NAS_PORT_TYPE = 61;

    public static final int PORT_LIMIT = 62;

    public static final int LOGIN_LAT_PORT = 63;

    public static final int ARAP_PASSWORD = 70;

    public static final int ARAP_FEATURES = 71;

    public static final int ARAP_ZONE_ACCESS = 72;

    public static final int ARAP_SECURITY = 73;

    public static final int ARAP_SECURITY_DATA = 74;

    public static final int PASSWORD_RETRY = 75;

    public static final int PROMPT = 76;

    public static final int CONNECT_INFO = 77;

    public static final int CONFIGURATION_TOKEN = 78;

    public static final int EAP_MESSAGE = 79;

    public static final int MESSAGE_AUTHENTICATOR = 80;

    public static final int ARAP_CHALLENGE_RESPONSE = 84;

    public static final int ACCT_INTERIM_INTERVAL = 85;

    public static final int NAS_PORT_ID = 87;

    public static final int FRAMED_POOL = 88;

    /* ******************* Constant Attribute Types ************************* */

    /* ****************** Constant Attribute Values ************************ */
    // Service-Type or User Types
    public static final int LOGIN = 1;

    public static final int FRAMED = 2;

    public static final int CALLBACK_LOGIN = 3;

    public static final int CALLBACK_FRAMED = 4;

    public static final int OUTBOUND = 5;

    public static final int ADMINISTRATIVE = 6;

    public static final int NAS_PROMPT = 7;

    public static final int AUTHENTICATE_ONLY = 8;

    public static final int CALLBACK_NAS_PROMPT = 9;

    public static final int Call_CHECK = 10;

    public static final int CALLBACK_ADMINISTRATIVE = 11;

    // Framed-Protocol
    public static final int PPP = 1;

    public static final int SLIP = 2;

    public static final int ARAP = 3;

    public static final int GANDALF_SLML = 4;

    public static final int XYLOGICS_PROPRIETARY_IPX_SLIP = 5;

    public static final int X75_SYNCHRONOUS = 6;

    // Framed-Routing
    public static final int NONE = 0;

    public static final int BROADCAST = 1;

    public static final int LISTEN = 2;

    public static final int BROADCAST_LISTEN = 3;

    // Framed-Compression
    // public static final int None = 0;
    public static final int VJ_TCP_IP_HEADER_COMPRESSION = 1;

    public static final int IPX_HEADER_COMPRESSION = 2;

    public static final int STAC_LZS_COMPRESSION = 3;

    // Login-Service
    public static final int TELNET = 0;

    public static final int RLOGIN = 1;

    public static final int TCP_CLEAR = 2;

    public static final int PORTMASTER = 3;

    public static final int LAT = 4;

    public static final int X25_PAD = 5;

    public static final int X25_T3POS = 7;

    public static final int TCP_CLEAR_QUIET = 8;

    // Termination-Action
    public static final int DEFAULT = 0;

    public static final int RADIUS_REQUEST = 1;

    // NAS-PORT-TYPE
    public static final int ASYNC = 0;

    public static final int SYNC = 1;

    public static final int ISDN_SYNC = 2;

    public static final int ISDN_ASYNC_V120 = 3;

    public static final int ISDN_ASYNC_V110 = 4;

    public static final int VIRTUAL = 5;

    public static final int PIAFS = 6;

    public static final int HDLC_CLEAR_CHANNEL = 7;

    public static final int X25 = 8;

    public static final int X75 = 9;

    public static final int G3_FAX = 10;

    public static final int SDSL = 11;

    public static final int ADSL_CAP = 12;

    public static final int ADSL_DMT = 13;

    public static final int IDSL = 14;

    public static final int ETHERNET = 15;

    public static final int XDSL = 16;

    public static final int CABLE = 17;

    public static final int WIRELESS_OTHER = 18;

    public static final int WIRELESS_IEEE_802_11 = 19;

    /* ****************** Constant Attribute Values ************************ */
    /* ***************** Attributes and sub attributes for SIP ************** */

    // SIP DIGEST AUTH - draft-sterman-aaa-sip-00
    public static final int DIGEST_RESPONSE = 206;

    public static final int DIGEST_ATTRIBUTE = 207;

    // SIP DIGEST AUTH - draft-sterman-aaa-sip-00
    public static final int SIP_REALM = 1;

    public static final int SIP_NONCE = 2;

    public static final int SIP_METHOD = 3;

    public static final int SIP_URI = 4;

    public static final int SIP_QOP = 5;

    public static final int SIP_ALGORITHM = 6;

    public static final int SIP_BODY_DIGEST = 7;

    public static final int SIP_CNONCE = 8;

    public static final int SIP_NONCE_COUNT = 9;

    public static final int SIP_USER_NAME = 10;

    /* ***************** Attributes and sub attributes for SIP ************** */

    protected int type;

    protected int length;

    protected byte[] value;

    protected RadiusAttribute next;

    private static int NEXT_HASH = 0;

    private final static int HASH_INCREMENT = 0x989693;

    private int hash;

    protected RadiusAttribute(int type) {
        this.type = type;
        synchronized (RadiusAttribute.class) {
            hash = NEXT_HASH;
            NEXT_HASH += HASH_INCREMENT;
        }
    }

    public static RadiusAttribute getAttribute(NAS nas, int id) {
        if (nas == null) {
            return new RadiusAttribute(id);
        }
        VendorDict dict = VendorDictManager.getFactory().getVendorDict(
                nas.getVendor());
        AttributeDict ad = dict.getAttributeById(id);
        int valueType = ad.getIntType();
        switch (valueType) {
        case AttributeDict.STRING_INT_TYPE:
            return new StringAttribute(id);
        case AttributeDict.IPADDR_INT_TYPE:
            return new AddressAttriubte(id);
        case AttributeDict.INTEGER_INT_TYPE:
            return new IntegerAttribute(id);
        case AttributeDict.DATE_INT_TYPE:
            return new TimeAttribute(id);
        case AttributeDict.TAG_INT_INT_TYPE:
            //TODO
            return new RadiusAttribute(id);
        case AttributeDict.TAG_STR_INT_TYPE:
            //TODO
            return new RadiusAttribute(id);
        default:
            return new RadiusAttribute(id);
        }
    }

    /**
     * @return Returns the length.
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length
     *            The length to set.
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }

    /**
     * @return Returns the value.
     */
    public byte[] getValue() {
        encodeValue();
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(byte[] value) {
        this.value = value;
        if (value != null && length == 0)
            this.length = value.length + 2;
        decodeValue();
    }

    public void setObjectValue(Object value) {
        if (value == null)
            return;
        if (value instanceof byte[]) {
            setValue((byte[]) value);
        } else {
            setValue(value.toString().getBytes());
        }
    }

    public void clear() {
        clearSub();
        this.type = 0;
        this.length = 0;
        this.value = null;
    }

    protected void clearSub() {
        // this method should override by subclass
    }

    protected void decodeValue() {
        // this method should override by subclass
    }

    protected void encodeValue() {
        // this method should override by subclass
    }

    public boolean isValid() {
        return isTypeValid() && isLengthValid() && isValueValid();
    }

    protected boolean isTypeValid() {
        return type > 0 && type < 256;
    }

    protected boolean isLengthValid() {
        if (value == null) {
            return false;
        } else {
            return length > 2 && length <= 255 && (value.length + 2 == length);
        }
    }

    protected boolean isValueValid() {
        // this method should override by subclass
        return true;
    }

    /**
     * @return Returns the next.
     */
    public RadiusAttribute getNext() {
        return next;
    }

    /**
     * @param next
     *            The next to set.
     */
    public void setNext(RadiusAttribute next) {
        assert next == null || next.type == this.type;
        this.next = next;
    }

    public int hashCode() {
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof RadiusAttribute) {
            RadiusAttribute ra = (RadiusAttribute) obj;
            if (ra.type == this.type && ra.length == ra.length) {
                if (ra.getValue() != null && this.getValue() != null) {
                    int len = this.value.length;
                    if (ra.value.length == this.value.length) {
                        for (int i = 0; i < len; i++) {
                            if (ra.value[i] != value[i]) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
                if (ra.getValue() == null && getValue() == null)
                    return true;
                return false;
            }
        }
        return false;
    }

}