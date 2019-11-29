package service.impl;

import java.util.ArrayList;
import java.util.Scanner;
import dao.FeedbackDao;
import dao.ForumDao;
import dao.UserDao;
import dao.impl.FeedbackDaoImpl;
import dao.impl.ForumDaoImpl;
import dao.impl.UserDaoImpl;
import entity.Feedback;
import entity.Forum;
import entity.Page;
import entity.User;
import service.ForumService;
import service.ManagerService;
import service.PageService;

//编写者:张越 时间: 2019年7月8日 14时13分
public class Manager extends NormalUser implements ManagerService{//管理员的具体操作

	//编写者:张越 时间: 2019年7月8日 14时15分
	@Override
	public void modifyPage(String PageName) {//修改版面
		new PageServiceImpl().modifyPage(PageName);//调用版面服务的修改版面方法
	}
	
	//编写者:张越 时间: 2019年7月8日 14时20分
	@Override
	public void addPage() {//添加版面
		new PageServiceImpl().addPage();//调用版面服务的增加版面方法
	}
	
	//编写者:张越 时间: 2019年7月8日 14时22分
	@Override
	public void deletePage() {//删除版面
		new PageServiceImpl().deletePage();//调用版面服务的删除版面方法
	}
	
	//编写者:张越 时间: 2019年7月8日 14时24分
	@Override
	public void blackUser() {//拉黑用户,仅让其无法登陆，仍保留原帖信息
		Scanner input = new Scanner (System.in);
		FeedbackDao feedbackDao = new FeedbackDaoImpl();
		ArrayList<Feedback> allFB = feedbackDao.getAllFeedback();//返回当前所有的举报信息
		System.out.println("所有的举报信息如下:\n序号\t相关帖子编号\t举报人ID\t被举报人ID\t举报时间\t举报理由");
		for(int i = 0 ; i<allFB.size() ; i++ ) {//列出所有举报信息
			System.out.println((i+1)+"\t"+allFB.get(i).getForumId()+"\t\t"+allFB.get(i).getComplainerId()+"\t"+allFB.get(i).getDefendantId()+"\t\t"+allFB.get(i).getAccuseTime()+"\t"+allFB.get(i).getReason());
		}
		do {
			System.out.println("选择拉黑哪个用户,输入用户ID:(输入0退出)");
			String choose = "";
			do {
				choose = input.nextLine().trim();//去除头尾空白字符串
			}while(choose.length()==0);
			try {//输入非法时抛出异常
				Integer.valueOf(choose);
				if(Integer.valueOf(choose)%1!=0) {
					throw new Exception();
				}
			}catch(Exception e) {//对输入非法值进行处理
				System.out.println("输入值有误,请重新输入");
				continue;
			}
			String sql = "select * from ForumUser where id = ?";
			String[] uParam = {choose};
			if(Integer.valueOf(choose)==0) {
				System.out.println("用户选择退出,返回上一界面");
				return;
			}
			else if(new UserDaoImpl().selectUser(sql, uParam).size()==0) {//返回该ID下对用户list为空，说明该用户不存在
				System.out.println("该用户不存在，请重新输入");
			}
			else {//用户存在
				System.out.println("您想要拉黑用户"+new UserDaoImpl().selectUser(sql, uParam).get(0).getUserName()+"吗,确认请输入y");
				String confirm ="";
				do {
					confirm = input.nextLine().trim();//去除头尾空白字符串
				}while(confirm.length()==0);
				if(confirm.equals("y")) {//设置用户被拉黑属性为真
					sql = "update ForumUser set black = true where id= ?";
					Object[] param = {choose};
					UserDao userDao = new UserDaoImpl();
					userDao.updateUser(sql, param);
					System.out.println("拉黑操作完成,该用户将无法再次登陆本论坛");
				}
				else {
					System.out.println("用户取消该操作");
				}
			}
		}while(true);
	}
	
