package service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;

import Util.UserUtil;
import dao.ForumDao;
import dao.PageDao;
import dao.UserDao;
import dao.replyForumDao;
import dao.impl.ForumDaoImpl;
import dao.impl.PageDaoImpl;
import dao.impl.UserDaoImpl;
import dao.impl.replyForumDaoImpl;
import entity.Forum;
import entity.User;
import entity.replyForum;
import service.ForumService;
import service.UserService;
//编写者:张煜明 时间: 2019年7月7日 9时32分
public class ForumServiceImpl implements ForumService{
	
	//编写者:张煜明 时间: 2019年7月7日 9时34分
	@Override
	public void modifyForum(User user) {//修改帖子内容
		Scanner input = new Scanner(System.in);
		UserService userService = new NormalUser();
		ArrayList<Forum> allUserForum = userService.getUserForum(user.getId());//找出数据库中该用户的所有帖子
		if(allUserForum.size()==0) {//list长度为0,表示该用户无任何帖子发布
			System.out.println("该用户无任何帖子发布,无法修改帖子,自动返回上一级");
			return ;
		}
		System.out.println("用户"+user.getUserName()+"的全部帖子信息如下");
		showUserForum(allUserForum);//显示用户的所有帖子信息
		String forumNumber="",choose ="";
		boolean flag= true;
		do {
			System.out.println("选择要修改的帖子序号:(输入0返回上一级)");
			do {
				forumNumber = input.nextLine().trim();//去除首尾空白字符
			}while(forumNumber.length()==0);
			try {//对数据非法进行抛出和处理
				Integer.valueOf(forumNumber);
				if(Integer.valueOf(forumNumber)%1!=0) {
					throw new Exception();
				}
			}catch(Exception e) {
				System.out.println("输入值不是整数,请重新输入");
				continue;
			}
			if(Integer.valueOf(forumNumber)==0) {
				System.out.println("用户取消该操作,返回上一级");	
				return;
			}
			else if(Integer.valueOf(forumNumber)>allUserForum.size()||Integer.valueOf(forumNumber)<0) {
				System.out.println("输入序号不存在,请重新输入");	
			}
			else
				flag=false;
		}while(flag);
		do {
			System.out.println("输入1以修改标题，输入2以修改内容(输入0返回上一级):");
			do {
				choose = input.nextLine().trim();//去除首尾空白字符
			}while(choose.length()==0);
			try {//对数据非法进行抛出和处理
				Integer.valueOf(choose);
				if(Integer.valueOf(choose)%1!=0) {
					throw new Exception();
				}
			}catch(Exception e) {
				System.out.print("输入值不是整数,请重新");
				continue;
			}
			switch(Integer.valueOf(choose)) {
			case 1: {//对标题进行修改
				System.out.println("请输入您这篇帖子新的标题:(标题在20词以内,不能含敏感词,输入0取消操作)");
			    String sql= "select * from Forum where title=?";
			    String title="";
			    do{//删除字符串的头尾空白符
			    	title = input.nextLine().trim();
			    }while(title.length()==0);
			    if(title.equals("0")) {//用户选择退出
					System.out.println("用户确认放弃该操作");
					continue;
				}
			    while(new UserUtil().announceable(title).size()!=0||title.length()>20||title.length()==0) {//调用工具类中的返回所含敏感词list的方法
			    	if(title.length()>20||title.length()==0) {
						System.out.print("标题长度不符合要求，重新输入:(输入0取消操作)");
					}
					else
						System.out.println("当前标题中含敏感词,请重新输入:(输入0取消操作)");
		    		do{//删除字符串的头尾空白符
				    	title = input.nextLine().trim();
				    }while(title.length()==0);
		    		if(title.equals("0")) {//用户选择退出
						System.out.println("用户确认放弃该操作");
						continue;
					}
		    	}
			    ForumDao forumDao = new ForumDaoImpl();
				String[] param = {title};//为sql语句设置参数
		        ArrayList<Forum> existForum = forumDao.selectForum(sql, param);//返回与输入用户名相同用户名的用户list
			    if(existForum.size()==0) {//没有与之同名的帖子
			    	System.out.println("你修改后的帖子名为"+title+",请输入y以确认修改:");
					String confirm="";
					do {
						confirm = input.nextLine().trim();
					}while(confirm.length()==0);
					if(confirm.equals("y")) {//用户同意修改
				    	sql="update forum set title = ?,editDate=? where id = ?";
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				    	Object[] reparam = {title, date ,allUserForum.get(Integer.valueOf(forumNumber)-1).getId()};//为sql预设参数
				    	forumDao.updateForum(sql, reparam);//更新帖子名称信息
					    System.out.println("修改成功!修改后信息如下:");
					    allUserForum = userService.getUserForum(user.getId());
						System.out.println(allUserForum.get(Integer.parseInt(forumNumber)-1).getTitle()+"\t  编辑日期"+allUserForum.get(Integer.parseInt(forumNumber)-1).getEditDate()+"\n"+allUserForum.get(Integer.parseInt(forumNumber)-1).getContent());
					}
					else {
						System.out.println("用户放弃修改!返回选项部分");
					}
			    }
				else 
					System.out.print("新的帖子名已存在,请");
			}break;
			case 2: {//对内容进行修改
				int count=0;
				String temp = "" ;
				StringBuffer content=  new StringBuffer();
				System.out.println("输入帖子具体内容,最后一行输入0表示输入完毕:(注意:敏感词可存在,后会以*代替)");
				do {
					temp = input.nextLine();
					if(!temp.equals("0")&&temp.trim().length()!=0) {//没有结束且当前输入不为空时向buffer中添加字符串
						for(int i = 0 ; i< count ; i++) {//输出之前的空行
							content.append("\n");
						}
						count=0;
						content.append(temp+"\n");
					}
					if(!temp.equals("0")&&temp.trim().length()==0) {//记录空白行数
						count++;
					}
				}while(!temp.equals("0"));//没有确认结束
				temp = content.toString();
				if(temp.length()==0) {//帖子长度为0
					System.out.println("用户帖子内无任何内容,无法发出,自动回到上一界面");
				}
				else {
					ArrayList<String> sw = new UserUtil().announceable(temp);//列出所有的帖子中含有的敏感词信息
					if(sw.size()>0) {//list长度不为0，说明含有敏感词
						System.out.println("帖子中含有敏感词如下");
						for(int i = 0 ; i < sw.size() ; i++) {
							System.out.print(sw.get(i)+" ");
							temp = temp.replaceAll(sw.get(i),"***");//使用replaceAll屏蔽关键词
						}
					}
					System.out.println("\n最终帖子内容如下:\n"+temp+"用户确认发出该帖么,输入y以确认");
					String confirm ="";
					do {
						confirm = input.nextLine().trim();//去除首尾字符串
					}while(confirm.length()==0);
					if(confirm.equals("y")) {//用户确认进行修改
						String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//获取当前日期并按yyyy-MM-dd形式保存
						String sql = "update forum set content = ?,editDate=? where id = ?";
						ForumDao forumDao = new ForumDaoImpl();
						Object[] param ={temp , date , allUserForum.get(Integer.parseInt(forumNumber)-1).getId() };//为预设置sql语句设置参数
						forumDao.updateForum(sql, param);//更新帖子内数据库
						System.out.println("修改成功!修改后信息如下:");
					    allUserForum = userService.getUserForum(user.getId());
					    //输出显示修改后的信息
					    System.out.println(allUserForum.get(Integer.parseInt(forumNumber)-1).getTitle()+"\t  编辑日期:"+allUserForum.get(Integer.parseInt(forumNumber)-1).getEditDate()+"\n"+allUserForum.get(Integer.parseInt(forumNumber)-1).getContent());
					}
					else {
						System.out.println("用户选择取消该操作,返回上一级");
					}
				}
			}break;
			case 0: System.out.println("用户取消该操作,返回上一级");	return;
			default: System.out.println("输入值错误,请重新输入");break;
			}
		}while(true);
	}

