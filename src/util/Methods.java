package util; 
import java.lang.reflect.InvocationTargetException; 
import java.lang.reflect.Method; 
import java.sql.Connection; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.ResultSetMetaData; 
import java.sql.SQLException; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import entity.*; 
/***
 * @author xinge1023@163.com
 * 基础的删查方法   
 */
 public class Methods extends BeanManager implements SystemVariables{
 //预编译对象ps
 private static PreparedStatement ps=null;
 //数据结果集
 private static ResultSet rs=null;
 //方法集
 private static  Method[] methods;
 //用于事务的sql
 private static String TransactionSql="";
 private static final long serialVersionUID = 1L;
 //通过id获得Admin对象
 public static Admin getAdminById(int id)  throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, NumberFormatException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
   boolean isNull = true;
   PreparedStatement ps=null;
   Class<Admin> c = entity.Admin.class;
   Object obj = c.newInstance();
   // 得到对象类的名字
   String cName = c.getName();
   // 从类的名字中解析出表名
   String tableName = cName.substring(cName.lastIndexOf('.') + 1,cName.length());
   //把key首字母变成小写
   tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
   try {
 	  ps = DB.getConnection().prepareStatement(" select * from " + tableName + " where id = ?");
       ps.setInt(1, id);  
       } catch (SQLException e) {
 	  e.printStackTrace();
       }
   try {
       ResultSet rs = ps.executeQuery();
       ResultSetMetaData rsmd = rs.getMetaData();
   while(rs.next())
   {
   isNull = false;
   //获得单条数据的列数
   int cols = rsmd.getColumnCount();
   for (int i = 1; i <= cols; i++) {  
     String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ;
     if(propertyName.contains("_id")){propertyName = propertyName.substring(0, propertyName.length()-3);}
     java.lang.reflect.Method getMethod=c.getMethod("get"+propertyName,new Class[]{});
 	//转换类型使之可以被识别
 	obj = Util.convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null);
       }  
   for (int i = 1; i <= cols; i++) {   
     String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ; 
     if(propertyName.contains("_id")){ 
     propertyName = propertyName.substring(0, propertyName.length()-3); 
     Method setMethod=obj.getClass().getMethod("set"+propertyName, new Class[]{Class.forName(Entity_Package+"."+propertyName)}); 
     if(rs.getString(i)==null){setMethod.invoke(obj, new Object[]{null}); }
     else{setMethod.invoke(obj, new Object[]{getChildById(propertyName,Integer.valueOf(rs.getString(i)))});}
       }else{ 
     java.lang.reflect.Method getMethod=c.getMethod("get"+propertyName,new Class[]{}); 
     //转换类型使之可以被识别 
     obj = Util.convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null); 
       }  
     }  
 	}
   } catch (SQLException e) {
 	e.printStackTrace();
 	} 
   if(!isNull){
	   return (Admin)obj;
	   }else {
		return null;
	}
  }
  
 
//查询数据返回adminList对象
@SuppressWarnings("unchecked")
public static  List<Admin> getAdminList() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, SQLException, FileNotFoundException, IOException, ClassNotFoundException, NumberFormatException, IllegalArgumentException, InvocationTargetException {
  List<Admin> adminList = new ArrayList<Admin>();  
  String sql = "select id from admin ";
  ps = DB.getConnection().prepareStatement(sql);
  ResultSet rs = ps.executeQuery();
  while(rs.next())
  {
  int id = rs.getInt(1);
adminList.add(getAdminById(id));
  }
  return adminList;
  }
  
//删除一条Admin数据(返回sql语句)
/***
 *参数说明:id为删除对象的主键,isCascade表示是否级联删除(既删除对象的同时删除子对象对应的数据) 
 */
