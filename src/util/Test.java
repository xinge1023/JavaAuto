package util;

import java.text.ParseException;

import entity.Admin;
import entity.Info;
import entity.User;
/***
 * @author xinge1023@163.com
 * 测试类
 */
@SuppressWarnings("serial")
public class Test  extends Util {
	public static void main(String[] args) throws ParseException, Exception, Throwable {
    //重新建表
    DropAndRecreateTable();
    //添加对象
    User user = new User();
    Admin admin = new Admin();
    Info info = new Info();
    info.setUsername("info的name");
    admin.setName("admin的name");
    user.setName("user的name");
    admin.setInfo(info);
    user.setAdmin(admin);
    user.setInfo(info);
    add(user);
    //修改对象
	User u = getUserById(1);
	Info i= u.getInfo();
	i.setUsername("被修改的info.name");
    u.setInfo(i);
    update(u);
    //查询对象
	System.out.println("id为1的User的名字:"+getUserById(1).getName());
	System.out.println("user列表:"+getUserList());
    deleteUser(1,true);
	}
}
