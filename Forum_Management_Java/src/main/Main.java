package main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import Util.UserUtil;
import dao.replyForumDao;
import dao.impl.ForumDaoImpl;
import dao.impl.PageDaoImpl;
import dao.impl.replyForumDaoImpl;
import entity.Forum;
import entity.Page;
import entity.User;
import entity.replyForum;
import service.ForumService;
import service.PageService;
import service.impl.ForumServiceImpl;
import service.impl.Manager;
import service.impl.NormalUser;
import service.impl.PageServiceImpl;
import service.impl.UserFactoryImpl;

public class Main {
	public static void main(String[] args) {
		Main.startForum();
	}
	//编写者:林可葳 时间:2019年7月9日 14时23分
	private static void startForum() {
		Scanner input = new Scanner(System.in);
		String choose = "";
		System.out.println("————————————————————欢迎回到EVOL论坛！！————————————————————");
		do {
			User user = new User();
			System.out.println("请选择是否登陆(输入1为游客,2为选择登陆,3选择注册,输入0表示退出):");
			do {
				choose = input.nextLine().trim();
			}while(choose.length()==0);//输入空行则重复输入
			try{//输入非法
				Integer.parseInt(choose);
				if(Integer.parseInt(choose)%1!=0)
					throw new Exception();
			}catch(Exception e) {//抛出异常
				System.out.print("用户输入非法,");
				continue;
			}
			switch(Integer.parseInt(choose)) {
			case 1:Main.doVisitor(user); break;//进入游客服务界面
			case 2:{//进入登陆界面
				user = user.login();//用户登陆
				if(user == null)//用户登录失败
					break;
				else if(user.getRole().getDescription().equals("用户")) {//登陆身份为用户
					user.getRole().setNormalUser(new NormalUser());
					Main.doNormalUser(user);
				}
				else {//登陆身份为管理员
					user.getRole().setNormalUser(new NormalUser());
					user.getRole().setManager(new Manager());
					Main.doManager(user);
				}
			}break;
			case 3:Main.logon();break;//注册界面
			case 0:System.out.print("用户退出该论坛,感谢您的使用,期待下一次见面");return;
				default:System.out.println("输入错误");break;
			}
		}while(!choose.equals("0"));
	}

	//编写者:林可葳 时间:2019年7月9日 15时03分
	private static void logon() {//注册过程
		Scanner input = new Scanner(System.in);
		System.out.println("选择注册何种用户类型:(输入1表示用户,输入0返回):");
		String choose ="";
		do {
			choose = input.nextLine().trim();
		}while(choose.length()==0);//输入空行则重复输入
		try{
			Integer.parseInt(choose);
			if(Integer.parseInt(choose)%1!=0)
				throw new Exception();
		}catch(Exception e) {//输入非法则抛出异常
			System.out.println("用户输入非法,自动返回主界面");
			return;
		}
		switch(Integer.parseInt(choose)) {
		case 0: System.out.println("用户选择退出该操作,返回上一级");return;//
		case 1:new UserFactoryImpl().createUser("用户");break;//
			default:
				System.out.print("用户输入错误,请重新");
				Main.logon();
				break;
		}
	}

