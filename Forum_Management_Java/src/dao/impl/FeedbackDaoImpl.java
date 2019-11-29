package dao.impl;

import java.util.*;
import java.sql.*;
import dao.BaseDao;
import dao.FeedbackDao;
import entity.Feedback;
//编写者:苗奇 时间:2019年7月6日 14时20分
public class FeedbackDaoImpl extends BaseDao implements FeedbackDao {
	
	private Connection conn = null; // 保存数据库连接

	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private ResultSet rs = null; // 用户保存查询结果集
	@Override
	public ArrayList<Feedback> getAllFeedback() {//列出所有用户的举报信息
		ArrayList<Feedback> allFeedbackList = new ArrayList<Feedback>();//初始化举报信息列表
		try {
			conn = getConn();//建立数据库连接
			//写sql语句读出所有举报信息中的所有属性
			String sql = "select id,forumId, complainerId, defendantId, reason,accuseTime from Feedback";
			pstmt =conn.prepareStatement(sql);//得到PrepareStatement对象
			rs=pstmt.executeQuery();//执行sql语句
			while(rs.next()) {//当还有信息可读取时进行循环
				Feedback feedback = new Feedback();
				feedback.setId(rs.getLong(1));//设定举报信息编号
				feedback.setForumId(rs.getLong(2));//设定举报帖子的编号
				feedback.setComplainerId(rs.getLong(3));//设定举报者id
				feedback.setDefendantId(rs.getLong(4));//设定被举报者id
				feedback.setReason(rs.getString(5));//设置举报原因
				feedback.setAccuseTime(rs.getDate(6));//设置举报时间
				allFeedbackList.add(feedback);//添加信息至列表中
			}
		}catch(SQLException e) {//处理SQLException异常
			e.printStackTrace();
		}catch(ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		}
		finally {//无论如何都需要关闭连接
			super.closeAll(conn, pstmt, rs);
		}
		return allFeedbackList;
	}

	@Override
	public ArrayList<Feedback> selectFeedback(String sql, String[] param) {//找到某些特定的举报信息
		ArrayList<Feedback> partFeedbackList = new ArrayList<Feedback>();//初始化举报信息列表
		try {
			conn = getConn();//建立数据库连接
			pstmt =conn.prepareStatement(sql);//得到PrepareStatement对象
			if(param!=null) {
				for(int i = 0 ; i<param.length ; i++) {
					pstmt.setString(i+1, param[i]);//为预编译sql语句设置参数
				}
			}
			rs=pstmt.executeQuery();//执行sql语句
			while(rs.next()) {//当还有信息可读取时进行循环
				Feedback feedback = new Feedback();
				feedback.setId(rs.getLong(1));//设定举报信息编号
				feedback.setForumId(rs.getLong(2));//设定举报帖子的编号
				feedback.setComplainerId(rs.getLong(3));//设定举报者id
				feedback.setDefendantId(rs.getLong(4));//设定被举报者id
				feedback.setReason(rs.getString(5));//设置举报原因
				feedback.setAccuseTime(rs.getDate(6));//设置举报时间
				partFeedbackList.add(feedback);//添加信息至列表中
			}
		}catch(SQLException e) {//处理SQLException异常
			e.printStackTrace();
		}catch(ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		}
		finally {//无论如何都需要关闭连接
			super.closeAll(conn, pstmt, rs);
		}
		return partFeedbackList;
	}

	@Override
	public int updateFeedback(String sql, Object[] param) {//更新举报信息
		return super.executeSQL(sql, param);
	}
	
}
