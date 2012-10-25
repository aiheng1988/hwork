package controllers;

import java.util.List;

import models.BlogModel;

import org.hwork.annotation.ControllerName;
import org.hwork.annotation.Import;
import org.hwork.annotation.RequestMapping;
import org.hwork.common.RequestMethod;
import org.hwork.utils.JsonUtils;
import org.hwork.web.Controller;

import entitys.Blog;
import entitys.Users;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-9 上午1:08:33 
 * @version V0.1  
 */

@ControllerName("blog")
public class BlogController extends Controller {
	
	private String username;
	
	private Blog blog;
	
	private Users users;
	
	@Import
	private BlogModel blogModel;
	
	@Override
	public void init() throws Exception {
	}

	@RequestMapping(value = "ll", method = RequestMethod.GET)
	public void list() throws Exception{
		render("redirect:/index/index");
	}
	
	public void index() throws Exception{
		render("index");
	}
	
	public void test() throws Exception{
		List<Blog> lists = blogModel.findAll();
		System.out.println(username);
		renderJson(lists, false);
	}
	
	public void testBlog() throws Exception{
		System.out.println(blog.getUsers().getUsername() + blog.getTitle());
	}

	@RequestMapping(value="add", method = RequestMethod.POST)
	public void addUsers() throws Exception{
		renderJson(users);
	}

}
