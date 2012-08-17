package org.hwork.web;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hwork.annotation.ControllerName;
import org.hwork.render.Render;
import org.hwork.utils.JsonUtils;
import org.hwork.utils.LoadProperties;
import org.hwork.utils.StringUtils;

/**
 * @Description: 控制器基类，封装基础渲染或参数获取方法
 * @author heng.ai@chinacache.com  
 * @date 2012-8-8 下午11:33:52 
 * @version V0.1  
 */

public abstract class Controller {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	/**
	 * 封装请求发送过来的参数
	 */
	protected Map<String, String[]> params;
	
	public Controller(){}
	
	/**
	 * 构造方法
	 * @param request
	 * 		request请求
	 * @param response
	 * 		response处理
	 */
	public Controller(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	/**
	 * 渲染相应信息至网页
	 * @param message
	 * 		输出信息的内容
	 * @param type
	 * 		ContentType的类型<br/>
	 * 		例如：text/html;charset=utf-8
	 * @param charset
	 * 		渲染的格式响应编码
	 * @throws Exception
	 */
	public void render(String message, String type, String charset) throws Exception{
		response.setContentType(type);
		response.setCharacterEncoding(charset);
		Writer out = response.getWriter();
		out.write(message);
		out.close();
	}
	
	/**
	 * 渲染相应信息至网页(默认输出编码UTF-8)
	 * @param message
	 * 		输出信息的内容
	 * @param type
	 * 		ContentType的类型<br/>
	 * 		例如：text/html;charset=utf-8
	 * @throws Exception
	 */
	private void render(String message, String type) throws Exception{
		response.setContentType(type);
		response.setCharacterEncoding("UTF-8");
		Writer out = response.getWriter();
		out.write(message);
		out.close();
	}
	
	/**
	 * 渲染文本信息至网页（默认输出编码为UTF-8）
	 * @param message
	 * 		文本信息
	 * @throws Exception
	 */
	public void renderText(String message) throws Exception{
		render(message, "text/plain;charset=UTF-8");
	}
	
	/**
	 * 渲染文本信息至网页
	 * @param message
	 * 		文本信息
	 * @param charset
	 * 		输出编码
	 * @throws Exception
	 */
	public void renderText(String message, String charset) throws Exception{
		render(message, "text/plain;charset=UTF-8", charset);
	}
	
	/**
	 * 渲染HTML内容至网页
	 * @param message
	 * 		HTML文件内容体（可以包含javascript，css等）
	 * @throws Exception
	 */
	public void renderHtml(String message) throws Exception{
		render(message, "text/html;charset=UTF-8");
	}
	
	/**
	 * 将obj转换为json并输出至网页（json文本输出到网页） 有缓存
	 * @param obj
	 * 		待转换为json的对象
	 * @throws Exception
	 */
	public void renderJson(Object obj) throws Exception{
		renderJson(obj, false);
	}
	
	/**
	 * 将obj转换为json并输出至网页  有缓存
	 * @param obj
	 * 		待转换为json的对象
	 * @param jsonType
	 * 		是否以json文件的形式传给网页
	 * @throws Exception
	 */
	public void renderJson(Object obj, boolean jsonType) throws Exception{
		if(jsonType){
			if(obj instanceof List<?>){
				render(JsonUtils.toJson((List<?>)obj), "text/json;charset=UTF-8");
				return;
			}
			if(obj instanceof Map<?,?>){
				render(JsonUtils.toJson((Map<?,?>)obj), "text/json;charset=UTF-8");
				return;
			}
			if(obj instanceof Object[]){
				render(JsonUtils.toJson((Object[])obj), "text/json;charset=UTF-8");
				return;
			}
			render(JsonUtils.toJson(obj), "text/json;charset=UTF-8");
		}else{
			if(obj instanceof List<?>){
				renderText(JsonUtils.toJson((List<?>)obj));
				return;
			}
			if(obj instanceof Map<?,?>){
				renderText(JsonUtils.toJson((Map<?,?>)obj));
				return;
			}
			if(obj instanceof Object[]){
				renderText(JsonUtils.toJson((Object[])obj));
				return;
			}
			renderText(JsonUtils.toJson(obj));
		}
	}
	
	/**
	 * 将传出对应的ajax文本至网页（一般用于ajax操作，无缓存）
	 * @param obj
	 * 		传回给网页的ajax文本
	 * @throws Exception
	 */
	public void renderAjaxText(Object obj) throws Exception{
		renderAjax(obj, false);
	}
	
	/**
	 * 将传出对应的json数据至网页（一般用于ajax操作，无缓存）
	 * @param obj
	 * 		传回给网页的对象（通过JsonUtils来转换为json数据）
	 * @throws Exception
	 */
	public void renderAjaxJson(Object obj) throws Exception{
		renderAjax(obj, true);
	}
	
	/**
	 * 将传出对应的json数据至网页（一般用于ajax操作，无缓存）
	 * @param obj
	 * 		传回给网页的对象（通过JsonUtils来转换为json数据）
	 * @param isJson
	 * 		是否为json数据
	 * @throws Exception
	 */
	public void renderAjax(Object obj, boolean isJson) throws Exception{
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		//防止缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma","No-cache");    
        response.setDateHeader("Expires",0);
        Writer out = response.getWriter();
        if(isJson)
		out.write(JsonUtils.toJson(obj));
        else
        out.write(obj.toString());
		out.close();
	}
	
	/**
	 * 弹出错误信息，并返回上一步操作（js实现）
	 * @param alert
	 * 		弹出的信息
	 * @throws Exception
	 */
	public void renderAlert(String alert) throws Exception{
		renderHtml("<script type='text/javascript'>alert('" + alert + "');history.go(-1);</script>");
	}
	
	/**
	 * 弹出错误信息，并跳转到相应页面（js实现）
	 * @param alert
	 * 		弹出的信息
	 * @param url
	 * 		跳转的页面
	 * @throws Exception
	 */
	public void renderAlert(String alert, String url) throws Exception{
		renderHtml("<script type='text/javascript'>alert('" + alert + "');top.location.href='" + url + "';</script>");
	}
	
	/**
	 * 客户端跳转，参数丢失 （建议在表单提交后调用此方法）
	 * @param url
	 * 		跳转的页面
	 * @throws Exception
	 */
	public void redirect(String url) throws Exception{
		renderHtml("<script type='text/javascript'>top.location.href='" + url + "';</script>");
	}
	
	/**
	 * 给视图绑定参数值 （绑定一个值）
	 * @param name
	 * 		在模板中通过改名获取对象
	 * @param value
	 * 		在模板中要获取的内容
	 */
	public void assign(String name, Object value){
		request.setAttribute(name, value);
	}
	
	/**
	 * 给视图绑定参数值（绑定一组值Map<String,Object>形式）
	 * @param values
	 */
	public void assign(Map<String, Object> values){
		if(values != null){
			for(Map.Entry<String, Object> entry : values.entrySet()){
				request.setAttribute(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 渲染对应的模板文件
	 * @param view
	 * 		模板文件，或文件名<br/>
	 * 		例如：render("list") 或 render("list.jsp") 框架将会寻找对应模块文件夹下的list.jsp文件
	 * @throws Exception
	 */
	public void render(String view) throws Exception{
		if(StringUtils.isBlank(view)) throw new IllegalAccessException("视图不能为空");
		if(view.trim().startsWith("redirect:")){
			view = view.replace("redirect:", "").trim();
			redirect(view);
			return;
		}
		String defaultRender = Constant.defaultRender;
		String userRender = new LoadProperties(Constant.propertiesName).getValue("defaultRender");
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
		if(this.getClass().isAnnotationPresent(ControllerName.class)){
			view = this.getClass().getAnnotation(ControllerName.class).value() + "/" + view;
		}else{
			view = this.getClass().getSimpleName().replaceAll("Controller", "").toLowerCase() + "/" + view;
		}
		render.render("/WEB-INF/" + defaultRender + "/" + view);
	}
	
	/**
	 * 获取POST或GET请求参数值
	 * @param key
	 * 		参数名，一般为链接参数或表单参数
	 * @return
	 */
	public String params(String key){
		String value = "";
		value = params.get(key)[0];
		return value;
	}
	
	/**
	 * 获取POST或GET请求参数值 （数组形式，一般用于多选框的获取）<br/>
	 * 注意：如果想在链接上以数组的形式传值，可以用“，”分割值传递 <br/>
	 * 例如：http://localhost/index/index/loves/football,basketball,sport <br/>
	 * 接收值： paramArr("loves")  => 可以获取到相应的数组 {football,basketball,sport}
	 * @param key
	 * 		参数名，一般为链接参数或表单参数
	 * @return
	 */
	public String[] paramsArr(String key){
		return params.get(key);
	}
	
	/**
	 * 每一次执行Controller方法之前都会调用init方法
	 * @throws Exception
	 */
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
