package role;

import service.ManagerService;
import service.UserService;
import service.impl.ForumServiceImpl;
import service.impl.NormalUser;
import java.util.ArrayList;
import entity.Forum;
import entity.Page;
import entity.User;

//编写者:徐越 时间: 2019年7月6日 10时23分
public class Role {
	
	private String description = "游客";//角色名
	
	private UserService normalUser = null;//普通用户权限
	
	private ManagerService manager = null;//管理员权限

	
	public String getDescription() {//获得当前用户的描述信息，游客、用户、管理员
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserService getNormalUser() {
		return normalUser;
	}

	public void setNormalUser(UserService normalUser) {
		this.normalUser = normalUser;
	}

	public ManagerService getManager() {
		return manager;
	}

	public void setManager(ManagerService manager) {
		this.manager = manager;
	}
	
	//编写者:徐越 时间: 2019年7月6日 10时32分
	public ArrayList<Forum> getUserForum(long id) {//获取某用户的全部帖子
		return new NormalUser().getUserForum(id);
	}

	//编写者:徐越 时间: 2019年7月6日 10时35分
	public void modifySelfInformation(User user) {//修改自己的信息
		if(normalUser == null&& manager == null) {
			System.out.println("您没有该操作权限,请登陆");
			return;
		}
		normalUser.modifySelfInformation(user);
	}

	//编写者:徐越 时间: 2019年7月6日 10时40分
	public User editorOfForum(Forum forum) {//根据帖子找用户
		return new NormalUser().editorOfForum(forum);
	}

	//编写者:徐越 时间: 2019年7月6日 10时45分
	public void accuseUser(User user) {//举报某用户
		if(normalUser == null&& manager == null) {
			System.out.println("您没有该操作权限,请登陆");
			return;
		}
		normalUser.accuseUser(user);
	}

	//编写者:徐越 时间: 2019年7月6日 10时53分
	public void addPage() {//添加版面
		if(manager==null) {
			System.out.println("您没有该操作权限");
			return;
		}
		manager.addPage();
	}

	//编写者:徐越 时间: 2019年7月6日 10时57分
	public void deletePage() {//删除版面
		if(manager==null) {
			System.out.println("您没有该操作权限");
			return;
		}
		manager.deletePage();
	}

	//编写者:徐越 时间: 2019年7月6日 11时02分
	public void modifyPage(String PageName) {//修改版面
		if(manager==null) {
			System.out.println("您没有该操作权限");
			return;
		}
		manager.modifyPage(PageName);
	}

	//编写者:徐越 时间: 2019年7月6日 11时06分
	public void blackUser() {//拉黑用户
		if(manager==null) {
			System.out.println("您没有该操作权限");
			return;
		}
		manager.blackUser();
	}

	//编写者:徐越 时间: 2019年7月6日 11时10分
	public void Top(Page page) {//置顶某版面内的某个帖子
		if(manager==null) {
			System.out.println("您没有该操作权限");
			return;
		}
		manager.Top(page);
	}

	//编写者:徐越 时间: 2019年7月6日 11时16分
	public void modifyForum(User user) {//修改帖子内容
		if(normalUser == null&& manager == null) {
			System.out.println("您没有该操作权限,请登陆");
			return;
		}
		new ForumServiceImpl().modifyForum(user);
	}

	//编写者:徐越 时间: 2019年7月6日 11时24分
	public void postForum(User user,String pagename) {//发帖
		if(normalUser == null&& manager == null) {
			System.out.println("您没有该操作权限,请登陆");
			return;
		}
		new ForumServiceImpl().postForum(user,pagename);
	}

	//编写者:徐越 时间: 2019年7月6日 11时28分
	public void deleteUserForum(User user) {//删帖
		if(normalUser == null&& manager == null) {
			System.out.println("您没有该操作权限,请登陆");
			return;
		}
		new ForumServiceImpl().deleteUserForum(user);
	}

	//编写者:徐越 时间: 2019年7月6日 11时33分
	public void replyForum(User user,Forum forum) {//回帖
		if(normalUser == null&& manager == null) {
			System.out.println("您没有该操作权限,请登陆");
			return;
		}
		new ForumServiceImpl().replyForum(user,forum);
	}
	
	//编写者:徐越 时间: 2019年7月6日 11时33分
	public void UserLevelUp() {//回帖
		if(manager == null) {
			System.out.println("您没有该操作权限,请登陆");
			return;
		}
		manager.UserLevelUp();
	}
}
