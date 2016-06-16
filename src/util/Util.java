package util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;
/***
 * @author xinge1023@163.com
 * 提供基础方法
 */
public class Util extends Methods implements SystemVariables{
	/**
	 * Util类
	 */
	private static final long serialVersionUID = 1L;
	//数据结果集
	private static ResultSet rs=null;
	//预编译对象ps
	private static PreparedStatement ps=null;
	//对象管理器
	public static Map<String,Object> entityMap = new HashMap<String,Object>();
	//查询条件map
	public static Map<String,Object> map = new HashMap<String,Object>(); 
	//临时list
	public static List<Object> tempList = new ArrayList<Object>(); 
	//方法集
	private static  Method[] methods;
	//用于事务的sql
	private static String TransactionSql="";
	//记录getMaxId中的对象个数
	private static  Map<Object,String> countList= new HashMap<Object,String>(); 
	//初始化所有方法
	public static void initMethod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception{
		setEntity(request,response);
	 }
	//set值转换类并返回对象
	public static Object convertClass(Method getMethod,Object object,String methodName,String value,Object newObject) {
		    //转化类型使之可以被识别 
		    try{
			if(getMethod.toString().contains("int")){Method setMethod=object.getClass().getMethod(methodName, new Class[]{int.class});if(value==null){setMethod.invoke(object, new Object[]{0});}else{setMethod.invoke(object, new Object[]{Integer.valueOf(value)});}}
		    }catch (Exception e) {
				System.out.println("java.lang.NumberFormatException:值'"+value+"'不是Int类型.");
			}
			if(getMethod.toString().contains("java.lang.String")){Method setMethod = null;
			try {
				setMethod = object.getClass().getMethod(methodName, new Class[]{String.class});
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}if(value==null){try {
				setMethod.invoke(object, new Object[]{null});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}}else{try {
				setMethod.invoke(object, new Object[]{String.valueOf(value)});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}}}
			if(getMethod.toString().contains("java.lang.Boolean")){Method setMethod = null;
			try {
				setMethod = object.getClass().getMethod(methodName, new Class[]{Boolean.class});
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}if(value==null){try {
				setMethod.invoke(object, new Object[]{null});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}}else{try {
				setMethod.invoke(object, new Object[]{Boolean.valueOf(value)});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}}}
			try{
			if(getMethod.toString().contains("java.lang.Double")){Method setMethod=object.getClass().getMethod(methodName, new Class[]{Double.class});if(value==null){setMethod.invoke(object, new Object[]{0.0});}else{setMethod.invoke(object, new Object[]{Double.valueOf(value)});}}
			}catch (Exception e) {
				System.out.println("java.lang.NumberFormatException::值'"+value+"'不是Double类型.");
			}
			try{
			if(getMethod.toString().contains("java.lang.Float")){Method setMethod=object.getClass().getMethod(methodName, new Class[]{Float.class});if(value==null){setMethod.invoke(object, new Object[]{0});}else{setMethod.invoke(object, new Object[]{Float.valueOf(value)});}}
			}catch (Exception e) {
				System.out.println("java.lang.NumberFormatException:值'"+value+"'不是Float类型.");
			}
			try{
			if(getMethod.toString().contains("java.lang.Byte")){Method setMethod=object.getClass().getMethod(methodName, new Class[]{Byte.class});if(value==null){setMethod.invoke(object, new Object[]{0});}else{setMethod.invoke(object, new Object[]{Byte.valueOf(value)});}}
			}catch (Exception e) {
				System.out.println("java.lang.NumberFormatException:值'"+value+"'不是Byte类型.");
			}
			try{
			if(getMethod.toString().contains("java.lang.Long")){Method setMethod=object.getClass().getMethod(methodName, new Class[]{Long.class});if(value==null){setMethod.invoke(object, new Object[]{null});}else{setMethod.invoke(object, new Object[]{Long.valueOf(value)});}}
			}catch (Exception e) {
				System.out.println("java.lang.NumberFormatException:值'"+value+"'不是Long类型.");
			}
			try{
				if(getMethod.toString().contains("java.math.BigDecimal")){Method setMethod=object.getClass().getMethod(methodName, new Class[]{BigDecimal.class});if(value==null){setMethod.invoke(object, new Object[]{null});}else{setMethod.invoke(object, new Object[]{new BigDecimal(value)});}}
				}catch (Exception e) {
				System.out.println("java.lang.NumberFormatException:值'"+value+"'不是BigDecimal类型.");
			}
			//格式化Date类型 
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if(getMethod.toString().contains("java.util.Date")){Method setMethod = null;
			try {
				setMethod = object.getClass().getMethod(methodName, new Class[]{java.util.Date.class});
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}try {
				setMethod.invoke(object, new Object[]{format.parse(value)});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}}
			try{
			if(getMethod.toString().contains("java.sql.Date")){if(value==null){}else{Method setMethod=object.getClass().getMethod(methodName, new Class[]{java.sql.Date.class});setMethod.invoke(object, new Object[]{java.sql.Date.valueOf(value)});}}
			}catch (Exception e) {
				System.out.println("java.lang.IllegalArgumentException:值'"+value+"'不是标准的Date类型.");
				e.printStackTrace();
			}
			return object;
	}
	
