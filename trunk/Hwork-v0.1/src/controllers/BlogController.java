package controllers;

import org.hwork.annotation.ControllerName;
import org.hwork.web.Controller;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-9 上午1:08:33 
 * @version V0.1  
 */

@ControllerName("blog")
public class BlogController extends Controller {
	
	@Override
	public void init() throws Exception {
	}

	public void list() throws Exception{
		render("list.jsp");
	}
	
	public void index() throws Exception{
		
		render("list");
	}
	
	public void test() throws Exception{
		
	}

}
