package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.BeanManager;
import util.Util;

/**
 * TestServlet
 */
public class TestServlet extends Util {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	//构造函数 
	public TestServlet() {
		super();
	}

    //销毁
	public void destroy() {
		super.destroy(); 
	}

	@SuppressWarnings("unchecked")
	//得到前台请求参数列表与值，值为String数组
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			//初始化所有方法(里面完成了把后台传值赋值到实体类,每次必须有这个方法)
			initMethod(request, response);
			
			//打印前台值
			System.out.println("user.name:"+getUser().getName());
			System.out.println("user.birthday:"+getUser().getBirthday());
			System.out.println("user.addr:"+getUser().getAddr());
			System.out.println("user.admin.height:"+getUser().getAdmin().getHeight());
			System.out.println("user.admin.name:"+getUser().getAdmin().getName());
			System.out.println("user.admin.addr:"+getUser().getAdmin().getAddr());
			System.out.println("user.name:"+getUser().getName());
			System.out.println("admin.addr:"+getAdmin().getAddr());
			System.out.println("admin.height:"+getAdmin().getHeight());
			System.out.println("admin.info.password:"+getAdmin().getInfo().getPassword());
			System.out.println("user.admin.info.username:"+getUser().getAdmin().getInfo().getUsername());
//			
			System.out.println("info.username:"+getInfo().getUsername());
			System.out.println("info.password:"+BeanManager.getInfo().getPassword());
			System.out.println("info.date:"+getInfo().getDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 }
		
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGet(request, response);
	}

	 //初始化
 	public void init() throws ServletException {
		// Put your code here
	}

}