	//编写者:张煜明 时间: 2019年7月7日 10时17分
	@Override
	public void postForum(User user,String pagename) {//某用户发帖
		boolean flag = true;
		Scanner input = new Scanner (System.in);
		int count=0;
		String title ="", temp = "" ;
		StringBuffer content=  new StringBuffer();
		do {
			System.out.println("输入帖子标题,回车表示输入完毕(标题在20词以内,不能含敏感词,输入0取消发帖操作):");
			do {
				title = input.nextLine().trim();//读取整行,去除头尾空格
			}while(title.length()==0);
			if(title.equals("0")) {
				System.out.println("用户选择取消该操作,返回上一级");
				return;
			}
			String sql= "select * from forum where title=?";
		    ForumDao forumDao = new ForumDaoImpl();
			String[] param = {title};//为sql语句设置参数
	        ArrayList<Forum> existForum = forumDao.selectForum(sql, param);//返回与输入用户名相同用户名的用户list
		    if(existForum.size()!=0) {//有与之同名的帖子
		    	System.out.print("新的帖子名已存在,请请重新");
		    }
			else if(title.length()>20||title.length()==0) {
				System.out.print("标题长度不符合要求，请重新");
			}
			else if(new UserUtil().announceable(title).size()!=0) {
				System.out.print("标题内含敏感词,不符合要求，请重新");
			}
			else {
				flag=false;//用户标题符合要求，结束循环
			}
		}while(flag);
		System.out.println("输入帖子具体内容,最后一行输入0表示输入完毕:(注意:敏感词可存在,后会以*代替)");
		do {
			temp = input.nextLine();
			if(!temp.equals("0")&&temp.trim().length()!=0) {//没有结束且当前输入不为空时向buffer中添加字符串
				for(int i = 0 ; i< count ; i++) {//输出之前的空行
					content.append("\n");
				}
				count=0;
				content.append(temp+"\n");
			}
			if(!temp.equals("0")&&temp.trim().length()==0) {//记录空白行数
				count++;
			}
		}while(!temp.equals("0"));
		temp = content.toString();
		if(temp.length()==0) {//输入字符串长度为0
			System.out.println("用户帖子内无任何内容,无法发出,自动回到上一界面");
		}
		else {
			ArrayList<String> sw = new UserUtil().announceable(temp);//获取当前帖子内所有的敏感词
			if(sw.size()>0) {
				System.out.println("帖子中含有敏感词如下");
				for(int i = 0 ; i < sw.size() ; i++) {
					System.out.print(sw.get(i)+" ");
					temp = temp.replaceAll(sw.get(i),"*");//用replaceAll屏蔽敏感词
				}
			}
			System.out.println("最终帖子内容如下:\n"+temp+"用户确认发出该帖么,输入y以确认");
			String confirm ="";
			do {
				confirm = input.nextLine().trim();//去除头尾的空白字符
			}while(confirm.length()==0);
			if(confirm.equals("y")) {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//获取当前日期并按该yyyy-MM-dd形式保存
				String sql = "insert into forum(id,userId, title, content, editDate,hasReplyForum,Pagename, priority) values (?,?,?,?,?,?,?,0)";
				ForumDao forumDao = new ForumDaoImpl();
				long id = new UserUtil().productRandomId();
				Object[] param ={id, user.getId(), title, temp, date , false , pagename};
				forumDao.updateForum(sql, param);
				//更新帖子内数据库
				System.out.println("用户发帖成功,帖子编号为"+id+",用户ID为:"+user.getId()+"\n帖子标题为"+title+"\t帖子编辑时间为"+date
						+"\n帖子内容为\n"+temp+"所属版面为:"+pagename);
				sql = "update ForumUser set editTimes= editTimes+1 where id = ?";
				Object [] userParam = {user.getId()};
				UserDao userDao = new UserDaoImpl();
				userDao.updateUser(sql, userParam);//更新用户发帖数
				sql ="update page set ForumNumber= ForumNumber+1 where name = ?";
				Object [] pageParam = {pagename};
				PageDao pageDao = new PageDaoImpl();
				pageDao.updatePage(sql, pageParam);//更新版面发帖量
			}
			else {
				System.out.println("用户选择取消该操作,返回上一级");
			}
		}
	}

