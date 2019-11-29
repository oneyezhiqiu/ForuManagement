package dao.impl;
import java.util.*;
import java.sql.*;
import dao.BaseDao;
import dao.sensitiveWordsDao;
//编写者:苗奇 时间:2019年7月6日 15时31分
public class sensitiveWordsDaoImpl extends BaseDao implements sensitiveWordsDao{

	private Connection conn = null; // 保存数据库连接

	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private ResultSet rs = null; // 用户保存查询结果集
	
	@Override
	public ArrayList<String> getsensitiveWords() {
		ArrayList<String> sw = new ArrayList<String>();//设置String类的list保存数据库内所有的屏蔽字
		try {
			conn = getConn(); // 得到数据库连接
			String sql ="select sWords from SensitiveWords";
			pstmt = conn.prepareStatement(sql); // 得到PreparedStatement对象
			rs = pstmt.executeQuery(); // 执行SQL语句
			while(rs.next()) {
				sw.add(rs.getString(1));
			}	
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return sw;
	}

	@Override
	public ArrayList<String> selectWord(String sql, String[] param) {
		ArrayList<String> wList = new ArrayList<String>();
		try {
			conn = getConn();//建立连接
			pstmt = conn.prepareStatement(sql);//得到prepareStatement的对象
			if(param!=null) {
				for (int i = 0; i < param.length; i++) {
					pstmt.setString(i + 1, param[i]); // 为预编译sql设置参数
				}
			}
			rs= pstmt.executeQuery();//执行sql语句
			while(rs.next()) {
				wList.add(rs.getString(1));
			}	
		}catch (SQLException e) {//处理SQLException异常
			e.printStackTrace();
		} catch (ClassNotFoundException e) {//处理ClassNotFoundException异常
			e.printStackTrace();
		} finally {//关闭数据库连接
			super.closeAll(conn, pstmt, rs);
		}
		return wList;
	}
	
	@Override
	public int updatesensitiveWords(String sql, Object[] param) {//更新屏蔽字信息
		return super.executeSQL(sql, param);
	}


}