	//编写者:林可葳 时间:2019年7月9日 15时35分
	private static void doVisitor(User user) {//执行游客模式下的操作
		Scanner input = new Scanner(System.in);
		do {
			ArrayList<Page> allPage =  new PageDaoImpl().getAllPage();
			PageService pageService = new PageServiceImpl();
			if(allPage.size()==0) {
				System.out.println("当前论坛内无任何版块,还是等管理员创建新版块后再过来看吧");
				return;
			}
			System.out.println("选择要查看哪个版面内的帖子(输入0退出)");
			pageService.showPage(allPage);
			String choose ="";
			do {
				choose = input.nextLine();
			}while(choose.length()==0);
			try{
				Integer.parseInt(choose);
				if(Integer.parseInt(choose)%1!=0)
					throw new Exception();
			}catch(Exception e) {
				System.out.print("用户输入非法,");
				continue;
			}
			if(Integer.parseInt(choose)==0) {
				System.out.println("用户选择退出,返回登陆界面");
				return;
			}
			else if(Integer.parseInt(choose)<0||Integer.parseInt(choose)>allPage.size()) {
				System.out.print("版面信息不存在,请重新");
			}
			else {
				ArrayList<Forum> allforum= pageService.getAllForum(allPage.get(Integer.parseInt(choose)-1).getName());
				ForumService forumService = new ForumServiceImpl();
				if(allforum.size()==0) {
					System.out.println("已经到达知识的边缘了,没有任何帖子可以看了,自动回到版面选择了");
					continue;
				}
				System.out.print("当前帖子如下:\n");
				allforum=forumService.showPageForum(allforum);
				do {
					if(user.getRole().getDescription().equals("游客")) {
						System.out.println("选择进行的操作:1.查看某个帖子及其回帖内容     2.查看发表某个帖子的用户信息    3.返回版块选择   0.返回登陆:");
					}
					else {
						System.out.println("选择进行的操作:1.查看某个帖子及其回帖内容     2.查看发表某个帖子的用户信息    3.返回版块选择  4.回帖   0.返回服务界面:");
					}
					do {
						choose = input.nextLine();
					}while(choose.length()==0);
					try{
						Integer.parseInt(choose);
						if(Integer.parseInt(choose)%1!=0)
							throw new Exception();
					}catch(Exception e) {
						System.out.println("用户输入非法,当前帖子如下:");
						allforum=forumService.showPageForum(allforum);
						choose="";
						continue;
					}
					if(Integer.parseInt(choose)==3){
						System.out.println("正在返回版面选择界面"); 
						break;
					}
					else if(Integer.parseInt(choose)==0&&user.getRole().getDescription().equals("游客")){
						System.out.println("用户选择退出,返回登陆界面");
						return;
					}
					else if(Integer.parseInt(choose)==0&&!user.getRole().getDescription().equals("游客")) {
						System.out.println("用户选择退出,返回服务界面");
						return;
					}
					else if(Integer.parseInt(choose)!=1&&(Integer.parseInt(choose)!=2)&&(Integer.parseInt(choose)!=4)){
						System.out.println("用户输入错误,当前帖子如下:");
						allforum=forumService.showPageForum(allforum);
					}
					else if(Integer.parseInt(choose)==4&& user.getRole().getDescription().equals("游客")) {
						System.out.println("用户输入错误,当前帖子如下:");
						allforum=forumService.showPageForum(allforum);
					}
					else {
						System.out.println("输入帖子前序号");
						String id = "";
						do {
							id= input.nextLine();
						}while(id.length()==0);
						try{
							Integer.parseInt(id);
							if(Integer.parseInt(id)%1!=0)
								throw new Exception();
						}catch(Exception e) {
							System.out.print("用户输入非法,请重新");
							continue;
						}
						if(Integer.parseInt(id)<=0||Integer.parseInt(id)>allforum.size()) {
							System.out.print("用户帖子信息不存在,请重新");
							continue;
						}
						else {
							if(Integer.parseInt(choose)==1){
								System.out.println("原帖包括其回帖信息如下所示:");
								allforum=new ForumServiceImpl().ReadForum(allforum.get(Integer.parseInt(id)-1));
							}
							else if(Integer.parseInt(choose)==4&& !user.getRole().getDescription().equals("游客")) {
								user.replyForum(user, allforum.get(Integer.parseInt(id)-1));
								allforum= pageService.getAllForum(allforum.get(Integer.parseInt(id)-1).getPagename());
								System.out.println("当前帖子如下:");
								allforum=forumService.showPageForum(allforum);
							}
							else {
								User temp = new NormalUser().editorOfForum(allforum.get(Integer.parseInt(id)-1));
								temp.showUserInformation();
								System.out.println("该用户的帖子如下:");
								allforum = new NormalUser().getUserForum(temp.getId());
								forumService.showUserForum(allforum);
							}
						}
					}
				}while(choose.length()==0||Integer.parseInt(choose)!=3);
			}
		}while(true);
	}

