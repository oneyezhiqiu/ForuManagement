package entity;

import java.util.*;
//编写者:苗奇 时间:2019年7月6日 13时12分
public class Forum {//帖子类
	
	private long id;//帖子编号
	
	private long userId; //投稿人id
	
	private String title; //帖子标题
	
	private String content; //帖子的内容
	
	private Date editDate;//编辑日期
	
	private boolean hasReplyForum;//判断是否有回帖
	
	private String Pagename;//所属版面名称;
	
	private long priority;//优先级，用于置顶帖子

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public boolean isHasReplyForum() {
		return hasReplyForum;
	}

	public void setHasReplyForum(boolean hasReplyForum) {
		this.hasReplyForum = hasReplyForum;
	}

	public String getPagename() {
		return Pagename;
	}

	public void setPagename(String pagename) {
		Pagename = pagename;
	}
	
}
