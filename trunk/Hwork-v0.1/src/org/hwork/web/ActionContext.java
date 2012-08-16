package org.hwork.web;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 执行上下文
 * @author heng.ai
 */
public class ActionContext {
	
	private static Map<String, Class<? extends Controller>> controllers = null;
	private static HttpServletRequest request = null;
	private static HttpServletResponse response = null;
	private static ServletContext application = null;

	public static Map<String, Class<? extends Controller>> getControllers(){
		return controllers;
	}
	
	public static void setControllers(Map<String, Class<? extends Controller>> controllers){
		ActionContext.controllers = controllers;
	}
	
	public static HttpServletRequest getRequest(){
		return request;
	}
	
	public static void setRequest(HttpServletRequest request){
		ActionContext.request = request;
	}
	
	public static HttpServletResponse getResponse(){
		return response;
	}
	
	public static void setResponse(HttpServletResponse response){
		ActionContext.response = response;
	}

	public static void setApplication(ServletContext servletContext) {
		ActionContext.application = servletContext;
	}
	
	public static ServletContext getApplication(){
		return application;
	}

	public static void clean() {
		ActionContext.controllers = null;
		ActionContext.request = null;
		ActionContext.response = null;
		ActionContext.application = null;
	}

}
