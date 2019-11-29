package dao;

import java.util.ArrayList;
import entity.replyForum;
//编写者:苗奇 时间:2019年7月6日 11时28分
public interface replyForumDao {//对回帖操作的数据库接口
	public abstract ArrayList<replyForum> getAllreply();//列出所有回帖信息
	
	public abstract ArrayList<replyForum> selectReply(String sql, String[] param);//

	public abstract int updateReply(String sql, Object[] param);//更新回帖信息
}
