package entity;
//编写者:苗奇 时间:2019年7月6日 13时18分
public class Page {//版面类
	
	private String name;//版面名称
	
	private long ForumNumber;//所含帖子数量

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getForumNumber() {
		return ForumNumber;
	}

	public void setForumNumber(long forumNumber) {
		ForumNumber = forumNumber;
	}

}
