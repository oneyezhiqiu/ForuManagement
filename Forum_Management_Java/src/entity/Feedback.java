package entity;

import java.util.Date;
//编写者:苗奇 时间:2019年7月6日 13时05分
public class Feedback {//举报信息类
	
	private long id;//举报编号
	
	private long forumId;//相关的帖子编号
	
	private long complainerId;//举报人id
	
	private long defendantId;//被举报者id
	
	private String reason; //举报理由
	
	private Date accuseTime;//举报时间

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getForumId() {
		return forumId;
	}

	public void setForumId(long forumId) {
		this.forumId = forumId;
	}

	public long getComplainerId() {
		return complainerId;
	}

	public void setComplainerId(long complainerId) {
		this.complainerId = complainerId;
	}

	public long getDefendantId() {
		return defendantId;
	}

	public void setDefendantId(long defendantId) {
		this.defendantId = defendantId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getAccuseTime() {
		return accuseTime;
	}

	public void setAccuseTime(Date accuseTime) {
		this.accuseTime = accuseTime;
	}
	
}