	//编写者:张煜明 时间: 2019年7月7日 12时21分
	@Override
	public void deleteUserForum(User user) {//用户本身执行删帖操作
		Scanner input = new Scanner(System.in);
		UserService userService = new NormalUser();
		ArrayList<Forum> allUserForum = new ArrayList<Forum>();
		if(user.getRole().getDescription().equals("用户")) {
			allUserForum = userService.getUserForum(user.getId());//返回该用户的所有帖子存储在list中
			if(allUserForum.size()==0) {//list内无任何帖子,即该用户没有发过帖子
				System.out.println("该用户无任何帖子发布,无法删帖");
				return ;
			}
	    	System.out.println("用户"+user.getUserName()+"的全部帖子信息如下");
		}
		else{
			allUserForum = new ForumDaoImpl().getAllForum();
			System.out.println("全部帖子信息如下:");
		}
		showUserForum(allUserForum);//现实用户的所有帖子信息
		System.out.println("选择要删除的帖子序号:");
		String choose = "";
		do {
			choose = input.nextLine().trim();
		}while(choose.length()==0);
		//输入为整数且存在该帖子序号
		try{
			Integer.parseInt(choose);
			if(Integer.parseInt(choose)%1!=0)
				throw new Exception();
		}catch(Exception e) {
			System.out.print("用户输入非法,返回上一级");
			return ;
		}
		if(Integer.parseInt(choose)>allUserForum.size()||Integer.parseInt(choose)<=0) {
			System.out.print("用户输入序号不存在,返回上一级");
			return ;
		}
		System.out.println("所删原帖为:");
    	//显示原帖内容
		System.out.println("帖子标题\t"+allUserForum.get(Integer.parseInt(choose)-1).getTitle()+"编辑日期:"+allUserForum.get(Integer.parseInt(choose)-1).getEditDate()+"\n"+allUserForum.get(Integer.parseInt(choose)-1).getContent());
		System.out.print("是否确认删除(输入y以确认):");
		String confirm ="";
		do {
			confirm  = input.nextLine().trim();//去除字符串两端的空白字符
		}while(confirm.length()==0);
		if(!confirm.equals("y")) {
			System.out.print("用户取消本次删帖操作,返回上一界面");
			return;
		}
		else {
			deleteForum(allUserForum.get(Integer.parseInt(choose)-1));//调用具体的删帖操作,将原帖及其回帖全部删除
			allUserForum = userService.getUserForum(user.getId());//存储删帖后的用户的全部帖子
			if(allUserForum.size()>0) {
				 System.out.println("删帖操作成功结束\n用户"+user.getUserName()+"的全部帖子信息如下");
			     showUserForum(allUserForum);//显示用户的全部帖子
			}
			else {//用户删除后帖子数为0
				System.out.println("帖子已成功删除!删除后用户发帖量为0");
			}
		}
	}
	