@SuppressWarnings("unchecked")
public static void deleteAdmin(int id,boolean... isCascade) throws SQLException, NumberFormatException, SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException{
 Connection conn=null;
 boolean b = false;
	//为true则删除时删除子对象
	if(isCascade.length>=1 && isCascade[0]){b = true;}
	Object childObject = null;
 Object object = getAdminById(id); 
 if(object==null){System.out.println("id为"+id+"的Admin对象不存在,无法删除.");return ;} 
 Class c = object.getClass();
 methods = c.getMethods();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	//把key首字母变成小写
	tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
 if(b){
		for (Method method : methods) { 
		//循环获取对象中的方法名,也就是get,set方法
		String mName = method.getName(); 
		//判断，去掉对象中默认存在的getClass方法
		if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
		//截取key的名字
		String fieldName = mName.substring(3, mName.length()); 
     //把key首字母变成小写
		//fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
     if(method.getReturnType().toString().contains("class "+Entity_Package)){
     java.lang.reflect.Method getMethod=c.getMethod("get"+fieldName,new Class[]{});
     childObject = getMethod.invoke(object);
     deleteChild(childObject);
       }
    }
   }
}
	String sql = "delete from " + tableName + " where id = "+id;
	if(TransactionSql.length()==0){
	ps = DB.getConnection().prepareStatement("delete from "+tableName+" where id=?");
	ps.setInt(1, id);
 int f = ps.executeUpdate();
 if(f==1){System.out.println("删除id为"+id+"的Admin对象成功:");System.out.println("删除Admin的语句: delete from " + tableName + " where id ="+ id);}else{System.out.println("id为"+id+"的Admin对象,删除失败.");}
}else{
try {
 int count = 0;
 TransactionSql+=sql+";\n";
 conn = DB.getConnection();
 conn.setAutoCommit(false);
 for(int i=0;i<TransactionSql.split(";\n").length;i++){
  ps = conn.prepareStatement((TransactionSql.split(";"))[i]);
  ps.execute();
  ps=conn.prepareStatement("select row_count();");
  rs = ps.executeQuery();
  if(rs.next()){count+=rs.getInt(1);}
 }
 conn.commit();
 System.out.print("删除"+object.getClass().getSimpleName()+"执行事务成功的sql");
 System.out.println("(影响的数据行共"+count+"行):");
 System.out.println(TransactionSql);
 } catch (SQLException e) {
 conn.rollback();
   System.err.println("删除发生错误,执行回滚.");
   System.err.println("发生错误的语句:"+TransactionSql);
   e.printStackTrace();
   }finally{
   TransactionSql="";
   ps.close();
  }
}
      }
 //通过id获得Info对象
 public static Info getInfoById(int id)  throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, NumberFormatException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
   boolean isNull = true;
   PreparedStatement ps=null;
   Class<Info> c = entity.Info.class;
   Object obj = c.newInstance();
   // 得到对象类的名字
   String cName = c.getName();
   // 从类的名字中解析出表名
   String tableName = cName.substring(cName.lastIndexOf('.') + 1,cName.length());
   //把key首字母变成小写
   tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
   try {
 	  ps = DB.getConnection().prepareStatement(" select * from " + tableName + " where id = ?");
       ps.setInt(1, id);  
       } catch (SQLException e) {
 	  e.printStackTrace();
       }
   try {
       ResultSet rs = ps.executeQuery();
       ResultSetMetaData rsmd = rs.getMetaData();
   while(rs.next())
   {
   isNull = false;
   //获得单条数据的列数
   int cols = rsmd.getColumnCount();
   for (int i = 1; i <= cols; i++) {  
     String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ;
     if(propertyName.contains("_id")){propertyName = propertyName.substring(0, propertyName.length()-3);}
     java.lang.reflect.Method getMethod=c.getMethod("get"+propertyName,new Class[]{});
 	//转换类型使之可以被识别
 	obj = Util.convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null);
       }  
   for (int i = 1; i <= cols; i++) {   
     String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ; 
     if(propertyName.contains("_id")){ 
     propertyName = propertyName.substring(0, propertyName.length()-3); 
     Method setMethod=obj.getClass().getMethod("set"+propertyName, new Class[]{Class.forName(Entity_Package+"."+propertyName)}); 
     if(rs.getString(i)==null){setMethod.invoke(obj, new Object[]{null}); }
     else{setMethod.invoke(obj, new Object[]{getChildById(propertyName,Integer.valueOf(rs.getString(i)))});}
       }else{ 
     java.lang.reflect.Method getMethod=c.getMethod("get"+propertyName,new Class[]{}); 
     //转换类型使之可以被识别 
     obj = Util.convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null); 
       }  
     }  
 	}
   } catch (SQLException e) {
 	e.printStackTrace();
 	} 
   if(!isNull){
	   return (Info)obj;
	   }else {
		return null;
	}
  }
  
 
