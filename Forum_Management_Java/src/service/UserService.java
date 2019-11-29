package service;

import entity.Forum;
import entity.User;
import java.util.*;

//编写者:徐越 时间: 2019年7月5日 20时21分
public interface UserService {
	
	public ArrayList<Forum> getUserForum(long id);//获取某用户的全部帖子
	
	public void modifySelfInformation(User user);//修改自己的信息
	
	public User editorOfForum(Forum forum);//根据帖子找用户
	
	public void accuseUser(User user);//举报某用户
}
