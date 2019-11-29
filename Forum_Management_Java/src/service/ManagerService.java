package service;

import entity.Page;
//编写者:张越 时间: 2019年7月5日 20时21分
public interface ManagerService extends UserService{

		public void modifyPage(String PageName);//修改版面
		
		public void addPage();//添加版面
		
		public void deletePage();//删除版面
		
		public void blackUser();//拉黑用户
		
		public void UserLevelUp();//设置某用户为管理员
		
		public void Top(Page page);//置顶某版面内的某个帖子
}
