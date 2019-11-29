package dao;

import java.util.ArrayList;
import entity.Page;
//编写者:苗奇 时间:2019年7月6日 10时48分
public interface PageDao {//与版面数据库交互的接口
	
	public abstract ArrayList<Page> getAllPage();//列出所有版块
	
	public abstract ArrayList<Page> selectPage(String sql, String[] param);//根据版面的信息找到满足条件的版面

	public abstract int updatePage(String sql, Object[] param);//更新版面的信息
}
