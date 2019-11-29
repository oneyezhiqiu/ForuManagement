package service.impl;

import java.util.ArrayList;
import java.util.Scanner;

import Util.UserUtil;
import entity.Forum;
import entity.Page;
import service.ForumService;
import service.PageService;
import dao.ForumDao;
import dao.PageDao;
import dao.impl.ForumDaoImpl;
import dao.impl.PageDaoImpl;
//编写者:林可葳 时间: 2019年7月6日 13时44分
public class PageServiceImpl implements PageService{
	
	//编写者:林可葳 时间: 2019年7月6日 13时44分
	@Override
	public ArrayList<Forum> getAllForum(String PageName) {//根据选择的版面信息列出当前版面内的所有帖子
		ForumDao forumDao= new ForumDaoImpl();
		String sql = "select * from forum where Pagename = ? order by priority desc,editDate desc";
		String[] param = {PageName};
		ArrayList<Forum> includeForum=forumDao.selectForum(sql, param);
		return includeForum;
	}

	//编写者:林可葳 时间: 2019年7月6日 14时03分
	@Override
	public void modifyPage(String PageName) {//修改某个版面,当前只有版面名称这一个属性可以修改
		Scanner input = new Scanner(System.in);
		String newPageName="";
		String sql = "select * from page where name = ?";
		String[] param = {newPageName};
		PageDao pageDao = new PageDaoImpl();
		boolean flag = true;//判断是否输入合法
		System.out.println("输入修改后的版面名称:(输入0退出)");
		do {
			do {
				newPageName = input.nextLine().trim();
			}while(newPageName.length()==0);
			if(newPageName.equals("0")) {
				System.out.println("用户选择退出,自动返回主服务界面");
				return;
			}
			if(newPageName.length()>100) {
				System.out.println("修改版面名长度不合理,请重新输入(输入0返回主界面):");
	    		continue;
			}
			if(new UserUtil().announceable(newPageName).size()!=0) {//调用工具类中的返回所含敏感词list的方法
	    		System.out.println("修改版面名中含敏感词,请重新输入(输入0返回主界面):");
	    		continue;
	    	}
			param[0]=newPageName;
			if(pageDao.selectPage(sql, param).size()!=0)//数据库中含名称为输入名的版面
				System.out.println("版面名称重复,请重新输入修改后的版面名称(输入0退出):");
			else 
				flag=false;
		}while(flag);
		sql = "update page set name = ? where name = ?";
		String[] updateparam = {newPageName, PageName};
		pageDao.updatePage(sql, updateparam);//更新版面名
		sql = "update forum set Pagename = ? where Pagename =?";
		new ForumDaoImpl().updateForum(sql, updateparam);//将帖子中所有的版面名更改为新设定的版面名称
		System.out.println("修改后所有的版面信息为:");
		showPage(pageDao.getAllPage());//显示所有的版面信息
	}

	//编写者:林可葳 时间: 2019年7月6日 14时43分
	@Override
	public void addPage() {//添加版面
		Scanner input = new Scanner(System.in);
		String newPageName="";
		String sql = "select * from page where name = ?";
		String[] param = {newPageName};
		PageDao pageDao = new PageDaoImpl();
		boolean flag = true;//判断是否输入合法
		System.out.println("输入添加的版面名称(注意，版面名称不区分大小写,输入0返回主界面):");
		do {
			do {
				newPageName = input.nextLine().trim();
			}while(newPageName.length()==0);
			if(newPageName.equals("0")) {
				System.out.println("用户选择退出,自动返回主服务界面");
				return;
			}
			if(newPageName.length()>100) {
				System.out.println("版面名长度不合理,请重新输入(输入0返回主界面):");
	    		continue;
			}
			else if(new UserUtil().announceable(newPageName).size()!=0) {//调用工具类中的返回所含敏感词list的方法
	    		System.out.println("修改版面名中含敏感词,请重新输入(输入0返回主界面):");
	    		continue;
	    	}
			param[0]=newPageName;
			if(pageDao.selectPage(sql, param).size()!=0)
				System.out.println("版面名称重复,请重新输入想添加的版面名称(输入0返回主界面):");
			else 
				flag=false;
		}while(flag);
		sql = "insert into page(name,ForumNumber) values(?,?)";
		Object[] addparam = {newPageName,0};
		pageDao.updatePage(sql, addparam);
		System.out.println("增加版面后,所有的版面信息为:");
		showPage(pageDao.getAllPage());//显示所有的版面信息
	}

	//编写者:林可葳 时间: 2019年7月6日 15时44分
	@Override
	public void deletePage() {//删除版面
		Scanner input = new Scanner (System.in);
		PageDao pageDao = new PageDaoImpl();
		ArrayList<Page> allPage = pageDao.getAllPage();//返回所有的版面信息存储在list中
		if(allPage.size()==0) {//版面无任何内容,无法继续删除操作
			System.out.println("当前无任何版面,请先创建版面");
			return;
		}
		else if(allPage.size()==1) {//版面至少要保留一个版块
			System.out.println("该论坛需要至少保留一个版块,无法继续进行删除操作");
			return;
		}
		System.out.println("现在所有版面信息如下");
		showPage(allPage);//显示所有的版面信息
		do {
			System.out.println("选择你想删除的版面序号:(输入0取消本次操作)");
			String choose ="";
			do {
				choose = input.nextLine().trim();
			}while(choose.length()==0);
			try {//用户输入异常
				Integer.parseInt(choose);
				if(Integer.parseInt(choose)%1!=0) 
					throw new Exception();
			}catch(Exception e) {
				System.out.print("输入值非法,请重新");
				continue;
			}
			if(Integer.parseInt(choose)<0||Integer.parseInt(choose)>allPage.size()) {
				System.out.print("输入版面序号不存在,请重新");
			}
			else if(Integer.parseInt(choose)==0) {
				System.out.println("用户选择退出,返回上一界面");
				break;
			}
			else {
				String sql = "delete from page where name= ? ";
				String[] pageParam = {allPage.get(Integer.parseInt(choose)-1).getName()};
				pageDao.updatePage(sql, pageParam);	//删除该版块
				ForumDao forumDao = new ForumDaoImpl();
				ArrayList<Forum> forumList = getAllForum(allPage.get(Integer.parseInt(choose)-1).getName());//返回该板块内的所有帖子信息
				ForumService forumService = new ForumServiceImpl();
				for(int i = 0 ; i < forumList.size() ; i++) {
					forumService.deleteForum(forumList.get(i));//删除该板块内的所有帖子
				}
				System.out.println("修改成功,现在所有版面信息如下");
				showPage(pageDao.getAllPage());
			}
		}while(true);
	}

	//编写者:林可葳 时间: 2019年7月6日 16时23分
	@Override
	public void showPage(ArrayList<Page> allPage) {//显示所有的版面信息
		System.out.println("序号\t版面名称\t\t所含帖子数量");
		for(int i = 0 ;i<allPage.size(); i++)
			System.out.println((i+1)+"\t"+allPage.get(i).getName()+"\t\t"+allPage.get(i).getForumNumber());
	}
}