	//填充Entity
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static Object setEntity(HttpServletRequest request, HttpServletResponse response) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, ParseException, InstantiationException, ClassNotFoundException{
		 Enumeration req = request.getParameterNames();
		  req = request.getParameterNames();
		  try{
		  Class.forName(BeanManager_FullPackage);
		  }catch (Exception e){
			  System.err.println("类加载器找不到对象"+BeanManager_FullPackage+",请先创建BeanManager.java.");
			  return null;
		  }
		 while (req.hasMoreElements()) {
		     Object obj = (Object) req.nextElement();
		     if(obj.toString().indexOf('.')!=-1){  //判断是否为domain类型,也就是对象.属性。
		    	 //通过前台key得到值，值为数组类型
		    	 String[] strs = request.getParameterValues(obj.toString());
		    	 //把值的格式转成和前台一样的utf-8，防止乱码
		    	 String value = new String(strs[strs.length-1].getBytes("iso8859-1"),"UTF-8");
		    	 String[] keys = obj.toString().split("\\.");
		    	 int len = obj.toString().split("\\.").length;
		    	 Object parentObject = null;  //父对象
		    	 Object childObject = null;   //子对象
		    	 for(int i=len;i>1;i--){
		    		 Method getMethod=null;
		    		 Method setMethod=null;
		    		 String cName = null;
		    		 String propertyName = null;
		    		 //获得对象名
		    		 cName  = obj.toString().split("\\.")[len-i];
		    		 //得到大写的对象名
	    			 cName = cName.replaceFirst(cName.substring(0, 1),cName.substring(0, 1).toUpperCase()) ;
	    			//获得属性名
		    		 propertyName  = obj.toString().split("\\.")[len-i+1];
		    		 //得到大写的属性名
			    	 propertyName  = propertyName .replaceFirst(propertyName .substring(0, 1),propertyName .substring(0, 1).toUpperCase()) ;	 
			    	 if(len==2){
			    		 getMethod=Class.forName(BeanManager_FullPackage).getMethod("get"+cName ,new Class[]{});
			    		 parentObject = getMethod.invoke(Class.forName(BeanManager_FullPackage));
			    		 getMethod=parentObject.getClass().getMethod("get"+propertyName ,new Class[]{});
			    		 convertClass(getMethod, parentObject,"set"+propertyName,value, null);
			    	 }else if(i==2){
			    		 getMethod=childObject.getClass().getMethod("get"+propertyName ,new Class[]{});
			    		 convertClass(getMethod, childObject,"set"+propertyName,value, new Class[]{});
			    	 }
			    	 else{
			    		 String key = "";
			    		 if(parentObject==null){
			    		 getMethod=Class.forName(BeanManager_FullPackage).getMethod("get"+cName ,new Class[]{});
			    		 //get方法得到的对象赋值给parentObject
			    		 parentObject = getMethod.invoke(Class.forName(BeanManager_FullPackage));
			    		 }else{
			    			 parentObject = childObject;
			    		 }
			    		 for(int p=0;p<len-i+2;p++){
					    		key = key + keys[p]+".";
					    	 }
			    		 if(entityMap.containsKey(key)){
			    			 childObject = entityMap.get(key);
			    		 }else{
			    		 //新建对象childObject
			    		 childObject = Class.forName(Entity_Package+"."+propertyName).newInstance();
			    		 setMethod=parentObject.getClass().getMethod("set"+propertyName, new Class[]{childObject.getClass()});
				    	 //对象的设值
				    	 setMethod.invoke(parentObject, new Object[]{childObject});
			    		 }
				    	 if(!entityMap.containsKey(key)){
				    	     entityMap.put(key, childObject);
			    	      }
			    	 }
			    	 
		    	 }
		    	 }
		 }
		 return Class.forName(BeanManager_FullPackage);
	}

	
//添加一条数据(返回sql语句)
@SuppressWarnings("unchecked")
public static String add(Object object) throws SQLException {
	Object childObject = null;
	String sql = "insert into ";
	Class c = object.getClass();
    methods = c.getMethods();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	//把key首字母变成小写
	tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
	//把表名拼接到sql
	sql += tableName + "("; 
	//kList装key(属性)
	List<String> kList = new ArrayList<String>(); 
	//vList装value(值)
	List vList = new ArrayList(); 
	for (Method method : methods) { 
	//循环获取对象中的方法名,也就是get,set方法
	String mName = method.getName(); 
	//判断，去掉对象中默认存在的getClass方法
	if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
	//截取key的名字
	String fieldName = mName.substring(3, mName.length()); 
	//把key首字母变成小写
	fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
	if(method.getReturnType().toString().contains("class "+Entity_Package)){fieldName = fieldName+"_id";}
	//把key装入kList,以便之后的循环
	kList.add(fieldName); 
	try { 
	//value返回的是key的类型
	Object value = method.invoke(object); 
	if("id".equals(fieldName)){
		if(Integer.valueOf(String.valueOf(value))==0){
			value = getMaxId(object)+1;
		 }
		}
	if (value instanceof String || value instanceof java.util.Date || value instanceof java.sql.Date) { 
	//把value装入vList,如果是String类型则加上引号
	vList.add("\"" + value + "\""); 
	} else { 
	if(method.getReturnType().toString().contains("class "+Entity_Package)){
		 childObject = value ;
		if(childObject != null){
	    c = childObject.getClass();
	    methods = c.getMethods();
	    for (Method childmethod : methods) {
	     if(childmethod.toString().contains("getId()")){
	    	 value = Integer.valueOf(String.valueOf(childmethod.invoke(childObject))); 
	    		if(countList.size()!=0){
	    			if(countList.containsKey(object.getClass())){
	    				countList.put(object.getClass(), String.valueOf(Integer.valueOf(countList.get(object.getClass()))+1));
	    				value = getMaxId(childObject)+1;
	   	    		    Method getMethod=childObject.getClass().getMethod("getId" ,new Class[]{});
	   	    		    convertClass(getMethod, childObject,"setId",String.valueOf(value), null);	
	    			}
	    		}
//	    	 if(Integer.valueOf(String.valueOf(value))==0){
//	    		 //设置id属性为表中最大id后给对象赋值
//	    		 value = getMaxId(childObject)+1;
//	    		 Method getMethod=childObject.getClass().getMethod("getId" ,new Class[]{});
//	    		 convertClass(getMethod, childObject,"setId",String.valueOf(value), null);
//	    	 }
	     }
	    }
	   addChild(childObject);  
		}
		}
	vList.add(value);
	}
	} catch (Exception e) { 
	e.printStackTrace();
	}
	}
	}
	//循环出key和value拼接sql
for (int i = 0; i < kList.size(); i++) { 
	if (i < kList.size() - 1) { 
	sql += kList.get(i) + ","; 
	} else { 
	sql += kList.get(i) + ") values(";
	}
	}
 for (int i = 0; i < vList.size(); i++) { 
	if (i < vList.size() - 1) { 
	sql += vList.get(i) + ","; 
	} else { 
	sql += vList.get(i) + ")";
	}
	}
    sql+=";";
    TransactionSql+=sql;
    Connection conn = DB.getConnection();
    conn.setAutoCommit(false);
    try{
    	int count = 0;
    	for(int i=0;i<TransactionSql.split(";").length;i++){
    		//分割并执行insert语句
        	ps = conn.prepareStatement((TransactionSql.split(";"))[i]);
            ps.executeUpdate();
            ps=conn.prepareStatement("select row_count();");
            rs = ps.executeQuery();
            if(rs.next()){count+=rs.getInt(1);}
        	}
    conn.commit();
    System.out.print("添加"+object.getClass().getSimpleName()+"执行事务成功的sql");
    System.out.println("(影响的数据行共"+count+"行):");
    System.out.println(TransactionSql);
    } catch (Exception e) {
	            	conn.rollback();
	                System.err.println("添加发生错误,执行回滚.");
	                System.err.println("发生错误的语句:\n"+TransactionSql);
	                e.printStackTrace();
    }
    conn.setAutoCommit(true);
    ps.close();
    conn.close();
    countList.clear();
    TransactionSql="";
    return sql;
}

