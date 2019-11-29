package entity;

import java.util.*;

import Util.UserUtil;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import role.Role;
//编写者:苗奇 时间:2019年7月6日 13时24分
public class User {//用户类
	
	private long id; // 用户id
	
	private String userName; //用户名
	
	private String password; // 用户密码
	
	private int defendantTimes;//被举报次数

	private int editTimes;//用户发帖量
	
	private boolean black;//是否被拉黑

	private Role role = new Role();//用户角色

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDefendantTimes() {
		return defendantTimes;
	}

	public void setDefendantTimes(int defendantTimes) {
		this.defendantTimes = defendantTimes;
	}

	public int getEditTimes() {
		return editTimes;
	}

	public void setEditTimes(int editTimes) {
		this.editTimes = editTimes;
	}

	public boolean isBlack() {
		return black;
	}

	public void setBlack(boolean black) {
		this.black = black;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	//编写者:苗奇 时间:2019年7月6日 13时27分
	public User login() {//用户登陆
		Scanner input = new Scanner(System.in);
		UserDao userDao = new UserDaoImpl();//初始化userDao对象,进行数据库操作
		System.out.println("您已进入登陆界面,请输入您的用户名:(按0退出)");
		User newUser = null;
		String sql = "select * from ForumUser where userName=?";//判断该用户是否存在
		int count=0;//记录输入错误次数,三次错误则退出登陆界面,返回主界面
		do {//当用户未登陆时，进行循环
			boolean correctPassword = false;//判断输入密码是否正确
			String Name = "";
			do {
				Name = input.nextLine().trim();//删除字符串的头尾空白符
			}while(Name.length()==0);
			if(Name.equals("0")) {//用户选择退出界面
				System.out.println("用户放弃登陆,返回主界面");
				return null;
			}
			String[] param= {Name};//预设置sql语句
			ArrayList<User> existUser=userDao.selectUser(sql, param);//返回用户名为输入Name的用户信息
			System.out.println("请您输入密码：");
		    String password = "";
		    do {
				password = input.nextLine().trim();//删除字符串的头尾空白符
			}while(password.length()==0);
		    boolean exist = false;
		    for(int i = 0 ; i<existUser.size() ; i++) {
		    	if(Name.equals(existUser.get(i).getUserName())){
		    		exist = true;
		    		break;
		    	}
		    	else {
		    		existUser.remove(i);
		    		i--;
		    	}
		    }
		    if(existUser.size()==0) {
		    	count++;
		    	if(count==3) {//用户输入错误三次,自动返回主界面
			    	System.out.println("当前用户输入错误达到三次,自动返回主界面");
			    	return null;
			    }
		    	else{
		    		System.out.println("用户输入用户名不存在,您还有"+(3-count)+"次机会重新输入正确的账号及密码");
		    	}
		    }
		    else if(exist == false) {
		    	count++;
		    	if(count==3) {//用户输入错误三次,自动返回主界面
			    	System.out.println("当前用户输入错误达到三次,自动返回主界面");
			    	return null;
			    }
		    	else{
		    		System.out.println("用户名输入错误,您还有"+(3-count)+"次机会重新输入正确的账号及密码");
		    	}
		    }
		    else {//用户list中存在用户
		    	if(existUser.get(0).isBlack()) {//用户被拉黑。账号存在但无法登陆
		    		System.out.println("该用户因被举报"+existUser.get(0).getDefendantTimes()+"次等原因被管理员拉黑，当前无法登陆");
		    		return null;
		    	}
		    	int i = 0;
		    	for(i = 0 ; i<existUser.size(); i++) {//判断是否存在和已知用户存储密码相同的密码
		    		if(new UserUtil().encode(password).equals(existUser.get(i).getPassword())) {//判断加密后的输入密码和存储密码是否相同
		    			correctPassword = true;//相同设置标识符为真
		    			break;
		    		}
		    	}
		    	if(correctPassword == true) {//用户存在且输入密码正确
		    		newUser=existUser.get(i);//用户成功登陆
		    		break;//跳出do-while循环,忽略之后的count++,
		    	}
		    	count++;
		    	if(count==3) {//用户输入错误三次,自动返回主界面
			    	System.out.println("当前用户输入错误达到三次,自动返回主界面");
			    	return null;
			    }
		    	else {
		    		System.out.println("用户输入密码错误,您还有"+(3-count)+"次机会重新输入正确的账号及密码");
		    	}
		    }
		    System.out.println("请重新输入您的用户名:(按0退出)");
		}while(newUser==null);
		System.out.println("恭喜用户"+newUser.getUserName()+"成功登录论坛,目前发帖数为"+newUser.getEditTimes());
		return newUser;//返回该用户信息
	}
	
	//编写者:苗奇 时间:2019年7月7日 10时27分
	public void modifyPage(String PageName) {//修改版面
		role.modifyPage(PageName);
	}

	//编写者:苗奇 时间:2019年7月7日 10时29分
	public ArrayList<Forum> getUserForum(long id) {//获取某用户的全部帖子
		return role.getUserForum(id);
	}
	
	//编写者:苗奇 时间:2019年7月7日 10时34分
	public void modifySelfInformation(User user) {//修改自己的信息
		role.modifySelfInformation(user);
	}

	//编写者:苗奇 时间:2019年7月7日 10时38分
	public User editorOfForum(Forum forum) {//根据帖子找用户
		return role.editorOfForum(forum);
	}

	//编写者:苗奇 时间:2019年7月7日 10时44分
	public void accuseUser(User user) {//举报某用户
		role.accuseUser(user);
	}

	//编写者:苗奇 时间:2019年7月7日 10时47分
	public void addPage() {//添加版面
		role.addPage();
	}

	//编写者:苗奇 时间:2019年7月7日 10时51分
	public void deletePage() {//删除版面
		role.deletePage();
	}

	//编写者:苗奇 时间:2019年7月7日 10时54分
	public void blackUser() {//拉黑用户
		role.blackUser();
	}

	//编写者:苗奇 时间:2019年7月7日 10时56分
	public void Top(Page page) {//置顶某版面内的某个帖子
		role.Top(page);
	}

	//编写者:苗奇 时间:2019年7月7日 10时58分
	public void modifyForum(User user) {//修改帖子内容
		role.modifyForum(user);
	}

	//编写者:苗奇 时间:2019年7月7日 11时01分
	public void postForum(User user,String pagename) {//发帖
		role.postForum(user,pagename);
	}

	//编写者:苗奇 时间:2019年7月7日 11时04分
	public void deleteUserForum(User user) {//删帖
		role.deleteUserForum(user);
	}

	//编写者:苗奇 时间:2019年7月7日 11时06分
	public void replyForum(User user, Forum forum) {//回帖
		role.replyForum(user,forum);
	}

	//编写者:苗奇 时间:2019年7月7日 11时09分
	public void showUserInformation() {//显示用户信息
		System.out.println("用户id为:"+id+"\t用户名为:"+userName+"\t用户的发帖数为:"+editTimes);
	}
	
	//编写者:苗奇 时间:2019年7月7日 11时12分
	public void UserLevelUp() {//显示用户信息
		role.UserLevelUp();
	}
}
