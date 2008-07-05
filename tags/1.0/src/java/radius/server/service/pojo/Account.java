package radius.server.service.pojo;

import java.io.Serializable;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class Account implements Serializable{

	private Long id;

	private String userName;

	private Integer statusType;

	private Integer delayTime;

	private Long inputOctects;

	private Long outputOctects;

	private String sessionId;

	private Integer authentic;

	private Integer sessionTime;

	private Integer inputPackets;

	private Integer outputPackets;

	private Integer terminateCause;

	private String multiSessionId;

	private Integer linkCount;

	/**
	 * @return Returns the authentic.
	 */
	public Integer getAuthentic() {
		return authentic;
	}

	/**
	 * @param authentic
	 *            The authentic to set.
	 */
	public void setAuthentic(Integer authentic) {
		this.authentic = authentic;
	}

	/**
	 * @return Returns the delayTime.
	 */
	public Integer getDelayTime() {
		return delayTime;
	}

	/**
	 * @param delayTime
	 *            The delayTime to set.
	 */
	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
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
	 * @return Returns the inputOctects.
	 */
	public Long getInputOctects() {
		return inputOctects;
	}

	/**
	 * @param inputOctects
	 *            The inputOctects to set.
	 */
	public void setInputOctects(Long inputOctects) {
		this.inputOctects = inputOctects;
	}

	/**
	 * @return Returns the inputPackets.
	 */
	public Integer getInputPackets() {
		return inputPackets;
	}

	/**
	 * @param inputPackets
	 *            The inputPackets to set.
	 */
	public void setInputPackets(Integer inputPackets) {
		this.inputPackets = inputPackets;
	}

	/**
	 * @return Returns the linkCount.
	 */
	public Integer getLinkCount() {
		return linkCount;
	}

	/**
	 * @param linkCount
	 *            The linkCount to set.
	 */
	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}

	/**
	 * @return Returns the multiSessionId.
	 */
	public String getMultiSessionId() {
		return multiSessionId;
	}

	/**
	 * @param multiSessionId
	 *            The multiSessionId to set.
	 */
	public void setMultiSessionId(String multiSessionId) {
		this.multiSessionId = multiSessionId;
	}

	/**
	 * @return Returns the outputOctects.
	 */
	public Long getOutputOctects() {
		return outputOctects;
	}

	/**
	 * @param outputOctects
	 *            The outputOctects to set.
	 */
	public void setOutputOctects(Long outputOctects) {
		this.outputOctects = outputOctects;
	}

	/**
	 * @return Returns the outputPackets.
	 */
	public Integer getOutputPackets() {
		return outputPackets;
	}

	/**
	 * @param outputPackets
	 *            The outputPackets to set.
	 */
	public void setOutputPackets(Integer outputPackets) {
		this.outputPackets = outputPackets;
	}

	/**
	 * @return Returns the sessionId.
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            The sessionId to set.
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return Returns the sessionTime.
	 */
	public Integer getSessionTime() {
		return sessionTime;
	}

	/**
	 * @param sessionTime
	 *            The sessionTime to set.
	 */
	public void setSessionTime(Integer sessionTime) {
		this.sessionTime = sessionTime;
	}

	/**
	 * @return Returns the statusType.
	 */
	public Integer getStatusType() {
		return statusType;
	}

	/**
	 * @param statusType
	 *            The statusType to set.
	 */
	public void setStatusType(Integer statusType) {
		this.statusType = statusType;
	}

	/**
	 * @return Returns the terminateCause.
	 */
	public Integer getTerminateCause() {
		return terminateCause;
	}

	/**
	 * @param terminateCause
	 *            The terminateCause to set.
	 */
	public void setTerminateCause(Integer terminateCause) {
		this.terminateCause = terminateCause;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
