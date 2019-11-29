package dao;

import java.util.*;
import entity.Feedback;
//编写者:苗奇 时间:2019年7月6日 10时32分
public interface FeedbackDao {//对举报反馈库中的操作
	
	public abstract ArrayList<Feedback> getAllFeedback();//列出所有用户的举报信息
	
	public abstract ArrayList<Feedback> selectFeedback(String sql, String[] param);//找到某些特定的举报信息

	public abstract int updateFeedback(String sql, Object[] param);//更新用户间的举报信息
}
