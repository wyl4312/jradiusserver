package radius.attribute;

import java.io.UnsupportedEncodingException;

import radius.RadiusAttribute;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class StringAttribute extends RadiusAttribute {
	public static final String STRING_ENCODE = "ISO-8859-1";

	private String string;

	public StringAttribute(int type) {
		super(type);
	}

	protected void decodeValue() {
		try {
			string = new String(value, STRING_ENCODE);
		} catch (UnsupportedEncodingException e) {
			// this should never happend
			assert false : STRING_ENCODE
					+ " is always supported by java,but why this happend";
		}
	}

	protected void encodeValue() {
		if (value == null && string != null) {
			try {
				value = string.getBytes(STRING_ENCODE);
			} catch (UnsupportedEncodingException e) {
				// this should never happend
				assert false : STRING_ENCODE
						+ " is always supported by java,but why this happend";
			}
		}
	}

	protected void clearSub() {
		this.string = null;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		assert string!=null:"can't set a null string";
		this.string = string;
		this.value = null;
		encodeValue();
		this.length = value.length+2;
	}

}
