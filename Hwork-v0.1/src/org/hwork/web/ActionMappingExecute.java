package org.hwork.web;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hwork.utils.LoadProperties;
import org.hwork.utils.StringUtils;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-7 下午10:48:50 
 * @version V0.1  
 */

public class ActionMappingExecute {
	
	private static final Object[] NULLPARAMS = new Object[0];
	private ActionMapping actionMapping;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Class<? extends Controller>> controllers;
	
	public ActionMappingExecute(ActionMapping actionMapping,
			Map<String, Class<? extends Controller>> controllers, HttpServletRequest request, HttpServletResponse response){
		this.actionMapping = actionMapping;
		this.request = request;
		this.response = response;
		this.controllers = controllers;
	}
	
	public boolean doExecute() throws Exception{
		String action = actionMapping.getAction();
		String method = actionMapping.getMethod();
		Map<String, String[]> params = actionMapping.getParams();
		String actionClass = null;
		//放过收藏夹图标
		if("favicon.ico".equals(action)){
			return false;
		}
		//放过静态资源文件夹
		String staticFolder = null;
		if((staticFolder = new LoadProperties(Constant.propertiesName).getValue("staticFolder")) == null){
			staticFolder = "public";
		}
		if(staticFolder.equals(action)){
			return false;
		}
		String controllerPackage = new LoadProperties(Constant.propertiesName).getValue("controllerPackage");
		actionClass = controllerPackage + "." + StringUtils.toUpperFirst(action) + "Controller";
		
		if(controllers.containsKey(action)){
			actionClass = controllers.get(action).getName();
		}
		
		Controller controller = (Controller) Class.forName(actionClass).newInstance();
		Method m = Class.forName(actionClass).getMethod(method);
		controller.setParams(params);
		controller.setRequest(request);
		controller.setResponse(response);
		//先执行init()方法
		Class.forName(actionClass).getMethod("init").invoke(controller, NULLPARAMS);
		//处理对应的controller
		m.invoke(controller, NULLPARAMS);
		
		return true;
	}
	
}
