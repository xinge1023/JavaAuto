package util;


/***
 * @author xinge1023@163.com
 * 提供系统全局变量的接口
 */
public interface SystemVariables {
    //项目名称
    public final static String ProjectName = System.getProperty("user.dir").substring(System.getProperty("user.dir").lastIndexOf("\\")+1, System.getProperty("user.dir").length());
	//系统路径
    public final static String SystemPath = System.getProperty("user.dir");
	//util(工具类)的包名
    public final static String Util_Package = "util";
	//util(工具类)包的绝对路径
    public final static String Util_AbsPath = SystemPath+"\\src\\"+Util_Package;
	//entity(实体类)的包名
    public final static String Entity_Package = "entity";
    //entity(实体类)包的绝对路径
    public final static String Entity_AbsPath = SystemPath+"\\src\\"+Entity_Package;
    //对象管理器的类名
    public final static String BeanManager_FullPackage = "util.BeanManager";
    //数据库名
    public final static String DatebaseName ="JavaAuto";
    //基本数据库地址(mysql)
    public final static String baseDataBaseUrl ="jdbc:mysql://localhost:3306/";
    //数据库地址(mysql)
    public final static String dataBaseUrl =baseDataBaseUrl+DatebaseName+"?useUnicode=true&characterEncoding=utf-8";
    //数据库类别(mysql)
    public final static String Sql_Type = "com.mysql.jdbc.Driver";
    
    /************************************************************
    //基本数据库地址(sqlserver)
    public final static String baseDataBaseUrl ="jdbc:microsoft:sqlserver://localhost:1433";
    //数据库地址(sqlserver)
    public final static String dataBaseUrl =baseDataBaseUrl+";DatabaseName="+DatebaseName;
    //数据库类别(sqlserver)
    public final static String Sql_Type = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    
    //基本数据库地址(oracle)
    public final static String baseDataBaseUrl ="jdbc:oracle:thin:@loaclhost:1521";
    //数据库地址(oracle)
    public final static String dataBaseUrl =baseDataBaseUrl+":"+DatebaseName;
    //数据库类别(oracle)
    public final static String Sql_Type = "oracle.jdbc.driver.OracleDriver";
    *************************************************************/
    
}
