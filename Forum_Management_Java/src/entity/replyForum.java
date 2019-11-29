package entity;
//编写者:苗奇 时间:2019年7月6日 13时21分
public class replyForum {//回帖信息类
	
	private long originForumId;//原帖ID
	
	private long replyForumId;//回帖ID
	
	public long getOriginForumId() {
		return originForumId;
	}
	public void setOriginForumId(long originForumId) {
		this.originForumId = originForumId;
	}
	public long getReplyForumId() {
		return replyForumId;
	}
	public void setReplyForumId(long repleyForumId) {
		this.replyForumId = repleyForumId;
	}
	
}