	//编写者:张煜明 时间: 2019年7月7日 15时32分
	@Override
	public void deleteForum(Forum forum) {//实现删帖的具体方法
		String sql = "select * from forum where id = ?";
		String[] param = {String.valueOf(forum.getId())};
		ForumDao forumDao= new ForumDaoImpl();
		if(forumDao.selectForum(sql, param).size()==0) {//该帖不存在
			return;
		}
		else{//该帖存在
			//首先判断其是不是某个帖子的回帖
			sql = "select * from replyForum where replyForumId = ?";
			replyForumDao replyforumDao = new replyForumDaoImpl();
			ArrayList<replyForum> r = replyforumDao.selectReply(sql, param);//原帖的list
			if(r.size()!=0) {//该帖是某个帖子的回帖
				sql = "select  * from replyForum where originForumId = ? and replyForumId <> ?";
				String [] rParam = {String.valueOf(r.get(0).getOriginForumId()), String.valueOf(forum.getId())};
				if(replyforumDao.selectReply(sql, rParam).size()==0) {//原帖只有这一个回帖
					sql = "update forum set hasReplyForum = false where id = ? ";//置其是否有回帖为false
					Object[] delParam = {r.get(0).getOriginForumId()};
					new ForumDaoImpl().updateForum(sql, delParam);//更新
				}
				sql = "delete from replyForum where originForumId = ? and replyForumId= ? ";
				Object [] delReplyForum = {r.get(0).getOriginForumId(),forum.getId()};
				replyforumDao.updateReply(sql, delReplyForum);//删除在回帖表中的数据
			}
			ArrayList<Forum> replyForum = new ArrayList<Forum>();//该list存放包括原帖在内的所有回帖
			replyForum.add(forum);//将原帖置入list中
			while(replyForum.size()!=0) {//回帖没有删除完
				Forum temp = replyForum.get(0);//取出第一个
				replyForum.remove(0);//移除并作删除操作
				sql = "delete from forum where id = ?";
			    Object[] forumParam = {temp.getId()};
			    forumDao.updateForum(sql, forumParam );//在数据库中删除这条帖子的记录
			    sql = "update ForumUser set editTimes = editTimes-1 where id=?";
			    Object[] userParam = {new NormalUser().editorOfForum(temp).getId()};
			    UserDao userDao =new UserDaoImpl();
			    userDao.updateUser(sql, userParam);//在用户数据库中将发帖数减1
			    sql = "update page set ForumNumber= ForumNumber-1 where name= ?";
			    Object []pageParam = {temp.getPagename()};
			    PageDao pageDao = new PageDaoImpl();
			    pageDao.updatePage(sql, pageParam);//在版面数据库中将含有帖子数减1
			    if(temp.isHasReplyForum()==true) {//该帖有回帖
			    	sql = "select * from replyForum where originForumId= ?";//原帖ID是该帖ID的回复组取出
			    	String[] replyForumParam = {String.valueOf(temp.getId())};
			    	r = replyforumDao.selectReply(sql, replyForumParam);
			    	for(int i = 0 ; i<r.size() ; i++) {//对于所有的回帖
			    		sql = "select * from forum where id = ?";
			    		replyForumParam[0] = String.valueOf(r.get(i).getReplyForumId());
			    		replyForum.add(forumDao.selectForum(sql, replyForumParam).get(0));//置于replyForum这个list中等待删除
			    		sql = "delete from replyForum where originForumId = ? and replyForumId= ? ";
						Object [] delReplyForum = {temp.getId(),r.get(i).getReplyForumId()};
						replyforumDao.updateReply(sql, delReplyForum);//删除在回帖表中的数据
			    	}
			    }
			}
		}
	}
	