//修改一条数据(返回sql语句)
@SuppressWarnings("unchecked")
public static String update(Object object) throws SQLException, SecurityException, NoSuchMethodException, NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
	DB.getConnection().setAutoCommit(false);
	Object childObject = null;
	String sql = "update ";
	Class c = object.getClass();
    methods = c.getMethods();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	//把表名拼接到sql
	sql += tableName + " set "; 
	//kList装key(属性)
	List<String> kList = new ArrayList<String>(); 
	//vList装value(值)
	List vList = new ArrayList(); 
	for (Method method : methods) { 
	//循环获取对象中的方法名,也就是get,set方法
	String mName = method.getName(); 
	//判断，去掉对象中默认存在的getClass方法
	if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
	//截取key的名字
	String fieldName = mName.substring(3, mName.length()); 
	//把key首字母变成小写
	fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
	if(method.getReturnType().toString().contains("class "+Entity_Package)){fieldName = fieldName+"_id";}
	//把key装入kList,以便之后的循环
	kList.add(fieldName); 
	try { 
	//value返回的是key的类型
	Object value = method.invoke(object); 
	if (value instanceof String || value instanceof java.util.Date || value instanceof java.sql.Date) { 
	//把value装入vList,如果是String类型则加上引号
	vList.add("\"" + value + "\""); 
	} else { 
	if(method.getReturnType().toString().contains("class "+Entity_Package)){
		 childObject = value ;
		if(childObject != null){
	    c = childObject.getClass();
	    methods = c.getMethods();
	    for (Method childmethod : methods) {
	     if(childmethod.toString().contains("getId()")){
	    	 value = Integer.valueOf(String.valueOf(childmethod.invoke(childObject))); 
	    	 }
	    }
	   updateChild(childObject);  
		}
	}
	vList.add(value);
	}
	} catch (Exception e) { 
	e.printStackTrace();
	}
	}
	}
	Method getMethod=object.getClass().getMethod("getId" ,new Class[]{});
	int id = Integer.valueOf(String.valueOf(getMethod.invoke(object)));
	if(id==0){id = -1;}
	//循环出key和value拼接sql
    for (int i = 0; i < kList.size(); i++) { 
	if (i < kList.size() - 1) { 
	sql += kList.get(i) + " = "+vList.get(i)+","; 
	} else { 
	sql += kList.get(i) + " = "+vList.get(i)+" where id = "+id;
	}
	}
    sql+=";";
    TransactionSql+=sql;
    Connection conn = DB.getConnection();
    conn.setAutoCommit(false);
    try{
    	int count = 0;
    	for(int i=0;i<TransactionSql.split(";\n").length;i++){
        	ps = conn.prepareStatement((TransactionSql.split(";\n"))[i]);
            ps.executeUpdate();
            ps=conn.prepareStatement("select row_count();");
            rs = ps.executeQuery();
            if(rs.next()){count+=rs.getInt(1);}
        	}
    conn.commit();
    System.out.print("修改"+object.getClass().getSimpleName()+"执行事务成功的sql");
    System.out.println("(影响的数据行共"+count+"行):");
    System.out.println(TransactionSql);
    } catch (Exception e) {
	            	conn.rollback();
	                System.err.println("修改发生错误,执行回滚.");
	                System.err.println("发生错误的语句:\n"+TransactionSql);
	                e.printStackTrace();
    }
    conn.setAutoCommit(true);
    ps.close();
    conn.close();
    countList.clear();
    TransactionSql="";
    return sql;
	}


