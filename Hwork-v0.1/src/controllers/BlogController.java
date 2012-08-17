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

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-9 上午1:08:33 
 * @version V0.1  
 */

@ControllerName("index")
public class BlogController extends Controller {
	
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
		renderText(JsonUtils.toJson(params));
	}
	
	public void test() throws Exception{
		List<Blog> lists = blogModel.findAll();
		renderJson(lists, false);
	}

}
