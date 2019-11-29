package service;

import entity.Forum;
import entity.Page;
import java.util.*;

//编写者:林可葳 时间: 2019年7月5日 20时21分
public interface PageService {
	
	//根据选择的版面信息列出当前版面内的所有帖子
	public ArrayList<Forum> getAllForum(String PageName);

	public void modifyPage(String PageName);//修改某个版面
	
	public void addPage();//添加版面
	
	public void deletePage();//删除版面
	
	public void showPage(ArrayList<Page> allPage);//显示帖子信息
}