//查询数据返回infoList对象
@SuppressWarnings("unchecked")
public static  List<Info> getInfoList() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, SQLException, FileNotFoundException, IOException, ClassNotFoundException, NumberFormatException, IllegalArgumentException, InvocationTargetException {
  List<Info> infoList = new ArrayList<Info>();  
  String sql = "select id from info ";
  ps = DB.getConnection().prepareStatement(sql);
  ResultSet rs = ps.executeQuery();
  while(rs.next())
  {
  int id = rs.getInt(1);
infoList.add(getInfoById(id));
  }
  return infoList;
  }
  
//删除一条Info数据(返回sql语句)
/***
 *参数说明:id为删除对象的主键,isCascade表示是否级联删除(既删除对象的同时删除子对象对应的数据) 
 */
@SuppressWarnings("unchecked")
public static void deleteInfo(int id,boolean... isCascade) throws SQLException, NumberFormatException, SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException{
 Connection conn=null;
 boolean b = false;
	//为true则删除时删除子对象
	if(isCascade.length>=1 && isCascade[0]){b = true;}
	Object childObject = null;
 Object object = getInfoById(id); 
 if(object==null){System.out.println("id为"+id+"的Info对象不存在,无法删除.");return ;} 
 Class c = object.getClass();
 methods = c.getMethods();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	//把key首字母变成小写
	tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
 if(b){
		for (Method method : methods) { 
		//循环获取对象中的方法名,也就是get,set方法
		String mName = method.getName(); 
		//判断，去掉对象中默认存在的getClass方法
		if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
		//截取key的名字
		String fieldName = mName.substring(3, mName.length()); 
     //把key首字母变成小写
		//fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
     if(method.getReturnType().toString().contains("class "+Entity_Package)){
     java.lang.reflect.Method getMethod=c.getMethod("get"+fieldName,new Class[]{});
     childObject = getMethod.invoke(object);
     deleteChild(childObject);
       }
    }
   }
}
	String sql = "delete from " + tableName + " where id = "+id;
	if(TransactionSql.length()==0){
	ps = DB.getConnection().prepareStatement("delete from "+tableName+" where id=?");
	ps.setInt(1, id);
 int f = ps.executeUpdate();
 if(f==1){System.out.println("删除id为"+id+"的Info对象成功:");System.out.println("删除Info的语句: delete from " + tableName + " where id ="+ id);}else{System.out.println("id为"+id+"的Info对象,删除失败.");}
}else{
try {
 int count = 0;
 TransactionSql+=sql+";\n";
 conn = DB.getConnection();
 conn.setAutoCommit(false);
 for(int i=0;i<TransactionSql.split(";\n").length;i++){
  ps = conn.prepareStatement((TransactionSql.split(";"))[i]);
  ps.execute();
  ps=conn.prepareStatement("select row_count();");
  rs = ps.executeQuery();
  if(rs.next()){count+=rs.getInt(1);}
 }
 conn.commit();
 System.out.print("删除"+object.getClass().getSimpleName()+"执行事务成功的sql");
 System.out.println("(影响的数据行共"+count+"行):");
 System.out.println(TransactionSql);
 } catch (SQLException e) {
 conn.rollback();
   System.err.println("删除发生错误,执行回滚.");
   System.err.println("发生错误的语句:"+TransactionSql);
   e.printStackTrace();
   }finally{
   TransactionSql="";
   ps.close();
  }
}
      }
 //通过id获得User对象
 public static User getUserById(int id)  throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, NumberFormatException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
   boolean isNull = true;
   PreparedStatement ps=null;
   Class<User> c = entity.User.class;
   Object obj = c.newInstance();
   // 得到对象类的名字
   String cName = c.getName();
   // 从类的名字中解析出表名
   String tableName = cName.substring(cName.lastIndexOf('.') + 1,cName.length());
   //把key首字母变成小写
   tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
   try {
 	  ps = DB.getConnection().prepareStatement(" select * from " + tableName + " where id = ?");
       ps.setInt(1, id);  
       } catch (SQLException e) {
 	  e.printStackTrace();
       }
   try {
       ResultSet rs = ps.executeQuery();
       ResultSetMetaData rsmd = rs.getMetaData();
   while(rs.next())
   {
   isNull = false;
   //获得单条数据的列数
   int cols = rsmd.getColumnCount();
   for (int i = 1; i <= cols; i++) {  
     String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ;
     if(propertyName.contains("_id")){propertyName = propertyName.substring(0, propertyName.length()-3);}
     java.lang.reflect.Method getMethod=c.getMethod("get"+propertyName,new Class[]{});
 	//转换类型使之可以被识别
 	obj = Util.convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null);
       }  
   for (int i = 1; i <= cols; i++) {   
     String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ; 
     if(propertyName.contains("_id")){ 
     propertyName = propertyName.substring(0, propertyName.length()-3); 
     Method setMethod=obj.getClass().getMethod("set"+propertyName, new Class[]{Class.forName(Entity_Package+"."+propertyName)}); 
     if(rs.getString(i)==null){setMethod.invoke(obj, new Object[]{null}); }
     else{setMethod.invoke(obj, new Object[]{getChildById(propertyName,Integer.valueOf(rs.getString(i)))});}
       }else{ 
     java.lang.reflect.Method getMethod=c.getMethod("get"+propertyName,new Class[]{}); 
     //转换类型使之可以被识别 
     obj = Util.convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null); 
       }  
     }  
 	}
   } catch (SQLException e) {
 	e.printStackTrace();
 	} 
   if(!isNull){
	   return (User)obj;
	   }else {
		return null;
	}
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
  
