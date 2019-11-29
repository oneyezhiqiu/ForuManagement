package dao;

import java.util.*;
import entity.Forum;
//编写者:苗奇 时间:2019年7月6日 10时40分
public interface ForumDao {//帖子的数据库接口
	
	public abstract ArrayList<Forum> getAllForum();//列出所有帖子
	
	public abstract ArrayList<Forum> selectForum(String sql, String[] param);//根据帖子信息找到满足条件的帖子

	public abstract int updateForum(String sql, Object[] param);//更新帖子的信息

}
