package radius.dict;

/**
 * Valid types: <br>
 * string - 0-253 octets <br>
 * ipaddr - 4 octets in network byte order <br>
 * integer - 32 bit value in big endian order (high byte first) <br>
 * date - 32 bit value in big endian order - seconds since <br>
 * 00:00:00 GMT, Jan. 1, 1970 <br>
 * tag-str - same as string but with tunnel tag id <br>
 * tag-int - same as integer but with tunnel tag id <br>
 * 
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class AttributeDict {
    public final static String STRING_TYPE = "string";

    public final static String IPADDR_TYPE = "ipaddr";

    public final static String INTEGER_TYPE = "integer";

    public final static String DATE_TYPE = "date";

    public final static String TAG_STR_TYPE = "tag-str";

    public final static String TAG_INT_TYPE = "tag-int";

    public final static int UNKNOWN_INT_TYPE = -1;

    public final static int STRING_INT_TYPE = 0;

    public final static int IPADDR_INT_TYPE = 1;

    public final static int INTEGER_INT_TYPE = 2;

    public final static int DATE_INT_TYPE = 3;

    public final static int TAG_STR_INT_TYPE = 4;

    public final static int TAG_INT_INT_TYPE = 5;

    private String attributeName;

    private int attributeId;

    private String attributeType;

    private int intType = UNKNOWN_INT_TYPE;

    private String vendor;

    /**
     * @return Returns the attributeId.
     */
    public int getAttributeId() {
        return attributeId;
    }

    /**
     * @param attributeId
     *            The attributeId to set.
     */
    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

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
     * @return Returns the attributeType.
     */
    public String getAttributeType() {
        return attributeType;
    }

    /**
     * @param attributeType
     *            The attributeType to set.
     */
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
        if (STRING_TYPE.equalsIgnoreCase(attributeType)) {
            this.intType = STRING_INT_TYPE;
        } else if (IPADDR_TYPE.equalsIgnoreCase(attributeType)) {
            this.intType = IPADDR_INT_TYPE;
        } else if (INTEGER_TYPE.equalsIgnoreCase(attributeType)) {
            this.intType = INTEGER_INT_TYPE;
        } else if (DATE_TYPE.equalsIgnoreCase(attributeType)) {
            this.intType = DATE_INT_TYPE;
        } else if (TAG_STR_TYPE.equalsIgnoreCase(attributeType)) {
            this.intType = TAG_STR_INT_TYPE;
        } else if (TAG_INT_TYPE.equalsIgnoreCase(attributeType)) {
            this.intType = TAG_INT_INT_TYPE;
        } else {
            this.intType = UNKNOWN_INT_TYPE;
        }
    }

    public int getIntType() {
        return intType;
    }

    /**
     * @return Returns the vendor.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor
     *            The vendor to set.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("AttributeDict[");
        sb.append("attributeName=").append(this.attributeName).append(",");
        sb.append("attributeId=").append(this.attributeId).append(",");
        sb.append("attributeType=").append(this.attributeType).append(",");
        sb.append("vendor=").append(this.vendor).append("]");
        return sb.toString();
    }

}