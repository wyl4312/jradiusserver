package radius.server.service.pojo;

import java.io.Serializable;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class ServiceType implements Serializable {

    private String name;

    private String type;

    private Integer attId;

    private String unit;

    private String value;

    /**
     * @return 返回 value。
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            要设置的 value。
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return Returns the attId.
     */
    public Integer getAttId() {
        return attId;
    }

    /**
     * @param attId
     *            The attId to set.
     */
    public void setAttId(Integer attId) {
        this.attId = attId;
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

    /**
     * @return Returns the unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit
     *            The unit to set.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

}