	//编写者:张煜明 时间: 2019年7月7日 19时21分
	@Override
	public void replyForum(User user, Forum forum) {//回帖
		Scanner input = new Scanner (System.in);
		int count=0;
		String temp =" ";
		StringBuffer content= new StringBuffer();
		System.out.println("输入帖子具体内容,最后一行输入0表示输入完毕:(注意:空行不保留但敏感词可存在,后会以*代替)");
		do {
			temp = input.nextLine();
			if(!temp.equals("0")&&temp.trim().length()!=0) {//没有结束且当前输入不为空时向buffer中添加字符串
				for(int i = 0 ; i< count ; i++) {//输出之前的空行
					content.append("\n");
				}
				count=0;
				content.append(temp+"\n");
			}
			if(!temp.equals("0")&&temp.trim().length()==0) {//记录空白行数
				count++;
			}
		}while(!temp.equals("0"));
		temp = content.toString();
		if(temp.length()==0) {//帖子长度为0
			System.out.println("用户帖子内无任何内容,无法发出,自动回到上一界面");
		}
		else {
			ArrayList<String> sw = new UserUtil().announceable(temp);//返回该帖子中的所有敏感词信息
			if(sw.size()>0) {
				System.out.println("帖子中含有敏感词如下");
				for(int i = 0 ; i < sw.size() ; i++) {
					System.out.print(sw.get(i)+" ");
					temp = temp.replaceAll(sw.get(i),"**");
				}
			}
			System.out.println("最终帖子内容如下:\n"+temp+"用户确认发出该帖么,输入y以确认");
			String confirm = "";
			do {
				confirm= input.nextLine().trim();
			}while(confirm.length()==0);
			if(confirm.equals("y")) {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				//设置发帖日期为yyyy-mm-dd的形式
				String sql = "insert into forum(id,userId, title, content, editDate,hasReplyForum,Pagename, priority) values (?,?,?,?,?,?,?,0)";
				ForumDao forumDao = new ForumDaoImpl();
				long id = new UserUtil().productRandomId()+forum.getId();
				Object[] param ={id, user.getId(), "re:"+forum.getTitle() , temp, date, false , forum.getPagename()};
				forumDao.updateForum(sql, param);//更新帖子内数据库,新增回帖
				sql = "update forum set hasReplyForum = true where id = ?";
				Object[] forumPage= {forum.getId()};
				forumDao.updateForum(sql, forumPage);//更新帖子内数据库,原帖设置为存在回帖
				System.out.println("用户回帖成功,用户ID为:"+user.getId()+"\t帖子编辑时间为"+date +"\n回帖子内容为:\n"+temp);
				sql = "update ForumUser set editTimes= editTimes+1 where id = ?";
				Object [] userParam = {user.getId()};
				UserDao userDao = new UserDaoImpl();
				userDao.updateUser(sql, userParam);//更新用户发帖数
				sql ="update page set ForumNumber= ForumNumber+1 where name = ?";
				Object [] pageParam = {forum.getPagename()};
				PageDao pageDao = new PageDaoImpl();
				pageDao.updatePage(sql, pageParam);//更新版面发帖量
				replyForumDao rf = new replyForumDaoImpl();
				sql = "insert into replyForum(originForumId,replyForumId) values (?,?)";
				Object [] reply= {forum.getId(), id};
				rf.updateReply(sql, reply);//更新记录回帖的数据库信息
			}
			else {
				System.out.println("用户选择取消该操作,返回上一级");
			}
		}
	}
	