//查询数据返回List对象(可以查询单表任意n个字段的值,通过需要设定查询条件Map修改sql)
@SuppressWarnings("unchecked")
public static List getEntityList(Object object, Map map) throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException {
	List list =new ArrayList();
	Class c = object.getClass();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	try {
		//ps = DB.getConnection().prepareStatement("select * from "+tableName);
		String args = "select * from "+tableName+" where ";
		for (Object key : map.keySet()) {  
			Object value = map.get(key);
			Method getMethod=object.getClass().getMethod("get"+key.toString().replaceFirst(key.toString().substring(0, 1),key.toString().substring(0, 1).toUpperCase()),new Class[]{});
			if(getMethod.toString().contains("java.lang.String")){
				//如果是字符串类型则加上双引号
				value = "\"" + value + "\"";
			}
			args+=key+" = "+value+" and ";
			  }  
		//如果有参数则减去最后一个"and",如果没有参数在减去最后一个"where"
		if(args.contains("and")){args = args.substring(0, args.length()-5);}else{args = args.substring(0, args.length()-7);}
		ps = DB.getConnection().prepareStatement(args);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	 try {
	    ResultSet rs = ps.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();  
		//获得单条数据的列数
		int cols = rsmd.getColumnCount();
		while(rs.next())
		{
			Object obj = c.newInstance();
			 for (int i = 1; i <= cols; i++) {  
	             String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ;
	             Method getMethod=object.getClass().getMethod("get"+propertyName,new Class[]{});
				 //转换类型使之可以被识别
				 obj = convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null);	
	            }  
			list.add(obj);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	} 
	ps.close();
	return list;
}


//查询数据返回userList对象
@SuppressWarnings("unchecked")
public static  List<User> getUserList() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, SQLException, FileNotFoundException, IOException, ClassNotFoundException, NumberFormatException, IllegalArgumentException, InvocationTargetException {
	List<User> userList = new ArrayList<User>();  
	String sql = "select id from user ";
	ps = DB.getConnection().prepareStatement(sql);
	ResultSet rs = ps.executeQuery();
	while(rs.next())
	{
		int id = rs.getInt(1);
		userList.add(getUserById(id));
     }
	return userList;
}


//自动创建bean对象以及建表
@SuppressWarnings("unchecked")
public static String CreateTable() throws FileNotFoundException, IOException, SQLException, Exception, Exception, Throwable, Throwable, Throwable, ParseException, InstantiationException{
	  Properties properties=new Properties();
	  properties.load(new FileInputStream(new File("").getCanonicalPath()+"\\src\\util\\SqlPropertyMapping.properties"));
	  for(int p=0;p<getClassList().size();p++){
	    Class c = (Class)getClassList().get(p);
		Method[]  methods = c.getMethods();
		// 得到对象类的名字
		String cName = c.getName(); 
		// 从类的名字中解析出表名
		String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length());
		tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
		//kList装key(属性)
		List<String> kList = new ArrayList<String>(); 
		//tList装type(类型)
		List<String> tList = new ArrayList<String>(); 
		//sList装sql(类型)
		List<String> sList = new ArrayList<String>(); 
		for (Method method : methods) { 
		//循环获取对象中的方法名,也就是get,set方法
		String mName = method.getName(); 
		//判断，去掉对象中默认存在的getClass方法
		if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
		//截取key的名字
		String fieldName = mName.substring(3, mName.length()); 
		//把key首字母变成小写
		fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
		//把key装入kList,以便之后的循环
		try { 
		//value返回的是key的类型
		 Object type = method.getReturnType();
		 if(type.toString().contains("class "+Entity_Package)){type = "int";fieldName = fieldName+"_id";}
		 else{type = properties.getProperty(type.toString());}
		 kList.add(fieldName); 
		 tList.add(String.valueOf(type));
		} catch (Exception e) { 
		e.printStackTrace();
		}
		}
		}
		//防止误删除表可以修改表结构 语句：(alter table user add  column name varchar(40),add column pa varchar(40));
		  String sql = "create database if not exists "+DatebaseName+" default character set utf8 ";
		  ps = DB.getConnection().prepareStatement(sql);
	      ps.execute();
	      sql = "create table if not exists "+tableName+" (";
		   for(int i=0;i<kList.size();i++){
			   if(kList.get(i).equals("id")){
				   //自增的主键
		   sql+=kList.get(i)+" int(11) NOT NULL auto_increment,";
			   }else{
		   sql+=kList.get(i)+" "+tList.get(i)+",";
			   }
            }
		   sql+=" PRIMARY KEY  (id)) ";
		   //如果是mysql数据库则设置是巨款引擎为INNODB
		   if("MySQL".equalsIgnoreCase(DB.getConnection().getMetaData().getDatabaseProductName())){
		   ps = DB.getConnection().prepareStatement("select version()");
		   rs = ps.executeQuery();
		   if(rs.next()){
			   //通过查询mysql版本号更换数据库引擎用以完成事务操作
			   String version = rs.getString(1);
			   version = version.substring(0,version.indexOf(".", 2));
			   if(Double.valueOf(version)<5.5){
				 sql+=" TYPE=INNODB; ";
			   }else{
				 sql+=" ENGINE=INNODB; ";
			   }
			   }
		   }
		   if(!sql.equals("")){
		   ps = DB.getConnection().prepareStatement(sql);
		   ps.execute();
		   //System.out.println("创建表"+tableName+"的sql:"+sql);
		   }
		  ps = DB.getConnection().prepareStatement("select * from "+tableName);
		  ResultSet rs = ps.executeQuery();
		  ResultSetMetaData rsmd = rs.getMetaData();  
			//获得单条数据的列数
		  int cols = rsmd.getColumnCount();
		  sql = "alter table "+tableName+" ";
		  for (int i = 1; i <= cols; i++) {  
				 String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ;
	             propertyName = propertyName.substring(0,1).toLowerCase()+propertyName.substring(1);
	            	 sList.add(propertyName);
	             }
		   for(int i=0;i<kList.size();i++){
			   if(!sList.contains(kList.get(i))){
				   sql+= "add "+kList.get(i)+" "+tList.get(i)+",";
			   }
		   }
	      if(sql.contains("add")){
	    	  sql = sql.substring(0,sql.length()-1);
	      }else{
	    	  sql="";
	      }
		     if(!sql.equals("")){
		     ps = DB.getConnection().prepareStatement(sql);
		     ps.execute(sql);
		     ps.close();
		     }
	   }
			 return null;
}

