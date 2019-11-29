package dao.impl;

import java.util.*;
import java.sql.*;
import dao.BaseDao;
import dao.PageDao;
import entity.Page;
//编写者:苗奇 时间:2019年7月6日 15时10分
public class PageDaoImpl extends BaseDao implements PageDao{
	
	private Connection conn = null; // 保存数据库连接

	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private ResultSet rs = null; // 用户保存查询结果集

	@Override
	public ArrayList<Page> getAllPage() {//返回所有的版面信息
		ArrayList<Page> allPageList = new ArrayList<Page>();
		try {
			String sql = "select name,ForumNumber from page ";
			conn = getConn(); // 得到数据库连接
			pstmt = conn.prepareStatement(sql); // 得到PreparedStatement对象
			rs = pstmt.executeQuery(); // 执行SQL语句
			
			while (rs.next()) {
				Page page= new Page();
				page.setName(rs.getString(1));//设置版面名称
				page.setForumNumber(rs.getLong(2));//设置版面内所含帖子数
				allPageList.add(page);
			}
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return allPageList;
	}

	@Override
	public ArrayList<Page> selectPage(String sql, String[] param) {//选择部分版面
		ArrayList<Page> partPageList = new ArrayList<Page>();
		try {
			conn = getConn(); // 得到数据库连接
			pstmt = conn.prepareStatement(sql); // 得到PreparedStatement对象
			if(param!=null) {
				for(int i = 0 ; i<param.length; i++) {
					pstmt.setString(i+1, param[i]);//为预编译sql设置参数
				}
			}
			rs = pstmt.executeQuery(); // 执行SQL语句
			while (rs.next()) {
				Page page= new Page();
				page.setName(rs.getString(1));//设置版面名称
				page.setForumNumber(rs.getLong(2));//设置版面内所含帖子数
				partPageList.add(page);
			}
		} catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return partPageList;
	}

	@Override
	public int updatePage(String sql, Object[] param) {
		return super.executeSQL(sql, param);
	}

}