	//编写者:张煜明 时间: 2019年7月7日 20时19分
	@Override
	public ArrayList<Forum> showPageForum(ArrayList<Forum> allforum) {//显示不包括回帖的帖子信息
		ArrayList<Forum> exceptReply = allforum;
		int replyNumber = -1;//记录回帖数
		for(int i = 0 ; i<allforum.size(); i++) {
			String sql = "select * from replyForum where replyForumId= ?";
			String[] param = {String.valueOf(allforum.get(i).getId())};
			if(new replyForumDaoImpl().selectReply(sql, param).size()!=0){//该帖子是某个回帖
				exceptReply.remove(i);//指向同一块区域,因此exceptReply和allforum的forum都将被移除
				i--;
				continue;
			}
			System.out.print("序号"+(i+1)+":投稿人id:"+allforum.get(i).getUserId()+"\n帖子标题:"+allforum.get(i).getTitle()+"  \t编辑日期:"+allforum.get(i).getEditDate());
			if(allforum.get(i).isHasReplyForum()) {//该帖有回帖
				Stack<Forum> forumStack = new Stack<Forum>();//该Stack存放包括原帖在内的所有回帖
				forumStack.push(allforum.get(i));//将原帖推进栈中
				while(forumStack.size()!=0) {//当栈不为空时,进行深度优先遍历
					Forum temp = forumStack.pop();//弹栈操作
					replyNumber++;//回帖数+1
					sql = "select * from replyForum where originForumId= ?";//原帖ID是该帖ID的回复组取出
			    	String[] replyForumParam = {String.valueOf(temp.getId())};//为sql语句预设置参数
			    	replyForumDao replyforumDao = new replyForumDaoImpl();
			    	ArrayList<replyForum> r = replyforumDao.selectReply(sql, replyForumParam);//返回帖的所有回帖
			    	for(int j = 0 ; j<r.size() ; j++) {//对于所有的回帖
			    		sql = "select * from forum where id = ?  order by editDate, priority";
			    		replyForumParam[0] = String.valueOf(r.get(j).getReplyForumId());
			    		forumStack.push(new ForumDaoImpl().selectForum(sql, replyForumParam).get(0));//找到该帖并置于栈中
			    	}
				}
				System.out.println("(该帖的总回帖数为"+replyNumber+")");
			    replyNumber = -1;//记录回帖数重置
			}
			else
				System.out.print("\n");
		}
		return exceptReply;
	}