	//编写者:林可葳 时间:2019年7月9日 16时34分
	private static void doNormalUser(User newUser) {//执行普通用户的操作
		System.out.println("请选择进行的操作: 1.发帖\t2.查看当前帖子并可选择回帖\t3.修改自己信息\n4.举报某用户\t5.删帖\t6.修改自己的帖子内容\t7.注销并返回登陆界面");
		Scanner input = new Scanner(System.in);
		String choose ="";
		do {
			choose = input.nextLine().trim();
		}while(choose.length()==0);
		try{
			Integer.parseInt(choose);
			if(Integer.parseInt(choose)%1!=0)
				throw new Exception();
		}catch(Exception e) {
			System.out.println("用户输入非法");
			Main.doNormalUser(newUser);
			return;
		}
		switch(Integer.parseInt(choose)) {
		case 1:{//发帖
			ArrayList<Page> allPage =  new PageDaoImpl().getAllPage();
			PageService pageService = new PageServiceImpl();
			pageService.showPage(allPage);
			String page = "" ;
			do {
				System.out.println("选择帖子要发表在哪个版面,输入版面序号(输入0返回操作界面)");
				do {
					page = input.nextLine();
				}while(page.length()==0);
				try{
					Integer.parseInt(page);
					if(Integer.parseInt(page)%1!=0)
						throw new Exception();
				}catch(Exception e) {
					System.out.print("用户输入非法,请重新");
					continue;
				}
				if(Integer.parseInt(page)==0) {
					System.out.println("用户选择退出,返回返回操作界面");
					break;
				}
				else if(Integer.parseInt(page)<0||Integer.parseInt(page)>allPage.size()) {
					System.out.print("版面信息不存在,请重新");
				}
				else
					break;
			}while(true);//确认发布版面
			if(Integer.parseInt(page)==0) {
				Main.doNormalUser(newUser);//重复用户界面
			}
			else {
				newUser.postForum(newUser, allPage.get(Integer.parseInt(page)-1).getName());//发帖方法
				Main.doNormalUser(newUser);//重复用户界面
			}
		}
		break;
		case 2:{
			Main.doVisitor(newUser);//该处和游客登陆相同
			Main.doNormalUser(newUser);//重复用户界面
			break;
		}
		case 3:
			newUser.modifySelfInformation(newUser);//修改个人信息方法
			Main.doNormalUser(newUser);//重复用户界面
			break;
		case 4:
			newUser.accuseUser(newUser);//举报用户
			Main.doNormalUser(newUser);//重复用户界面
			break;
		case 5:
			newUser.deleteUserForum(newUser);//删帖
			Main.doNormalUser(newUser);//重复用户界面
			break;
		case 6:
			newUser.modifyForum(newUser);//修改帖子内容
			Main.doNormalUser(newUser);//重复用户界面
			break;
		case 7:
			System.out.println("感谢您的使用,下次再见");
			return;
			default:{
				System.out.println("用户输入选项不正确,请重新进行选择");
				Main.doNormalUser(newUser);//重复用户界面
				break;
			}
		}
	}
	
