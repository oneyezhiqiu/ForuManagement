package dao.impl;

import dao.replyForumDao;
import entity.replyForum;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.BaseDao;
//编写者:苗奇 时间:2019年7月6日 15时23分
public class replyForumDaoImpl extends BaseDao implements replyForumDao{

	private Connection conn = null; // 保存数据库连接

	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private ResultSet rs = null; // 用户保存查询结果集
	
	@Override
	public ArrayList<replyForum> getAllreply() {//获取所有的回帖信息
		ArrayList<replyForum> allReply = new ArrayList<replyForum>();
		try {
			conn = getConn();
			String sql="select originForumId, replyForumId from replyForum";
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				replyForum rf = new replyForum();
				rf.setOriginForumId(rs.getLong(1));//设置原帖ID
				rf.setReplyForumId(rs.getLong(2));//设置回帖ID
				allReply.add(rf);
			}
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return allReply;
	}

	@Override
	public ArrayList<replyForum> selectReply(String sql, String[] param) {
		ArrayList<replyForum> partReply = new ArrayList<replyForum>();
		try {
			conn = getConn();//建立连接
			pstmt = conn.prepareStatement(sql);//得到prepareStatement的对象
			if(param!=null) {
				for (int i = 0; i < param.length; i++) {
					pstmt.setString(i + 1, param[i]); // 为预编译sql设置参数
				}
			}
			rs= pstmt.executeQuery();//执行sql语句
			while(rs.next()) {
				replyForum rf = new replyForum();
				rf.setOriginForumId(rs.getLong(1));//设置原帖ID
				rf.setReplyForumId(rs.getLong(2));//设置回帖ID
				partReply.add(rf);
			}
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return partReply;
	}

	@Override
	public int updateReply(String sql, Object[] param) {//获取查询次数
		return super.executeSQL(sql, param);
	}

}
