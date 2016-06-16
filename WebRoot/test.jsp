<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html xmlns="http://www.w3.org/1999/xhtml">  
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>主页</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
   <!-- <a href='TestServlet?id=1&admin.id=3&user.name=党欣&user.sex=1&user.birthday=1987-10-23'>点击这里向后台传值</a>-->
   <div  style="text-align:center;margin:0 auto">
   <H2>简单form表单</H2>
    <form  method="post" action="TestServlet">
    
      info.date:<input type="text" name="info.date" value="2014-01-08"/><br/>
    info.username:<input type="text" name="info.username" value="info.username"/><br/>
    info.password:<input type="text" name="info.password" value="info.password"/><br/>
  
     
    user.admin高度:<input type="text" name="user.admin.height" value="70"/><br/>
    user.admin名字:<input type="text" name="user.admin.name" value="user.admin.name"/><br/>
    user.admin地址:<input type="text" name="user.admin.addr" value="user.admin.addr"/><br/>
    user名字:<input type="text" name="user.name" value="user.name"/><br/>
    user地址:<input type="text" name="user.addr" value="user.addr"/><br/>
    admin地址:<input type="text" name="admin.addr" value="admin.addr"/><br/>
    admin高度:<input type="text" name="admin.height" value="20"/><br/>
    admin.info.password:<input type="text" name="admin.info.password" value="123456"/><br/>
    admin.info.password:<input type="text" name="admin.info.password" value="1234567"/><br/>
    user.admin.info.username:<input type="text" name="user.admin.info.username" value="user.admin.info.username"/><br/>
     <input type="submit" value="提交 "/>
    </form>
    </div>
  </body>
</html>
