package dao.impl;

import java.util.*;
import java.sql.*;
import dao.BaseDao;
import dao.ForumDao;
import entity.Forum;
//编写者:苗奇 时间:2019年7月6日 14时59分
public class ForumDaoImpl extends BaseDao implements ForumDao{
	
	private Connection conn = null; // 保存数据库连接

	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private ResultSet rs = null; // 用户保存查询结果集
	
	@Override
	public ArrayList<Forum> getAllForum() {//获取所有帖子
		ArrayList<Forum> allForumList = new ArrayList<Forum>();
		try {
			String sql="select id,userId, title, content, editDate,hasReplyForum,Pagename,priority from forum order by priority desc,editDate desc";
			conn = getConn();//建立连接
			pstmt = conn.prepareStatement(sql);//得到prepareStatement的对象
			rs= pstmt.executeQuery();//执行sql语句
			while(rs.next()) {//当还有信息可读取时进行循环
				Forum newForum= new Forum();//创建帖子对象
				newForum.setId(rs.getLong(1));//设定帖子编号
				newForum.setUserId(rs.getLong(2));//设定投稿人id
				newForum.setTitle(rs.getString(3));//设定帖子标题
				newForum.setContent(rs.getString(4));//设定帖子内容
				newForum.setEditDate(rs.getDate(5));//设定编辑日期
				newForum.setHasReplyForum(rs.getBoolean(6));//设定标识符判断是否此贴有回帖
				newForum.setPagename(rs.getString(7));//设置所属版面名称
				newForum.setPriority(rs.getLong(8));;//设定帖子优先级
				allForumList.add(newForum);//添加到list表中
			}
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return allForumList;
	}

	@Override
	public ArrayList<Forum> selectForum(String sql, String[] param) {//选择合适的帖子
		ArrayList<Forum> partForumList = new ArrayList<Forum>();
		try {
			conn = getConn();//建立连接
			pstmt = conn.prepareStatement(sql);//得到prepareStatement的对象
			if(param!=null) {
				for (int i = 0; i < param.length; i++) {
					pstmt.setString(i + 1, param[i]); // 为预编译sql设置参数
				}
			}
			rs= pstmt.executeQuery();//执行sql语句
			while(rs.next()) {//当还有信息可读取时进行循环
				Forum newForum= new Forum();//创建帖子对象
				newForum.setId(rs.getLong(1));//设定帖子编号
				newForum.setUserId(rs.getLong(2));//设定投稿人id
				newForum.setTitle(rs.getString(3));//设定帖子标题
				newForum.setContent(rs.getString(4));//设定帖子内容
				newForum.setEditDate(rs.getDate(5));//设定编辑日期
				newForum.setHasReplyForum(rs.getBoolean(6));//设定标识符判断是否此贴有回帖
				newForum.setPagename(rs.getString(7));//设置所属版面名称
				newForum.setPriority(rs.getLong(8));;//设定帖子优先级
				partForumList.add(newForum);//添加到list表中
			}
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return partForumList;
	}

	@Override
	public int updateForum(String sql, Object[] param) {//获取更新数量
		return super.executeSQL(sql, param);
	}

}
