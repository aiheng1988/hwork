package org.hwork.web;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hwork.annotation.ViewsPackage;
import org.hwork.render.Render;
import org.hwork.utils.StringUtils;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-8 下午11:33:52 
 * @version V0.1  
 */

public abstract class Controller {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, String[]> params;
	
	public Controller(){}
	
	public Controller(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	private void render(String message, String type) throws Exception{
		response.setContentType(type);
		Writer out = response.getWriter();
		out.write(message);
		out.close();
	}
	
	public void renderText(String message) throws Exception{
		render(message, "text/plain;charset=UTF-8");
	}
	
	public void renderHtml(String message) throws Exception{
		render(message, "text/html;charset=UTF-8");
	}
	
	public void renderJson(Object obj) throws Exception{
		//TODO: 实现JSON转换
	}
	
	public void assign(String name, Object value){
		request.setAttribute(name, value);
	}
	
	public void assign(Map<String, Object> values){
		if(values != null){
			for(Map.Entry<String, Object> entry : values.entrySet()){
				request.setAttribute(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public void render(String view) throws Exception{
		String defaultRender = Constant.defaultRender;
		String userRender = new Properties().loadProperties(Constant.propertiesName).getDefaultRender();
		if(!StringUtils.isBlank(userRender)){
			defaultRender = userRender;
		}
		if(!StringUtils.isBlank(view) && !view.endsWith(".jsp")){
			view = view + ".jsp";
		}
		String renderClass = "org.hwork.render." + StringUtils.toUpperFirst(defaultRender) + "Render";
		Class<?> clazz = Class.forName(renderClass);
		Render render = (Render)clazz.newInstance();
		render.setRequest(request);
		render.setResponse(response);
		if(this.getClass().isAnnotationPresent(ViewsPackage.class)){
			view = this.getClass().getAnnotation(ViewsPackage.class).value() + "/" + view;
		}else{
			view = this.getClass().getSimpleName().replaceAll("Controller", "").toLowerCase() + "/" + view;
		}
		render.render("/WEB-INF/" + defaultRender + "/" + view);
	}
	
	public abstract void init() throws Exception;
	
	public void setParams(Map<String, String[]> params){
		this.params = params;
	}
	
	public void setRequest(HttpServletRequest request){
		this.request = request;
	}
	
	public void setResponse(HttpServletResponse response){
		this.response = response;
	}

}