	//编写者:林可葳 时间:2019年7月9日 16时56分
	private static void doManager(User newUser)  {//执行管理员操作
		System.out.println("请选择进行的操作:\n1.修改版面\t2.添加版面\t3.删除版面\t4.查看当前帖子并可选择回帖\n5.删帖\t\t6.发帖   \t7.拉黑用户\t8.修改自己信息\n9.修改自己的帖子内容\t10.添加屏蔽词\t11.查看并删除屏蔽词\n12.设置某用户为管理员\t13.置顶某帖\t14.注销并返回登陆界面");
		Scanner input = new Scanner(System.in);
		String choose ="";
		do {
			choose = input.nextLine();
		}while(choose.length()==0);
		try{
			Integer.parseInt(choose);
			if(Integer.parseInt(choose)%1!=0)
				throw new Exception();
		}catch(Exception e) {
			System.out.println("用户输入非法");
			Main.doNormalUser(newUser);
			return;
		}
		switch(Integer.parseInt(choose)) {
		case 1:{//修改版面
			ArrayList<Page> allPage =  new PageDaoImpl().getAllPage();
			PageService pageService = new PageServiceImpl();
			if(allPage.size()==0) {
				System.out.println("当前无任何版面,请先创建版面");
				return;
			}
			pageService.showPage(allPage);
			String page = "" ;
			do {
				System.out.println("选择要修改哪个版面,输入版面序号(输入0返回操作界面)");
				do {
					page = input.nextLine();
				}while(page.length()==0);
				try{
					Integer.parseInt(page);
					if(Integer.parseInt(page)%1!=0)
						throw new Exception();
				}catch(Exception e) {
					System.out.print("用户输入非法,请重新");
				}
				if(Integer.parseInt(page)==0) {
					System.out.println("用户选择退出,返回操作界面");
					break;
				}
				else if(Integer.parseInt(page)<0||Integer.parseInt(page)>allPage.size()) {
					System.out.print("版面信息不存在,请重新");
				}
				else
					break;
			}while(true);//确认发布版面
			if(Integer.parseInt(page)==0) {
				Main.doManager(newUser);
			}
			else{
				newUser.modifyPage(allPage.get(Integer.parseInt(page)-1).getName());//修改版面
				Main.doManager(newUser);//重复管理员界面
			}
		}break;
		case 2:
			newUser.addPage();//添加版面
			Main.doManager(newUser);//重复管理员界面
			break;
		case 3:
			newUser.deletePage();//删除版面
			Main.doManager(newUser);//重复管理员界面
			break;
		case 4:{
			Main.doVisitor(newUser);//该处和游客登陆相同
			Main.doManager(newUser);//重复管理员界面
			break;
		}
		case 5:
			newUser.deleteUserForum(newUser);
			Main.doManager(newUser);//重复管理员界面
			break;
		case 6:{//发帖
			ArrayList<Page> allPage =  new PageDaoImpl().getAllPage();
			PageService pageService = new PageServiceImpl();
			pageService.showPage(allPage);
			String page = "" ;
			do {
				System.out.println("选择帖子要发表在哪个版面,输入版面序号(输入0返回操作界面)");
				do {
					page = input.nextLine();
				}while(page.length()==0);
				try{
					Integer.parseInt(page);
					if(Integer.parseInt(page)%1!=0)
						throw new Exception();
				}catch(Exception e) {
					System.out.print("用户输入非法,请重新");
				}
				if(Integer.parseInt(page)==0) {
					System.out.println("用户选择退出,返回登陆界面");
					Main.doNormalUser(newUser);
				}
				else if(Integer.parseInt(page)<0||Integer.parseInt(page)>allPage.size()) {
					System.out.print("版面信息不存在,请重新");
				}
				else
					break;
			}while(true);//确认发布版面
			newUser.postForum(newUser, allPage.get(Integer.parseInt(page)-1).getName());//发帖方法
			Main.doManager(newUser);//重复管理员界面
			break;
		}
		case 7:
			newUser.blackUser();
			Main.doManager(newUser);//重复管理员界面
			break;
		case 8:
			newUser.modifySelfInformation(newUser);//修改个人信息方法
			Main.doManager(newUser);//重复管理员界面
			break;
		case 9:
			newUser.modifyForum(newUser);//修改帖子内容
			Main.doManager(newUser);//重复管理员界面
			break;
		case 10:
			new UserUtil().addSensitiveWords();//添加屏蔽词
			Main.doManager(newUser);//重复管理员界面
			break;
		case 11:
			new UserUtil().delSensitiveWords();//删除屏蔽词
			Main.doManager(newUser);//重复管理员界面
			break;
		case 12:
			newUser.UserLevelUp();
			Main.doManager(newUser);
			break;
		case 13:
			ArrayList<Page> allPage =  new PageDaoImpl().getAllPage();
			PageService pageService = new PageServiceImpl();
			if(allPage.size()==0) {
				System.out.println("当前无任何版面,请先创建版面");
				return;
			}
			pageService.showPage(allPage);
			String page = "" ;
			do {
				System.out.println("选择要置顶哪个版面的帖子,输入版面序号(输入0返回操作界面)");
				do {
					page = input.nextLine();
				}while(page.length()==0);
				try{
					Integer.parseInt(page);
					if(Integer.parseInt(page)%1!=0)
						throw new Exception();
				}catch(Exception e) {
					System.out.print("用户输入非法,请重新");
				}
				if(Integer.parseInt(page)==0) {
					System.out.println("用户选择退出,返回操作界面");
					break;
				}
				else if(Integer.parseInt(page)<0||Integer.parseInt(page)>allPage.size()) {
					System.out.print("版面信息不存在,请重新");
				}
				else
					break;
			}while(true);//确认发布版面
			if(Integer.parseInt(page)==0) {
				Main.doManager(newUser);
			}
			else {
				newUser.Top(allPage.get(Integer.parseInt(page)-1));
				Main.doManager(newUser);	
			}break;
		case 14:
			System.out.println("感谢您的使用,下次再见");
			return;
			default:{
				System.out.println("用户输入选项不存在");
				Main.doManager(newUser);;//重复用户界面
				break;
			}
		}
	}

}
