package dao.impl;

import java.util.*;
import java.sql.*;
import dao.UserDao;
import dao.BaseDao;
import entity.User;
import role.Role;
//编写者:苗奇 时间:2019年7月6日 15时39分
public class UserDaoImpl extends BaseDao implements UserDao {
	
	private Connection conn = null; // 保存数据库连接

	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private ResultSet rs = null; // 用户保存查询结果集

	@Override
	public ArrayList<User> getAllUser() {
		ArrayList<User> allUserList = new ArrayList<User>();
		try {
			String sql = "select id,userName,password,defendantTimes,editTimes, black, description from ForumUser";
			conn = getConn(); // 得到数据库连接
			pstmt = conn.prepareStatement(sql); // 得到PreparedStatement对象
			rs = pstmt.executeQuery(); // 执行SQL语句
			while(rs.next()) {
				User newUser = new User();//设置用户对象
				Role role =new Role();//设置用户内的role对象
				newUser.setId(rs.getLong(1));//设置用户ID
				newUser.setUserName(rs.getString(2));//设置用户名
				newUser.setPassword(rs.getString(3));//设置用户密码
				newUser.setDefendantTimes(rs.getInt(4));//设置用户的被举报次数
				newUser.setEditTimes(rs.getInt(5));//设置用户的发帖量
				newUser.setBlack(rs.getBoolean(6));//判断用户是否被拉黑
				role.setDescription(rs.getString(7));//设置role内的角色描述(用户、管理员、游客)
				newUser.setRole(role);//设置用户内role参数
				allUserList.add(newUser);
			}
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return allUserList;
	}

	@Override
	public ArrayList<User> selectUser(String sql, String[] param) {
		ArrayList<User> partUserList = new ArrayList<User>();
		try {
			conn = getConn(); // 得到数据库连接
			pstmt = conn.prepareStatement(sql); // 得到PreparedStatement对象
			if(param!=null) {
				for(int i = 0; i<param.length ; i++) {
					pstmt.setString(i+1, param[i]);// 为预编译sql设置参数
				}
			}
			rs = pstmt.executeQuery(); // 执行SQL语句
			while(rs.next()) {
				User newUser = new User();//设置用户对象
				Role role =new Role();//设置用户内的role对象
				newUser.setId(rs.getLong(1));//设置用户ID
				newUser.setUserName(rs.getString(2));//设置用户名
				newUser.setPassword(rs.getString(3));//设置用户密码
				newUser.setDefendantTimes(rs.getInt(4));//设置用户的被举报次数
				newUser.setEditTimes(rs.getInt(5));//设置用户的发帖量
				newUser.setBlack(rs.getBoolean(6));//判断用户是否被拉黑
				role.setDescription(rs.getString(7));//设置role内的角色描述(用户、管理员、游客)
				newUser.setRole(role);//设置用户内role参数
				partUserList.add(newUser);
			}
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return partUserList;
	}

	@Override
	public int updateUser(String sql, Object[] param) {//返回查询次数判断所查用户是否存在
		return super.executeSQL(sql, param);
	}
}
