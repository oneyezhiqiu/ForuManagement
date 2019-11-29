package service;

import java.util.ArrayList;

import entity.Forum;
import entity.User;

//编写者:张煜明 时间: 2019年7月5日 20时21分
public interface ForumService {
	
		public void modifyForum(User user);//修改帖子内容
		
		public void postForum(User user,String pagename);//发帖
		
		public void deleteUserForum(User user);//用户本身执行删帖操作
		
		public void deleteForum(Forum forum);//具体删帖的实现
		
		public void replyForum(User user, Forum forum);//回帖
		
		public ArrayList<Forum> showPageForum(ArrayList<Forum> allforum);//显示不包括回帖的帖子信息
		
		public void showUserForum(ArrayList<Forum> allforum);//显示用户的全部帖子信息
		
		public ArrayList<Forum> ReadForum(Forum forum);//显示原帖及其回帖
		
}
