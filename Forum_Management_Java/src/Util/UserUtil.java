package Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import dao.sensitiveWordsDao;
import dao.impl.ForumDaoImpl;
import dao.impl.sensitiveWordsDaoImpl;

public class UserUtil {//工具类
	
	//编写者:张越， 时间:2019年7月17日 13时29分
	public ArrayList<String> announceable(String content) {//返回一个帖子中带有的所有屏蔽字
		sensitiveWordsDao sd = new sensitiveWordsDaoImpl();
		ArrayList<String> sw = sd.getsensitiveWords(); //list内包含数据库内设定的所有的敏感词
		ArrayList<String> contain = new ArrayList<String>();//添加一个帖子中的所有屏蔽字至contain对象中
		for(int i = 0 ; i < sw.size() ; i++) {
			//indexOf从头开始检索子串在该字符串中的位置，不存在返回-1
			if(content.indexOf(sw.get(i))!=-1) {//存在该敏感词
				contain.add(sw.get(i));//添加进contain
			}
		}
		return contain;
	}
	
	//编写者:张越， 时间:2019年7月17日 13时54分
	public void addSensitiveWords() {//添加屏蔽词
		Scanner input = new Scanner(System.in);
		System.out.println("请输入新增加的屏蔽字:");
		String sWord ="";
		do {
			sWord = input.nextLine().trim();//去除首尾空白字符
		}while(sWord.length()==0);
		String sql = "select * from  SensitiveWords where sWords= ? ";//表中查看是否含有输入屏蔽字
		String [] param = {sWord};
		sensitiveWordsDao sw = new sensitiveWordsDaoImpl();
		if(sw.selectWord(sql, param).size()!=0) {
			System.out.println("该屏蔽词已存在与库中，无需添加");
		}
		else {
			sql = "insert into SensitiveWords(sWords) values (?)";
			sw.updatesensitiveWords(sql, param);//在数据库中插入该敏感词
			System.out.println("添加完毕,当前所有的敏感词如下:");
			ArrayList<String> words = sw.getsensitiveWords();
			for(int i = 0 ;i < words.size() ; i++) {//列出当前表内的所有敏感词
				System.out.print(words.get(i)+"\t");
				if(i%4==0&&i!=0) {//每五个换一行
					System.out.print("\n");
				}
			}
			System.out.println();
		}
	}

	//编写者:张越， 时间:2019年7月17日 16时23分
	public void delSensitiveWords() {//删除屏蔽词
		sensitiveWordsDao sw = new sensitiveWordsDaoImpl();
		ArrayList<String> words = sw.getsensitiveWords();//获取当前表内的所有屏蔽词信息
		System.out.println("当前所有的敏感词如下:");
		for(int i = 0 ;i < words.size() ; i++) {//列出当前表内的所有敏感词
			System.out.print(words.get(i)+"\t");
			if(i%4==0&&i!=0) {
				System.out.print("\n");
			}
		}
		Scanner input = new Scanner(System.in);
		System.out.println("\n请输入您想删除的敏感词:");
		String del = "";
		do {
			del = input.nextLine().trim();//去除首尾空白字符
		}while(del.length()==0);
		if(words.contains(del)) {//如果当前list中含有改词,则进行删除操作
			String sql = "delete from SensitiveWords where sWords= ? ";
			String[] param = {del};
			sw.updatesensitiveWords(sql, param);//对敏感词的库进行更新操作
			System.out.println("删除完毕,当前所有的敏感词如下:");
			words.remove(del);
			for(int i = 0 ;i < words.size() ; i++) {//列出当前list内的所有敏感词
				System.out.print(words.get(i)+"\t");
				if(i%5==0&&i!=0) {
					System.out.print("\n");
				}
			}
			System.out.println();
		}
		else {
			System.out.println("您想删除的敏感词不存在,无需删除");
		}
	}
	
	//编写者:苗奇 时间:2019年7月15日 16时32分
	public long productRandomId() {//生成随机的帖子ID
		 Date date = new Date();//获取当前时间
		 Random ran = new Random();
		 StringBuffer s = new StringBuffer();
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当前时间按该模式存储
		 String newdate = dateFormat.format(date);
		 s.append(newdate.substring(0, 4));//年
		 s.append(newdate.substring(5, 7));//月
		 s.append(newdate.substring(8,10));//日
		 long id = 1;
		 String sql ="select * from forum where id = ?";
		 String[] param = {String.valueOf(id)};
		 for(int i = 1 ; id==0||new ForumDaoImpl().selectForum(sql, param).size()!=0 ; i++ ) {//当表内存在该id时进行循环，直到表内无重复ID终止循环
			 id=(id+i*ran.nextLong())%100000;
			 param[0] = String.valueOf(id);
		 }
		return id;//返回随机生成的无重复的ID
	}

	//编写者:苗奇 时间:2019年7月18日 16时50分
	public String encode(String password) {//对存储的密码进行加密操作,使用凯撒密码加密
		StringBuffer secretP = new StringBuffer();
		char[] temp = password.toCharArray();
		for(int i = 0; i<password.length();i++) {
			if((temp[i]>='a'&&temp[i]<='w')||(temp[i]>='A'&&temp[i]<='W')||(temp[i]>='0'&&temp[i]<='9'))//后移3个字符
				secretP.append((char)(temp[i]+3));//加密后字符添加进StringBuffer中
			else if((temp[i]>='x'&&temp[i]<='z')||(temp[i]>='X'&&temp[i]<='Z')){//对应字母表循环后移3字符
				secretP.append((char)(temp[i]-23));//加密后字符添加进StringBuffer中
			}
			else {
				secretP.append((char)temp[i]);
			}
		}
		return secretP.toString();//存储该字符串
	}

	//编写者:苗奇 时间:2019年7月18日 17时10分
	public String decode(String secret) {//对存储的密码进行解密操作
		StringBuffer secretP = new StringBuffer();
		char[] temp = secret.toCharArray();
		for(int i = 0; i<secret.length();i++) {
			if((temp[i]>='d'&&temp[i]<='z')||(temp[i]>='D'&&temp[i]<='Z')||(temp[i]>='3'&&temp[i]<='<'))//后移3个字符
				secretP.append((char)(temp[i]-3));//解密后字符添加进StringBuffer中
			else if((temp[i]>='a'&&temp[i]<='c')||(temp[i]>='A'&&temp[i]<='D')){//对应字母表循环前移3字符
				secretP.append((char)(temp[i]+23));//解密后字符添加进StringBuffer中
			}
			else {
				secretP.append((char)temp[i]);
			}
		}
		return secretP.toString();//存储该字符串
	}
}