//自动创建bean对象以及建表
@SuppressWarnings("unchecked")
public static String DropAndRecreateTable() throws FileNotFoundException, IOException, SQLException, Exception, Exception, Throwable, Throwable, Throwable, ParseException, InstantiationException{
	  Properties properties=new Properties();
	  properties.load(new FileInputStream(new File("").getCanonicalPath()+"\\src\\util\\SqlPropertyMapping.properties"));
	  for(int p=0;p<getClassList().size();p++){
	    Class c = (Class)getClassList().get(p);
		Method[]  methods = c.getMethods();
		// 得到对象类的名字
		String cName = c.getName(); 
		// 从类的名字中解析出表名
		String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length());
		tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
		//kList装key(属性)
		List<String> kList = new ArrayList<String>(); 
		//tList装type(类型)
		List<String> tList = new ArrayList<String>(); 
		for (Method method : methods) { 
		//循环获取对象中的方法名,也就是get,set方法
		String mName = method.getName(); 
		//判断，去掉对象中默认存在的getClass方法
		if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
		//截取key的名字
		String fieldName = mName.substring(3, mName.length()); 
		//把key首字母变成小写
		fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
		//把key装入kList,以便之后的循环
		try { 
		//value返回的是key的类型
		 Object type = method.getReturnType();
		 if(type.toString().contains("class "+Entity_Package)){type = "int";fieldName = fieldName+"_id";}
		 else{type = properties.getProperty(type.toString());}
		 kList.add(fieldName); 
		 tList.add(String.valueOf(type));
		} catch (Exception e) { 
		e.printStackTrace();
		}
		}
		}
		//建库、建表操作
		String sql = "create database if not exists "+DatebaseName+" default character set utf8 ";
		ps = DB.getConnection().prepareStatement(sql);
		ps.execute();
		sql = "drop table if exists "+tableName;
		ps = DB.getConnection().prepareStatement(sql);
		ps.execute();
		sql = "create table if not exists "+tableName+" (";
			   for(int i=0;i<kList.size();i++){
				   if(kList.get(i).equals("id")){
					   //自增的主键
			   sql+=kList.get(i)+" int(11) NOT NULL auto_increment,";
				   }else{
			   sql+=kList.get(i)+" "+tList.get(i)+",";
				   }
	             }
			   //设定数据库主键为id属性
			   sql+=" PRIMARY KEY  (id)) ";
			   //如果是mysql数据库则设置是巨款引擎为INNODB
			   if("MySQL".equalsIgnoreCase(DB.getConnection().getMetaData().getDatabaseProductName())){
			   ps = DB.getConnection().prepareStatement("select version()");
			   rs = ps.executeQuery();
			   if(rs.next()){
				   //通过查询mysql版本号更换数据库引擎用以完成事务操作
				   String version = rs.getString(1);
				   version = version.substring(0,version.indexOf(".", 2));
				   if(Double.valueOf(version)<5.5){
					 sql+=" TYPE=INNODB; ";
				   }else{
					 sql+=" ENGINE=INNODB; ";
				   }
				   }
			   }
		     if(!sql.equals("")){
		     ps = DB.getConnection().prepareStatement(sql);
		     ps.execute(sql);
		     ps.close();
		     System.out.println("创建表"+tableName+"的sql:"+sql);
		     }
	  }
			 return null;
}