	//编写者:张煜明 时间: 2019年7月5日 20时32分
	@Override
	public void showUserForum(ArrayList<Forum> allforum) {//显示用户的全部帖子信息
		for(int i = 0 ; i<allforum.size() ; i++) {
			int replyNumber = -1;//记录回帖数
			System.out.print("序号"+(i+1)+":投稿人id:"+allforum.get(i).getUserId()+"\n帖子标题:"+allforum.get(i).getTitle()+"  \t编辑日期:"+allforum.get(i).getEditDate());
			if(allforum.get(i).isHasReplyForum()) {
				Stack<Forum> forumStack = new Stack<Forum>();//该Stack存放包括原帖在内的所有回帖
				forumStack.push(allforum.get(i));//将原帖推进栈中
				while(forumStack.size()!=0) {//当栈不为空时,进行深度优先遍历
					Forum temp = forumStack.pop();//弹栈操作
					replyNumber++;//回帖数+1
					String sql = "select * from replyForum where originForumId= ?";//原帖ID是该帖ID的回复组取出
			    	String[] replyForumParam = {String.valueOf(temp.getId())};//为sql语句预设置参数
			    	replyForumDao replyforumDao = new replyForumDaoImpl();
			    	ArrayList<replyForum> r = replyforumDao.selectReply(sql, replyForumParam);//返回帖的所有回帖
			    	for(int j = 0 ; j<r.size() ; j++) {//对于所有的回帖
			    		sql = "select * from forum where id = ? order by editDate,priority ";
			    		replyForumParam[0] = String.valueOf(r.get(j).getReplyForumId());
			    		forumStack.push(new ForumDaoImpl().selectForum(sql, replyForumParam).get(0));//找到该帖并置于栈中
			    	}
				}
				System.out.println("(该帖的总回帖数为"+replyNumber+")");
			}
			else
				System.out.print("\n");
		}
	}
	
	//编写者:张煜明 时间: 2019年7月7日 14时11分
		public ArrayList<Forum> ReadForum(Forum forum) {//显示原帖及其回帖
			Stack<Forum> forumStack = new Stack<Forum>();//该Stack存放包括原帖在内的所有回帖
			ArrayList<Forum> ForumList = new ArrayList<Forum>();//返回的选择list
			forumStack.push(forum);//将原帖推进栈中
			int height = 0;//层数
			int number = 0;//序号
			ArrayList<Integer> count = new ArrayList<Integer>();//每层帖子的个数
			count.add(1);//根有一个帖子
			while(forumStack.size()!=0) {//当栈不为空时,进行深度优先遍历
				Forum temp = forumStack.pop();//弹栈操作
				ForumList.add(temp);//将该帖加入到返回的list中
				number++;//序号++；
				if(height==0) {//根的内容输出
					System.out.print("序号1:发帖人:"+temp.getUserId()+"\t\t发帖时间:"+temp.getEditDate()+"\n标题:"+temp.getTitle()+"\n"+temp.getContent());
				}
				else {//叶的内容输出
					String symbol="";
					count.set(count.size()-1,count.get(count.size()-1)-1);//该层含帖子的数量-1
	 				for(int i = 0 ; i< height ; i++) {//分清层次关系
						System.out.print("\t");
					}
					System.out.println("序号"+number+":发帖人:"+temp.getUserId()+"\t\t发帖时间:"+temp.getEditDate());
					for(int i = 0 ; i< height ; i++) {//分清层次关系
						System.out.print("\t");
						symbol+="\t";
					}
					int index = temp.getContent().lastIndexOf('\n');//找到该字符串的最后一个\n
					if(index==-1) {
						System.out.println(temp.getContent());
					}
					else {
						System.out.println(temp.getContent().substring(0,index).replaceAll("\n","\n"+symbol));//打印帖子内容,不打印最后的\n
					}
				}
				if(temp.isHasReplyForum()==true) {//该帖有回帖
					height++;
			    	String sql = "select * from replyForum where originForumId= ?";//原帖ID是该帖ID的回复组取出
			    	String[] replyForumParam = {String.valueOf(temp.getId())};//为sql语句预设置参数
			    	replyForumDao replyforumDao = new replyForumDaoImpl();
			    	ArrayList<replyForum> r = replyforumDao.selectReply(sql, replyForumParam);//返回帖的所有回帖
				    count.add(r.size());//count添加该层当前帖子的个数
			    	for(int i = 0 ; i<r.size() ; i++) {//对于所有的回帖
			    		sql = "select * from forum where id = ? order by editDate,priority ";
			    		replyForumParam[0] = String.valueOf(r.get(i).getReplyForumId());
			    		forumStack.push(new ForumDaoImpl().selectForum(sql, replyForumParam).get(0));//置于replyForum这个list中等待显示
			    	}
			    }
				while(count.get(count.size()-1)==0) {//当前叶下无任何回帖的时候
					count.remove(count.size()-1);//当前高度下已经无任何帖子了,移除该高度的帖子数后，最后的数变为上一层的帖子数
					height--;//高度-1
				}
			}
			return ForumList;
		}
	
}
