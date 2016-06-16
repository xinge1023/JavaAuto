package util;
import javax.servlet.http.HttpServlet;
import entity.*;
public class BeanManager extends HttpServlet{
	 /**
	 * 对象管理器(负责生成静态对象方法供全局使用)   *单例模式*
	 */
	     private static final long serialVersionUID = 1L;
	     private static BeanManager beanManager;
	     public static BeanManager getBeanManager() {
	        if (beanManager == null) {
	        	beanManager = new BeanManager();
	        }
	        return beanManager;
	    }
       private static Util util;
       public static Util getUtil() {
	        if (util == null) {
	        	util = new Util();
	        }
	        return util;
	    }
       //全局Admin对象
       private static Admin admin;
       public static Admin getAdmin() {
	        if (admin == null) {
	        	admin = new Admin();
	        }
	        return admin;
	    }
       //全局BaseEntity对象
       private static BaseEntity baseEntity;
       public static BaseEntity getBaseEntity() {
	        if (baseEntity == null) {
	        	baseEntity = new BaseEntity();
	        }
	        return baseEntity;
	    }
       //全局Info对象
       private static Info info;
       public static Info getInfo() {
	        if (info == null) {
	        	info = new Info();
	        }
	        return info;
	    }
       //全局User对象
       private static User user;
       public static User getUser() {
	        if (user == null) {
	        	user = new User();
	        }
	        return user;
	    }
}
