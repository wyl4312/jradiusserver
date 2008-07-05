package radius.server.service.pojo;

import java.io.Serializable;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class User implements Serializable{

    private Long id;

    private String name;

    private String password;

    private char encrypt;

    private String authMethod;

    private char status;

    private int loginCount;

    private int groupId;

    /**
     * @return Returns the authMethod.
     */
    public String getAuthMethod() {
        return authMethod;
    }

    /**
     * @param authMethod
     *            The authMethod to set.
     */
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    /**
     * @return Returns the encrypt.
     */
    public char getEncrypt() {
        return encrypt;
    }

    /**
     * @param encrypt
     *            The encrypt to set.
     */
    public void setEncrypt(char encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
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
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return 返回 loginCount。
     */
    public int getLoginCount() {
        return loginCount;
    }

    /**
     * @param loginCount
     *            要设置的 loginCount。
     */
    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    /**
     * @return 返回 status。
     */
    public char getStatus() {
        return status;
    }

    /**
     * @param status
     *            要设置的 status。
     */
    public void setStatus(char status) {
        this.status = status;
    }

    /**
     * @return 返回 groupId。
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     *            要设置的 groupId。
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}