// 得到表中最大的id(若在事务中重复则自动加1)
@SuppressWarnings("unchecked")
public static int getMaxId(Object object) {
	int id = 0;
	if(countList.size()!=0){
			if(countList.containsKey(object.getClass())){
				countList.put(object.getClass(), String.valueOf(Integer.valueOf(countList.get(object.getClass()))+1));
				}else{countList.put(object.getClass(),String.valueOf(0));}
	}else{
	countList.put(object.getClass(),String.valueOf(0));
	}
	Class c = object.getClass();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	try {
		 ps = DB.getConnection().prepareStatement("select  max(id)  from "+tableName);
		 rs = ps.executeQuery();
		 if(rs.next()){id = rs.getInt(1)+Integer.valueOf(countList.get(object.getClass()));}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return id;
}


// 清空对象的属性

@SuppressWarnings("unchecked")
public static void cleanProperty(Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
	Class c = object.getClass();
    methods = c.getMethods();
	for (Method method : methods) { 
	//循环获取对象中的方法名,也就是get,set方法
	String mName = method.getName(); 
	//判断，去掉对象中默认存在的getClass方法
	if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
	//截取key的名字
	String propertyName = mName.substring(3, mName.length()); 
	Method getMethod=object.getClass().getMethod("get"+propertyName,new Class[]{});
	 //转换类型使之可以被识别
	 object = convertClass(getMethod, object,"set"+propertyName,null,null);
	}
	}
}

