package radius.attribute;

import radius.RadiusAttribute;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class IntegerAttribute extends RadiusAttribute {
    public static final int INTEGER_ATTRIBUTE_LENGTH = 6;

    private long integer = -1;

    public IntegerAttribute(int type) {
        super(type);
    }

    protected void clearSub() {
        integer = -1;
    }

    protected void decodeValue() {
        assert value != null && value.length == 4 : "bad format,value's length must be 4";
        integer = UnsignedIntegerUtil.parseLong(value);
    }

    protected void encodeValue() {
        if (value == null && integer != -1L) {
            value = UnsignedIntegerUtil.encodeLong(integer);
        }
    }

    protected boolean isLengthValid() {
        return length == INTEGER_ATTRIBUTE_LENGTH;
    }

    protected boolean isValueValid() {
        encodeValue();
        return value != null && value.length + 2 == INTEGER_ATTRIBUTE_LENGTH;
    }

    public long getInteger() {
        return integer;
    }

    public void setInteger(long integer) {
        assert integer > 0 && integer <= 0x0ffffffffL : "not a unsigned integer";
        this.integer = integer;
        this.value = null;
        encodeValue();
        this.length = value.length + 2;
    }

    public void setObjectValue(Object value) {
        if (value == null)
            return;
        if (value instanceof Number) {
            setInteger(((Number) value).longValue());
        } else if (value instanceof String) {
            try {
                long l = Long.parseLong((String) value);
                setInteger(l);
            } catch (Exception e) {
                super.setObjectValue(value);
            }
        } else {
            super.setObjectValue(value);
        }
    }
}