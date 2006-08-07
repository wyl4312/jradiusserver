package radius.attribute;

import java.io.UnsupportedEncodingException;

import radius.RadiusAttribute;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class TextAttribute extends RadiusAttribute {
	public static final String TEXT_ENCODE = "UTF-8";

	protected String text;

	public TextAttribute(int type) {
		super(type);
	}

	protected void decodeValue() {
		try {
			text = new String(value, TEXT_ENCODE);
		} catch (UnsupportedEncodingException e) {
			// this should never happend
			assert false : TEXT_ENCODE
					+ " is always supported by java,but why this happend";
		}
	}

	protected void encodeValue() {
		if (value == null && text != null) {
			try {
				value = text.getBytes(TEXT_ENCODE);
			} catch (UnsupportedEncodingException e) {
				// this should never happend
				assert false : TEXT_ENCODE
						+ " is always supported by java,but why this happend";
			}
		}
	}

	protected void clearSub() {
		this.text = null;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		assert text!=null:"can't set a null text";
		this.text = text;
		this.value = null;
		encodeValue();
		this.length = value.length+2;
	}

}