//删除一条User数据(返回sql语句)
/***
 *参数说明:id为删除对象的主键,isCascade表示是否级联删除(既删除对象的同时删除子对象对应的数据) 
 */
@SuppressWarnings("unchecked")
public static void deleteUser(int id,boolean... isCascade) throws SQLException, NumberFormatException, SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException{
 Connection conn=null;
 boolean b = false;
	//为true则删除时删除子对象
	if(isCascade.length>=1 && isCascade[0]){b = true;}
	Object childObject = null;
 Object object = getUserById(id); 
 if(object==null){System.out.println("id为"+id+"的User对象不存在,无法删除.");return ;} 
 Class c = object.getClass();
 methods = c.getMethods();
	// 得到对象类的名字
	String cName = c.getName(); 
	// 从类的名字中解析出表名
	String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
	//把key首字母变成小写
	tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
 if(b){
		for (Method method : methods) { 
		//循环获取对象中的方法名,也就是get,set方法
		String mName = method.getName(); 
		//判断，去掉对象中默认存在的getClass方法
		if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
		//截取key的名字
		String fieldName = mName.substring(3, mName.length()); 
     //把key首字母变成小写
		//fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
     if(method.getReturnType().toString().contains("class "+Entity_Package)){
     java.lang.reflect.Method getMethod=c.getMethod("get"+fieldName,new Class[]{});
     childObject = getMethod.invoke(object);
     deleteChild(childObject);
       }
    }
   }
}
	String sql = "delete from " + tableName + " where id = "+id;
	if(TransactionSql.length()==0){
	ps = DB.getConnection().prepareStatement("delete from "+tableName+" where id=?");
	ps.setInt(1, id);
 int f = ps.executeUpdate();
 if(f==1){System.out.println("删除id为"+id+"的User对象成功:");System.out.println("删除User的语句: delete from " + tableName + " where id ="+ id);}else{System.out.println("id为"+id+"的User对象,删除失败.");}
}else{
try {
 int count = 0;
 TransactionSql+=sql+";\n";
 conn = DB.getConnection();
 conn.setAutoCommit(false);
 for(int i=0;i<TransactionSql.split(";\n").length;i++){
  ps = conn.prepareStatement((TransactionSql.split(";"))[i]);
  ps.execute();
  ps=conn.prepareStatement("select row_count();");
  rs = ps.executeQuery();
  if(rs.next()){count+=rs.getInt(1);}
 }
 conn.commit();
 System.out.print("删除"+object.getClass().getSimpleName()+"执行事务成功的sql");
 System.out.println("(影响的数据行共"+count+"行):");
 System.out.println(TransactionSql);
 } catch (SQLException e) {
 conn.rollback();
   System.err.println("删除发生错误,执行回滚.");
   System.err.println("发生错误的语句:"+TransactionSql);
   e.printStackTrace();
   }finally{
   TransactionSql="";
   ps.close();
  }
}
      }
 
