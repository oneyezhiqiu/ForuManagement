package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库操作基类
 */
public class BaseDao {
	public static String DRIVER; // 数据库驱动字符串

	public static String URL ; // 连接url字符串

	public static String UserName; // 数据库用户名

	public static String Password; // 用户密码
	
	Connection conn = null;// 数据连接对象
	
	static{//静态代码块,在类加载的时候执行
		init();
	}
	
	/**
	 * 初始化连接参数,从配置文件里获得
	 */
		public static void init(){
			Properties params=new Properties();
			//String configFile = "databaseForSqlite.properties";//配置文件路径
			String configFile = "database.properties";//配置文件路径
			//加载配置文件到输入流中
			InputStream is=BaseDao.class.getClassLoader().getResourceAsStream(configFile);
			if(is!=null) {
			try {
				//从输入流中读取属性列表
				params.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//根据指定的获取对应的值
			DRIVER=params.getProperty("driver");
			URL=params.getProperty("url");
			UserName=params.getProperty("user");
			Password=params.getProperty("password");
			}else {
				DRIVER="org.sqlite.JDBC";
				URL="jdbc:sqlite:testDB.db";
				UserName="";
				Password="";
			}
		}   
 
		
	public Connection getConn() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			Class.forName(DRIVER); // 注册驱动
			conn = DriverManager.getConnection(URL,  UserName, Password); // 获得数据库连接
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn; // 返回连接
	}

	public void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {

		/* 如果rs不空，关闭rs */
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* 如果pstmt不空，关闭pstmt */
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* 如果conn不空，关闭conn */
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public int executeSQL(String preparedSql, Object[] param) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int num = 0;
		try {
			conn = getConn(); // 得到数据库连接
			pstmt = conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					pstmt.setObject(i + 1, param[i]); // 为预编译sql设置参数
				}
			}
			num = pstmt.executeUpdate(); // 执行SQL语句
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); // 处理ClassNotFoundException异常
		} catch (SQLException e) {
			e.printStackTrace(); // 处理SQLException异常
		} finally {
			this.closeAll(conn, pstmt, null);
		}
		return num;
	}
}