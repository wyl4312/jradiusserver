package radius.dict;

/**
 * syntax: VALUE <attr-name> <value-enum-name> <value-integer>
 * 
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ValueDict {

	private String attributeName;

	private String valueEnumName;

	private int value;

	/**
	 * @return Returns the attributeName.
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName
	 *            The attributeName to set.
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return Returns the valueName.
	 */
	public String getValueEnumName() {
		return valueEnumName;
	}

	/**
	 * @param valueName
	 *            The valueName to set.
	 */
	public void setValueEnumName(String valueName) {
		this.valueEnumName = valueName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ValueDict[");
		sb.append("attributeName=").append(this.attributeName).append(",");
		sb.append("valueEnumName=").append(this.valueEnumName).append(",");
		sb.append("value=").append(this.value).append("]");
		return sb.toString();
	}
}