//添加一条子对象数据(返回sql语句)
@SuppressWarnings("unchecked")
private static String addChild(Object object) throws SQLException {
	Object value = null;
	Object childObject = null;
	String sql = "insert into ";
	Class c = object.getClass();
    methods = c.getMethods();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	//把key首字母变成小写
	tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
	//把表名拼接到sql
	sql += tableName + "("; 
	//kList装key(属性)
	List<String> kList = new ArrayList<String>(); 
	//vList装value(值)
	List vList = new ArrayList(); 
	for (Method method : methods) { 
	//循环获取对象中的方法名,也就是get,set方法
	String mName = method.getName(); 
	//判断，去掉对象中默认存在的getClass方法
	if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
	//截取key的名字
	String fieldName = mName.substring(3, mName.length()); 
	//把key首字母变成小写
	fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
	if(method.getReturnType().toString().contains("class "+Entity_Package)){fieldName = fieldName+"_id";}
	//把key装入kList,以便之后的循环
	kList.add(fieldName); 
	try { 
	//value返回的是key的类型
      value = method.invoke(object); 
	if (value instanceof String || value instanceof java.util.Date || value instanceof java.sql.Date) { 
	//把value装入vList,如果是String类型则加上引号
	vList.add("\"" + value + "\""); 
	} else { 
	if(method.getReturnType().toString().contains("class "+Entity_Package)){
		 childObject = value ;
		if(childObject != null){
	    c = childObject.getClass();
	    methods = c.getMethods();
	    for (Method childmethod : methods) {
		     if(childmethod.toString().contains("getId()")){
		    	 value = Integer.valueOf(String.valueOf(childmethod.invoke(childObject))); 
		    		if(countList.size()!=0){
		    			if(countList.containsKey(object.getClass())){
		    				countList.put(object.getClass(), String.valueOf(Integer.valueOf(countList.get(object.getClass()))+1));
		    				value = getMaxId(childObject)+1;
		   	    		    Method getMethod=childObject.getClass().getMethod("getId" ,new Class[]{});
		   	    		    convertClass(getMethod, childObject,"setId",String.valueOf(value), null);	
		    			}
		    		}
//	    	 if(Integer.valueOf(String.valueOf(value))==0){
//	    		 //如果没有设置id属性则得到表中最大id后给对象赋值
//	    		 value = getMaxId(childObject)+1;
//	    		 Method getMethod=childObject.getClass().getMethod("getId" ,new Class[]{});
//	    		 convertClass(getMethod, childObject,"setId",String.valueOf(value), null);
//	    		 };
	    	 }
	    }
	    addChild(childObject); 
		}
	}
	vList.add(value);
	}
	} catch (Exception e) { 
	e.printStackTrace();
	}
	}
	}
	//循环出key和value拼接sql
