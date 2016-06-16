package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/***
 * @author xinge1023@163.com
 * 数据库连接类  
 */
public class DB implements SystemVariables{
	//登录名、密码等私有信息
    private static String username = "root";
    private static String password = "123456";
    private static Connection conn = null;
    static {
        try {
            Class.forName(Sql_Type);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * DB单例模式
     */
    private static DB DB;
    public static DB getDB() {
        if (DB == null) {
        	DB = new DB();
        }
        return DB;
    }

    /**
     * 获得Connection连接
     */
    public static Connection getConnection() {
        Connection conn = null;
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(dataBaseUrl, username, password);
            } catch (SQLException e) {
                if(e.getMessage().contains("Unknown database")){
                	System.out.println("#准备创建数据库"+DatebaseName);
                	try {
                		conn = DriverManager.getConnection(baseDataBaseUrl, username, password);
						PreparedStatement ps = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS javaauto DEFAULT CHARSET utf8 COLLATE utf8_general_ci");
						ps.executeUpdate();
						System.out.println("#创建数据库"+DatebaseName+"创建完成.");
                	} catch (SQLException e1) {
						//e1.printStackTrace();
						System.err.println("自动创建数据库语法出错,请手动创建空数据库:"+DatebaseName);
					}
                }
            }
        }

        return conn;
    }
    /**
     * 关闭连接
     */ 
    public static void closeConnection() {
       
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
