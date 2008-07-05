package radius.dict;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * syntax: VENDOR <vendor_name> <vendor_id> [<attr_type>]<br>
 * attr_type may be either "ietf" or "type4"<br>
 * header for ietf = vendor_id[4], vendor_type[1], vendor_length[1]<br>
 * header for type4 = vendor_id[4], vendor_type[4]<br>
 * 
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class VendorDict {

	public final static String IETF_TYPE = "ietf";

	public final static String TYPE4_TYPE = "type4";
	
	public final static String NORMAL_VENDOR_NAME = "";
	
	public final static int NORMAL_VENDOR_ID = -1;

	private String name;

	private int id;

	private String type;

	private Map attributesIdMap;

	private Map attributesNameMap;

	public VendorDict() {
		attributesIdMap = new HashMap();
		attributesNameMap = new HashMap();
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	public void addAttributeDict(AttributeDict ad) {
		attributesIdMap.put(new Integer(ad.getAttributeId()), ad);
		attributesNameMap.put(ad.getAttributeName(), ad);
	}

	public AttributeDict getAttributeById(int attributeId) {
		return (AttributeDict) attributesIdMap.get(new Integer(attributeId));
	}

	public AttributeDict getAttributeByName(String name) {
		return (AttributeDict) attributesIdMap.get(name);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("VendorDict[");
		sb.append("name=").append(this.name).append(",");
		sb.append("id=").append(this.id).append(",");
		sb.append("type=").append(this.type).append("]");
		sb.append("attributes\n");
		Iterator ite = this.attributesIdMap.values().iterator();
		while (ite.hasNext()) {
			sb.append(ite.next()).append("\n");
		}
		return sb.toString();
	}

}
