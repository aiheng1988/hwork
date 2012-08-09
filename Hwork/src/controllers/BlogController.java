package controllers;

import org.hwork.web.Controller;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-9 上午1:08:33 
 * @version V0.1  
 */

public class BlogController extends Controller {
	
	@Override
	public void init() throws Exception {
		System.out.println("每一次请求，都会执行这个!");
	}

	public void list() throws Exception{
		assign("name", "aiheng1988");
		render("list.jsp");
	}
	
	public void index() throws Exception{
		assign("name", params.get("name")[0]);
		render("list");
	}
	
	public void test() throws Exception{
		renderText("helloworld");
	}

}
