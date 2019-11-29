package service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import dao.FeedbackDao;
import dao.ForumDao;
import dao.UserDao;
import dao.impl.FeedbackDaoImpl;
import dao.impl.ForumDaoImpl;
import dao.impl.UserDaoImpl;
import entity.Forum;
import entity.User;
import service.UserService;
import Util.UserUtil;
//编写者:徐越 时间: 2019年7月8日 17时31分
public class NormalUser implements UserService{

	//编写者:徐越 时间: 2019年7月8日 17时31分
	@Override
	public ArrayList<Forum> getUserForum(long id) {//获取某用户的全部帖子
		ForumDao forumDao =new ForumDaoImpl();
		String sql="select * from forum where userId =? order by editDate desc";
		String[] param= {String.valueOf(id)};//为sql语句设置参数
		ArrayList<Forum> allUserForum =forumDao.selectForum(sql, param);//返回该用户ID下的所有帖子信息
		return allUserForum;
	}

	//编写者:徐越 时间: 2019年7月8日 17时47分
	@Override
	public void modifySelfInformation(User user) {//修改自己的信息,按属性可选择更改用户名和密码
		Scanner input = new Scanner(System.in);
		System.out.println("用户当前信息为:");
		user.showUserInformation();//显示用户当前的信息
		UserDao userDao = new UserDaoImpl();
		String choose = "";
		do {//持续为用户提供服务，直到有异常或用户选择退出
			System.out.println("输入1修改用户名,输入2修改密码,按0退出:");
			do {
				choose = input.nextLine().trim();
			}while(choose.length()==0);
			try {//用户输入的非整数则抛出异常
				Integer.parseInt(choose);
				if(Integer.parseInt(choose)%1!=0) {
					throw new Exception();
				}
			}catch(Exception e) {
				System.out.println("输入非法,返回上一级");
				return;
			}
			switch(Integer.parseInt(choose)) {//提供用户选择的服务
			case 1:{//修改用户名
				System.out.println("请输入您的新用户名:(用户名需在20词以内,不能含敏感词,输入0取消操作)");
			    String sql= "select * from ForumUser where userName=?";
			    String Name = "";
			    do{//删除字符串的头尾空白符
			    	Name = input.nextLine().trim();
			    }while(Name.length()==0);
			    if(Name.equals("0")) {//用户选择退出
					System.out.println("用户确认退出");
					return;
				}
			    while(new UserUtil().announceable(Name).size()!=0||Name.length()>20||Name.length()==0) {//调用工具类中的返回所含敏感词list的方法
					if(Name.length()>20||Name.length()==0) {
						System.out.print("用户名不符合要求，请重新输入:");
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
				String[] param = {Name};//为sql语句设置参数
		        ArrayList<User> existUser = userDao.selectUser(sql, param);//返回与输入用户名相同用户名的用户list
		        boolean exist = false;
		        for(int i = 0 ; i<existUser.size() ; i++) {
		        	if(Name.equals(existUser.get(i).getUserName())) {
		        		exist=true;
		        		break;
		        	}
		        }
			    if(existUser.size()==0||exist==false) {//没有与之同名的用户
			    	System.out.println("你决定修改的后用户名为"+Name+",请输入y以确认修改:");
					String confirm="";
					do {//删除字符串的头尾空白符
						confirm = input.nextLine().trim();
					}while(confirm.length()==0);
					if(confirm.equals("y")) {//用户同意修改
						user.setUserName(Name);
				    	sql="update ForumUser set userName = ? where id = ?";
				    	Object[] reparam = {user.getUserName(), user.getId()};//为sql预设参数
				    	userDao.updateUser(sql, reparam);//更新用户信息
					    System.out.println("修改成功!返回选项部分");
					}
					else {
						System.out.println("用户放弃修改!返回选项部分");
					}
			    }
				else 
					System.out.println("新的用户名已存在,返回选项重新选择");
			}break;
			case 2:{//修改密码
				String password="";
				int count = 0 ;
				do {
					System.out.println("请您输入原密码:");
					do {
						password = input.nextLine().trim();//删除字符串的头尾空白符
					}while(password.length()==0);
					if(!new UserUtil().encode(password).equals(user.getPassword())) {//先输入原密码确认为用户本人所进行的操作
						count++;
						if(count==3)
							continue;
						System.out.println("原密码输入错误,您还有"+(3-count)+"次机会重新输入");
					}
				}while(!new UserUtil().encode(password).equals(user.getPassword())&&count<3);//输入错误次数未达到三次且输入密码加密后与存储密码不相同时循环
				if(count==3&&!new UserUtil().encode(password).equals(user.getPassword())) {//密码输入错误达到三次
					System.out.println("原密码输入错误三次,自动返回选项界面");
					continue;
				}
				System.out.println("请您输入修改密码:(密码长度应在20字以内)");
				do {
					do {
						password = input.nextLine().trim();//删除字符串的头尾空白符
					}while(password.length()==0);
					if(new UserUtil().encode(password).equals(user.getPassword())) {//输入密码加密后与原存储密码相同,则不需要进行修改操作
						System.out.println("修改前后密码一致,无需修改,如果继续请输入y,否则将返回选项部分");
						String confirm ="";
						do {
							confirm = input.nextLine().trim();//删除字符串的头尾空白符
						}while(confirm.length()==0);
						if(confirm.equals("y")) {//用户确认继续修改
							System.out.println("请您输入修改密码:(密码长度应在20字以内)");
						}
						else {
							System.out.println("用户放弃修改密码,返回选项部分");
							break;//用户取消操作,跳出循环
						}
					}
				}while(new UserUtil().encode(password).equals(user.getPassword()));//输入密码加密后与原存储密码相同时继续循环，如果用户取消该操作则会在此前跳出循环
				if(new UserUtil().encode(password).equals(user.getPassword()))
					continue;
				System.out.println("请您再次输入修改后的密码以确认:(密码长度应在20字以内)");//输入两次修改后的密码已确认信息
				String confirm = "";
				do {
					confirm = input.nextLine().trim();//删除字符串的头尾空白符
				}while(confirm.length()==0);
				if(!password.equals(confirm))
					System.out.println("两次密码输入不一致,返回上一级");
				else if(password.length()>20||password.length()==0)
					System.out.println("密码设置长度不合理,返回上一级");
				else {
					System.out.println("你选择修改后的密码为"+password+",请输入y以确认修改:");
					do {
						confirm = input.nextLine().trim();//删除字符串的头尾空白符
					}while(confirm.length()==0);
					if(confirm.equals("y")) {
						user.setPassword(password);
				    	String sql="update ForumUser set password = ? where id = ?";
				    	Object[] param = {new UserUtil().encode(password), user.getId()};
				    	userDao.updateUser(sql, param);//更新用户存储的密码
					    System.out.println("修改成功!返回选项部分");
					}
					else {
						System.out.println("用户放弃修改!返回选项部分");
					}
				}
			}break;
			case 0:System.out.println("用户选择退出");return;
				default:System.out.print("用户输入选项不存在,请重新") ;break;
			}
		}while(true);
	}

	//编写者:徐越 时间: 2019年7月8日 18时24分
	@Override
	public User editorOfForum(Forum forum) {//根据帖子找用户,一个用户可编辑多个帖子，但一个帖子只能被一个用户编辑
		UserDao userDao = new UserDaoImpl();
		String sql = "select * from ForumUser where id=?";
		String[] param = {String.valueOf(forum.getUserId())};
		return userDao.selectUser(sql, param).get(0);
 	}

	//编写者:徐越 时间: 2019年7月8日 17时31分
	@Override
	public void accuseUser(User user) {//举报某用户
		Scanner input = new Scanner(System.in);
		UserDao userDao = new UserDaoImpl();
		System.out.println("输入您想举报用户的用户id:");
		String id = "" , sql="select * from forumUser where id =?";
		do {
			id = input.nextLine().trim();//删除字符串的头尾空白符
		}while(id.length()==0);
		try {//对数据非法进行抛出和处理
			Integer.valueOf(id);
			if(Integer.valueOf(id)%1!=0) {
				throw new Exception();
			}
		}catch(Exception e) {
			System.out.println("输入值有误,自动返回");
			return;
		}
		String[] param = {id};
		if(user.getId()==Integer.parseInt(id)) {
			System.out.println("请不要闲着没事举报自己,管理员随时手滑误拉黑就不好玩了");
		}
		else if(userDao.selectUser(sql, param).size()==0) {//返回值为0表示所查用户不存在
			System.out.println("未找到该用户");
		}
		else {
			ArrayList<Forum> forumList =userDao.selectUser(sql, param).get(0).getUserForum(Long.parseLong(id));
			String forumId="";
			if(forumList.size()==0) {
				System.out.println("该用户还未曾发表过任何帖子");
				forumId="0";
			}
			else {
				System.out.println("该用户的帖子如下:");
				new ForumServiceImpl().showUserForum(forumList);//列出该用户的所有帖子信息
				System.out.println("输入举报该用户的帖子序号:(输入0表示不是由于帖子而举报)");
				do {
					forumId = input.nextLine().trim();//删除字符串的头尾空白符
				}while(forumId.length()==0);
				try {//用户输入的序号为非整数
					Integer.parseInt(forumId);
					if(Integer.parseInt(forumId)%1!=0)
						throw new Exception();
				}catch(Exception e) {//进行异常处理
					System.out.println("输入有误,自动返回上一界面");
					return;
				}
				if(Integer.parseInt(forumId)<0||Integer.parseInt(forumId)>forumList.size()) {//输入编号不存在
					System.out.println("输入序号不正确,返回上一级");
					return;
				}
			}
			System.out.println("输入您想举报该用户的理由:");
			String reason ="";
			do {
				reason = input.nextLine().trim();//删除字符串的头尾空白符
			}while(reason.length()==0);
			System.out.println("用户"+user.getUserName()+"因为:\n"+reason+"\n举报用户"+userDao.selectUser(sql, param).get(0).getUserName());
			System.out.println("您是否确认该操作:(输入y以确认)");
			String confirm="";
			do {
				confirm = input.nextLine().trim();//删除字符串的头尾空白符
			}while(confirm.length()==0);
			if(confirm.equals("y")) {
				FeedbackDao feedbackDao = new FeedbackDaoImpl();//连接含举报信息的数据库
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				//预设置为不是因为帖子信息而举报该用户
				sql="insert into Feedback(id,forumId, complainerId, defendantId, reason,accuseTime) values (?,?,?,?,?,?)";
				Object[] feedbackParam= {feedbackDao.getAllFeedback().size()+1,null,user.getId(),id,reason,date};
				if(!forumId.equals("0")) {//若举报的帖子信息存在,则修改参数
					feedbackParam[1]= forumList.get(Integer.parseInt(forumId)-1).getId();
				}
				feedbackDao.updateFeedback(sql, feedbackParam);//更新含举报信息的数据库
				sql="update forumUser set defendantTimes=defendantTimes+1  where id=?";
				Object[] userParam = {id};
				userDao.updateUser(sql, userParam);//更新被举报用户的信息
				System.out.println("您的举报信息已被记录,请等待后续处理");
			}else {
				System.out.println("用户放弃该操作,返回上一界面");
			}
		}
	}
}