	//编写者:张越 时间: 2019年7月8日 15时07分
	@Override
	public void Top(Page page) {//置顶某版面内的某个帖子
		Scanner input = new Scanner(System.in);
		PageService pageService = new PageServiceImpl();
		ArrayList<Forum> allForum=pageService.getAllForum(page.getName());//获取当前版面内所有帖子
		if(allForum.size()==0) {
			System.out.println("当前版面内无任何帖子");
			return;
		}
		ForumService forumService = new ForumServiceImpl();
		allForum = forumService.showPageForum(allForum);
		String choose = "";//帖子序号
		boolean flag = true;//判断用户帖子序号是否输入正确
		do {
			System.out.println("选择要置顶的帖子前的序号(输入0终止该操作):");
			do {
				choose = input.nextLine().trim();//去除头尾空白字符串
			}while(choose.length()==0);
			try {//输入非法时抛出异常
				Integer.valueOf(choose);
				if(Integer.valueOf(choose)%1!=0) {
					throw new Exception();
				}
			}catch(Exception e) {//对输入非法值进行处理
				System.out.print("输入值不是整数,请重新");
				continue;
			}
			if(Integer.valueOf(choose)<0||Integer.valueOf(choose)>allForum.size()) {
				System.out.print("输入序号不存在,请重新");
			}
			else if(Integer.valueOf(choose)==0) {
				System.out.println("用户决定终止该操作,返回上一界面");
				return;
			}
			else {//输入序号存在时跳出循环
				flag=false;
			}
		}while(flag);
		System.out.println("是否确认置顶帖子:"+allForum.get(Integer.valueOf(choose)-1).getTitle()+",确认请输入y:");
		String confirm ="";
		do {
			confirm = input.nextLine().trim();//去除头尾空白字符串
		}while(confirm.length()==0);
		if(confirm.equals("y")) {
			ForumDao forumDao = new ForumDaoImpl();
			String sql = "update forum \n" + 
					     "set priority = (select pri from (select max(priority) as pri from forum where pagename =?) as temp)+1\n" + 
					     "where id =?";
			Object[] param = {allForum.get(Integer.valueOf(choose)-1).getPagename(),allForum.get(Integer.valueOf(choose)-1).getId()};
			forumDao.updateForum(sql, param);
			System.out.println("置顶操作已完成");
			forumService.showPageForum(pageService.getAllForum(page.getName()));
		}else {
			System.out.println("用户决定终止该操作,返回上一界面");
			return;
		}
	}

	//编写者:张越 时间: 2019年7月8日 15时35分
	@Override
	public void UserLevelUp() {//设置某人为管理员
		Scanner input = new Scanner(System.in);
		UserDao userDao = new UserDaoImpl();
		System.out.println("输入您想设置为管理员的用户id:");
		String id = "" , sql="select * from forumUser where id =?";
		do {
			id = input.nextLine().trim();//删除字符串的头尾空白符
		}while(id.length()==0);
		String[] param = {id};
		ArrayList<User> selectUser = userDao.selectUser(sql, param);
		if(selectUser.size()==0) {//返回值为0表示所查用户不存在
			System.out.println("未找到该用户");
		}
		else {
			System.out.println("该用户的信息如下");
			selectUser.get(0).showUserInformation();//显示ID为输入ID的用户信息
			System.out.println("输入y确认设置该用户为管理员:");
			String confirm ="";
			do {
				confirm = input.nextLine().trim();//删除字符串的头尾空白符
			}while(confirm.length()==0);
			if(confirm.equals("y")) {
				sql = "update ForumUser set description = '管理员' where id = ?";
				Object[] up= {Integer.parseInt(id)};
				new UserDaoImpl().updateUser(sql, up);//设置该用户的用户描述为管理员
				System.out.println("操作成功,该用户已被设置为管理员,自动返回主界面");
			}
			else {
				System.out.println("管理员放弃本次操作,自动返回操作界面");
			}
		}
	}
}
