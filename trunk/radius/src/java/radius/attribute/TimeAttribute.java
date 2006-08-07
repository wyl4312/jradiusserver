package radius.attribute;

import java.util.Date;

import radius.RadiusAttribute;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class TimeAttribute extends RadiusAttribute {
	public static final int TIME_ATTRIBUTE_LENGTH = 6;// 2+4

	private long time = -1;

	public TimeAttribute(int type) {
		super(type);
	}

	protected void decodeValue() {
		assert value != null && value.length == 4 : "bad format,value's length must be 4";
		time = UnsignedIntegerUtil.parseLong(value);
	}

	protected void encodeValue() {
		if (value == null && time != -1L) {
			value = UnsignedIntegerUtil.encodeLong(time);
		}
	}
	
	protected void clearSub() {
		time = -1;
	}

	protected boolean isLengthValid() {
		return length == TIME_ATTRIBUTE_LENGTH;
	}

	protected boolean isValueValid() {
		encodeValue();
		return value != null && value.length+2 == TIME_ATTRIBUTE_LENGTH;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		assert time>=0 && time<=0x0ffffffffL:time+"is not a unsigned integer";
		this.time = time;
		this.value = null;
		encodeValue();
		this.length = value.length+2;
	}

	public Date getDate() {
		if (time != -1) {
			return new Date(time * 1000);
		} else {
			return null;
		}
	}
	
	public void setObjectValue(Object value) {
	    if (value!=null)
	        return;
	    if (value instanceof Date) {
	        setTime(((Date)value).getTime());
	    }
	    else if (value instanceof Long) {
	        setTime(((Long)value).longValue());
	    }
	    else if (value instanceof String) {
	        try {
	            long l = Long.parseLong((String)value);
	            setTime(l);
	        }catch (Exception e) {
	            super.setObjectValue(value);
	        }
	    }
	    else {
	        super.setObjectValue(value);
	    }
	}
}
