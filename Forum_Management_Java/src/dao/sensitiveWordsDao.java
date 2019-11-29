package dao;

import java.util.ArrayList;
//编写者:苗奇 时间:2019年7月6日 11时40分
public interface sensitiveWordsDao {//对屏蔽词进行交互的数据库接口
	
	public abstract ArrayList<String> getsensitiveWords();//返回所有屏蔽词
	
	public abstract ArrayList<String> selectWord(String sql, String[] param);//找到某些特定的举报信息

	public abstract int updatesensitiveWords(String sql, Object[] param);//更新屏蔽词信息

}