//获得一条子对象的数据(返回sql语句)
@SuppressWarnings({ "unused", "unchecked" })
 private static Object getChildById(String objectName,int id)  throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException, NumberFormatException, IllegalArgumentException, InvocationTargetException {
   boolean isNull = true;
   PreparedStatement ps=null;
   Class c = Class.forName(Entity_Package+"."+objectName);
   Object obj = c.newInstance();
   // 得到对象类的名字
   String cName = c.getName();
   // 从类的名字中解析出表名
   String tableName = cName.substring(cName.lastIndexOf('.') + 1,cName.length());
   //把key首字母变成小写
   tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
   try {
 	 ps = DB.getConnection().prepareStatement(" select * from " + tableName + " where id = "+id);
       } catch (SQLException e) {
          e.printStackTrace();
       }
	  try {
      ResultSet rs = ps.executeQuery();
      ResultSetMetaData rsmd = rs.getMetaData();
   while(rs.next())
    {
    isNull = false;
    //获得单条数据的列数
    int cols = rsmd.getColumnCount();
    for (int i = 1; i <= cols; i++) {  
    String propertyName = rsmd.getColumnName(i).replaceFirst(rsmd.getColumnName(i).substring(0, 1),rsmd.getColumnName(i).substring(0, 1).toUpperCase()) ;
    if(propertyName.contains("_id")){
    propertyName = propertyName.substring(0, propertyName.length()-3);
	   Method setMethod=obj.getClass().getMethod("set"+propertyName, new Class[]{Class.forName(Entity_Package+"."+propertyName)});
    setMethod.invoke(obj, new Object[]{getChildById(propertyName,Integer.valueOf(rs.getString(i)))});
	   }else{
    java.lang.reflect.Method getMethod=c.getMethod("get"+propertyName,new Class[]{});
    //转换类型使之可以被识别
    obj = Util.convertClass(getMethod, obj,"set"+propertyName,rs.getString(i),null);
    } 
   }
  }
 } catch (SQLException e) {
    e.printStackTrace();
 } 
  if(!isNull){ 
	   return obj; 
	   }else { 
		return null; 
	} 
 }
 
//删除一条子对象的数据(返回sql语句)
@SuppressWarnings({ "unused", "unchecked" })
private static String deleteChild(Object object) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
 Object childObject=null;
 String sql="";
 if(object==null){return "对象为null";}
	Class c = object.getClass();
 methods = c.getMethods();
	// 得到对象类的名字
 String cName = c.getName(); 
 // 从类的名字中解析出表名
 String tableName = cName.substring(cName.lastIndexOf(".") + 1,cName.length()); 
 //把key首字母变成小写
 tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
 for (Method method : methods) { 
  //循环获取对象中的方法名,也就是get,set方法
  String mName = method.getName(); 
  //判断，去掉对象中默认存在的getClass方法
  if (mName.startsWith("get") && !mName.startsWith("getClass")) { 
  //截取key的名字
  String fieldName = mName.substring(3, mName.length()); 
	 if(method.getReturnType().toString().contains("class "+Entity_Package)){
  java.lang.reflect.Method getMethod=c.getMethod("get"+fieldName,new Class[]{});
  childObject = getMethod.invoke(object);
  deleteChild(childObject);
     }
   }
  }
 java.lang.reflect.Method getMethod=c.getMethod("getId",new Class[]{});
 int id  = Integer.valueOf(String.valueOf(getMethod.invoke(object)));
 sql = "delete from " + tableName + " where id = "+id;
 TransactionSql+=sql+";\n";
 return sql;
 }
}
