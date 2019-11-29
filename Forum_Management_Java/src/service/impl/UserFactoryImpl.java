package service.impl;

import java.util.ArrayList;
import java.util.Scanner;
import Util.UserUtil;
import service.UserFactory;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import entity.User;
//编写者:张越 时间: 2019年7月5日 21时27分
public class UserFactoryImpl implements UserFactory{
	public void createUser(String identity) {//用户注册
		Scanner input = new Scanner(System.in);
		UserDao userDao= new UserDaoImpl();//初始化userDao对象,进行数据库操作
		System.out.println("您已进入注册界面,请输入您的用户名:(用户名长度应在20字以内,按0退出)");
		User newUser = null;
	    String sql= "select * from ForumUser where userName=?";
		do {
			String Name = "";
			do {
				Name = input.nextLine().trim();//删除字符串的头尾空白符
			}while(Name.length()==0);
			if(Name.equals("0")) {//用户选择退出
				System.out.println("用户确认退出");
				return;
			}
			while(new UserUtil().announceable(Name).size()!=0||Name.length()>20||Name.length()==0) {//调用工具类中的返回所含敏感词list的方法
				if(Name.length()>20||Name.length()==0) {
					System.out.println("用户名长度不符合要求，重新输入:");
				}
				else
					System.out.println("当前用户名中含敏感词,请重新输入:");
	    		do {
					Name = input.nextLine().trim();//删除字符串的头尾空白符
				}while(Name.length()==0);
	    		if(Name.equals("0")) {//用户选择退出
					System.out.println("用户确认退出");
					return;
				}
	    	}
		    String[] param = {Name};//预设置sql语句
		    ArrayList<User> existUser = userDao.selectUser(sql, param);//返回用户名为输入Name的用户信息
		    boolean exist = false;
		    long totalSize=userDao.getAllUser().size();//返回当前注册成功的所有用户数量
		    for(int i = 0 ; i < existUser.size() ; i++ ) {
		    	if(Name.equals(existUser.get(i).getUserName())) {
		    		exist=true;
		    		break;
		    	}
		    }
		    if(existUser.size()==0||exist == false) {//用户名未被人注册过
		    	newUser=new User();//创建新用户对象
		    	newUser.setUserName(Name);//设置用户名
		    	newUser.setId(totalSize+1);//设置编号为当前用户数+1,多人同时修改数据库时编号重复,可优化.在util内中编写生成ID的方法
		    }
		    else 
		    	System.out.println("用户名重复，请重新输入:");
		}while(newUser==null);
		String password=" " , confirm=" "; 
		do {//注册密码
			System.out.println("请您输入密码:(长度应在20字以内)");
			do {
				password = input.nextLine().trim();//删除字符串的头尾空白符
			}while(password.length()==0);
			System.out.println("请您再次输入密码以确认：");//注册密码需输入两遍以确认信息
			do {
		    	confirm = input.nextLine().trim();//删除字符串的头尾空白符
			}while(confirm.length()==0);
			if(!password.equals(confirm))
				System.out.print("两次密码输入不一致,");
			else if(password.length()>20||password.length()==0)
				System.out.print("密码输入长度不合理");
		}while(!password.equals(confirm)||password.length()>20||password.length()==0);
		newUser.setPassword(password);
		System.out.println("你所注册的用户名为"+newUser.getUserName()+",设定密码为:"+newUser.getPassword()+",请输入y以确认注册:");
		do {
			confirm = input.nextLine().trim();//删除字符串的头尾空白符
		}while(confirm.length()==0);
		if(confirm.equals("y")) {//用户确认注册账号
			//用sql在数据库内添加用户ID，用户名，用户密码，被举报次数，发帖量，是否被拉黑，用户属性描述。
			sql = "insert into ForumUser(id,userName,password,defendantTimes,editTimes, black, description) values (?,?,?,?,?,?,?)";
		    Object[] setParam = {newUser.getId(),newUser.getUserName(),new UserUtil().encode(newUser.getPassword()),0,0,false,identity};//预设置sql语句
		    userDao.updateUser(sql, setParam);//更新数据库内信息
		    System.out.println("注册成功!返回主界面");
		}
		else {
			System.out.println("用户放弃注册!返回主界面");
		}
	}
}
