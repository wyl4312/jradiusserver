package radius.attribute;

import java.net.InetAddress;
import java.net.UnknownHostException;

import radius.RadiusAttribute;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class AddressAttriubte extends RadiusAttribute {
    public static final int ADDRESS_ATTRIBUTE_LENGTH = 6;// 2+4

    private InetAddress address;

    public AddressAttriubte(int type) {
        super(type);
    }

    protected void clearSub() {
        address = null;
    }

    protected void decodeValue() {
        assert value != null && value.length == 4 : "bad format,value's length must be 4";
        try {
            address = InetAddress.getByAddress(value);
        } catch (UnknownHostException e) {
            // never happend
            assert e == null;
        }
    }

    protected void encodeValue() {
        if (value == null && address != null) {
            value = address.getAddress();
        }
    }

    protected boolean isLengthValid() {
        return length == ADDRESS_ATTRIBUTE_LENGTH;
    }

    protected boolean isValueValid() {
        encodeValue();
        return value != null && value.length + 2 == ADDRESS_ATTRIBUTE_LENGTH;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        assert address != null : "can't set a null address";
        this.address = address;
        this.value = null;
        encodeValue();
        this.length = value.length + 2;
    }

    public void setObjectValue(Object value) {
        if (value == null)
            return;
        if (value instanceof InetAddress) {
            setAddress((InetAddress) value);
        } else if (value instanceof String) {
            try {
                setAddress(InetAddress.getByName(value.toString()));
            } catch (UnknownHostException e) {
                super.setObjectValue(value);
            }
        } else {
            super.setObjectValue(value);
        }
    }

}