for (int i = 0; i < kList.size(); i++) { 
	if (i < kList.size() - 1) { 
	sql += kList.get(i) + ","; 
	} else { 
	sql += kList.get(i) + ") values(";
	}
	}
 for (int i = 0; i < vList.size(); i++) { 
	if (i < vList.size() - 1) { 
	sql += vList.get(i) + ","; 
	} else { 
	sql += vList.get(i) + ")";
	}
	}
    sql+=";";
    TransactionSql+=sql+"\n";
    return sql;
}

//修改一条子对象数据(返回sql语句)
@SuppressWarnings("unchecked")
private static String updateChild(Object object) throws SQLException, SecurityException, NoSuchMethodException, NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	Object childObject = null;
	String sql = "update ";
	Class c = object.getClass();
    methods = c.getMethods();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	//把key首字母变成小写
	tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
	//把表名拼接到sql
	sql += tableName + " set "; 
	//kList装key(属性)
	List<String> kList = new ArrayList<String>(); 
	//vList装value(值)
	List vList = new ArrayList(); 
	for (Method method : methods) { 
	//循环获取对象中的方法名,也就是get,set方法
	String mName = method.getName(); 
	//判断，去掉对象中默认存在的getClass方法
	if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
	//截取key的名字
	String fieldName = mName.substring(3, mName.length()); 
	//把key首字母变成小写
	fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
	if(method.getReturnType().toString().contains("class "+Entity_Package)){fieldName = fieldName+"_id";}
	//把key装入kList,以便之后的循环
	kList.add(fieldName); 
	try { 
	//value返回的是key的类型
	Object value = method.invoke(object); 
	if (value instanceof String || value instanceof java.util.Date || value instanceof java.sql.Date) { 
	//把value装入vList,如果是String类型则加上引号
	vList.add("\"" + value + "\""); 
	} else { 
	if(method.getReturnType().toString().contains("class "+Entity_Package)){
		 childObject = value ;
		if(childObject != null){
	    c = childObject.getClass();
	    methods = c.getMethods();
	    for (Method childmethod : methods) {
	     if(childmethod.toString().contains("getId()")){
	    	 value = Integer.valueOf(String.valueOf(childmethod.invoke(childObject))); 
	    	 }
	    }
	    updateChild(childObject); 
		}
	}
	vList.add(value);
	}
	} catch (Exception e) { 
	e.printStackTrace();
	}
	}
	}
	Method getMethod=object.getClass().getMethod("getId" ,new Class[]{});
	int id = Integer.valueOf(String.valueOf(getMethod.invoke(object)));
	//if(id==0){id = -1;}
	//循环出key和value拼接sql
for (int i = 0; i < kList.size(); i++) { 
	if (i < kList.size() - 1) { 
	sql += kList.get(i) + " = "+vList.get(i)+","; 
	} else { 
	sql += kList.get(i) + " = "+vList.get(i)+" where id = "+id; 
	}
	}
    sql+=";";
    TransactionSql+=sql+"\n";
    return sql;
}

//返回entity包下所有类的对象列表
@SuppressWarnings("unused")
public static List<Object> getClassList() throws ClassNotFoundException{
	List<Object> classList = new ArrayList<Object>();
	File way = new File(Entity_AbsPath);
	File[] f = way.listFiles();
	for(int i=0;i<f.length;i++){
		if(f[i].getName().contains(".java") && !f[i].getName().contains("BaseEntity")){
			//获得entity包下的所有java类并存放到classNameList
			Object object= Class.forName(Entity_Package+"."+f[i].getName().substring(0,f[i].getName().lastIndexOf(".")));
			classList.add(object);
		}
	}
	return classList;
}

//返回entity包下所有类的名称列表
@SuppressWarnings("unused")
public static List<String> getClassNameList(){
	List<String> classNameList = new ArrayList<String>();
	File way = new File(Entity_AbsPath);
	File[] f = way.listFiles();
	for(int i=0;i<f.length;i++){
		if(f[i].getName().contains(".java")){
			//获得entity包下的所有java类并存放到classNameList
			classNameList.add(f[i].getName().substring(0,f[i].getName().lastIndexOf(".")));
		}
	}
	return classNameList;
}

}
