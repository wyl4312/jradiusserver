package radius;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import radius.util.ConvertUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 * 
 */
public class RadiusPacket {

	public static final int MIN_PACKET_LENGTH = 20;

	public static final int MAX_PACKET_LENGTH = 4096;

	/**
	 * RADIUS_HEADER_LENGTH is 20 bytes (corresponding to 1 byte for code + 1
	 * byte for Identifier + 2 bytes for Length + 16 bytes for Request
	 * Authenticator) It is not a coincidence that it is the same as the
	 * MIN_PACKET_LENGTH
	 */
	public static final int RADIUS_HEADER_LENGTH = 20;

	public static final String EMPTYSTRING = "";

	/* *************** Constant Packet Type Codes ************************* */
	public static final int ACCESS_REQUEST = 1;

	public static final int ACCESS_ACCEPT = 2;

	public static final int ACCESS_REJECT = 3;

	public static final int ACCOUNTING_REQUEST = 4;

	public static final int ACCOUNTING_RESPONSE = 5;

	public static final int ACCOUNTING_STATUS = 6;

	public static final int PASSWORD_REQUEST = 7;

	public static final int PASSWORD_ACCEPT = 8;

	public static final int PASSWORD_REJECT = 9;

	public static final int ACCOUNTING_MESSAGE = 10;

	public static final int ACCESS_CHALLENGE = 11;

	public static final int STATUS_SERVER = 12; // experimental

	public static final int STATUS_CLIENT = 13; // experimental

	public static final int RESERVED = 255;

	/* ****************** Constant Packet Type Codes ************************ */

	private int code;

	private int identifier;

	private int length;

	private byte[] authenticator;

	private Map attributes;

	public RadiusPacket() {
		attributes = new HashMap();
	}
	
	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return Returns the identifier.
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            The identifier to set.
	 */
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
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
	 * @return Returns the requestAuthenticator.
	 */
	public byte[] getAuthenticator() {
		return authenticator;
	}

	/**
	 * @param requestAuthenticator
	 *            The requestAuthenticator to set.
	 */
	public void setAuthenticator(byte[] authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * @return Returns the attributes.
	 */
	public Map getAttributes() {
		return attributes;
	}

	public boolean addAttribute(RadiusAttribute attribute) {
		int type = attribute.getType();
		if (type <= 0 || type > 255) {
			// TODO log this
			return false;
		}
		Integer key = new Integer(type);
		RadiusAttribute old = (RadiusAttribute) attributes.get(key);
		if (old==null) {
			attributes.put(key,attribute);
		}
		else {
			while (old.getNext()!=null) {
				old = old.getNext();
			}
			old.setNext(attribute);
		}
		return true;
	}

	public RadiusAttribute getAttribute(int type) {
		return (RadiusAttribute) attributes.get(new Integer(type));
	}
	
	public void removeAttribute(int type) {
		attributes.remove(new Integer(type));
	}

	public void clear() {
		this.code = 0;
		this.identifier = 0;
		this.length = 0;
		this.authenticator = null;
		attributes.clear();
	}

	public boolean isValid() {
		// TODO
		return true;
	}
	
	public void copy(RadiusPacket source) {
		this.code = source.code;
		this.length = source.length;
		this.identifier = source.identifier;
		this.authenticator = source.authenticator;
		Iterator ite = source.attributes.keySet().iterator();
		while (ite.hasNext()) {
			Object key = (Object) ite.next();
			this.attributes.put(key,source.attributes.get(key));
		}
	}

	public String toString() {
		return toXml();
	}

	public String toXml() {
		StringBuffer sb = new StringBuffer();
		String indent = "    ";
		sb.append("<radiuspacket>").append("\n");
		sb.append(indent).append("<code>").append(this.getCode()&0xff).append(
				"</code>").append("\n");
		sb.append(indent).append("<identifier>").append(this.getIdentifier()&0xff)
				.append("</identifier>").append("\n");
		sb.append(indent).append("<length>").append(this.getLength()).append(
				"</length>").append("\n");
		sb.append(indent).append("<authenticator>");
		ConvertUtil.toHex(this.getAuthenticator(), sb);
		sb.append("H");
		sb.append("</authenticator>").append("\n");
		sb.append(indent).append("<attributes>").append("\n");
		Iterator ite = attributes.values().iterator();
		while(ite.hasNext()) {
			RadiusAttribute ra = (RadiusAttribute)ite.next();
			if (ra == null)
				continue;
			do {
				sb.append(indent).append(indent).append("<attribute>").append(
						"\n");
				sb.append(indent).append(indent).append(indent)
						.append("<type>").append(ra.getType()).append(
								"</type>").append("\n");
				sb.append(indent).append(indent).append(indent).append(
						"<length>").append(ra.getLength()).append(
						"</length>").append("\n");
				sb.append(indent).append(indent).append(indent).append(
						"<value>");
				sb.append(new String(ra.getValue())).append("(");
				ConvertUtil.toHex(ra.getValue(), sb);
				sb.append("H)").append("</value>").append("\n");
				sb.append(indent).append(indent).append("</attribute>").append(
						"\n");
			}while((ra = ra.getNext())!=null);
		}

		sb.append(indent).append("</attributes>").append("\n");
		sb.append("</radiuspacket>").append("\n");
		return sb.toString();
	}
}