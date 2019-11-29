package dao;

import java.util.ArrayList;

import entity.User;
//编写者:苗奇 时间:2019年7月6日 11时44分
public interface UserDao{//对用户数据库的交互接口
	
	public abstract ArrayList<User> getAllUser();//列出所有用户
	
	public abstract ArrayList<User> selectUser(String sql, String[] param);//根据用户信息找到满足条件的用户

	public abstract int updateUser(String sql, Object[] param);//更新用户的信息

}
