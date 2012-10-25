package org.hwork.web;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hwork.annotation.RequestMapping;
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
	
	public ActionMappingExecute(ActionMapping actionMapping, HttpServletRequest request, HttpServletResponse response){
		this.actionMapping = actionMapping;
		this.request = request;
		this.response = response;
		controllers = ActionContext.getControllers();
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
		
		if(controllers.containsKey(action.toLowerCase())){
			actionClass = controllers.get(action.toLowerCase()).getName();
		}
		
		Controller controller = (Controller) Class.forName(actionClass).newInstance();
		//设置上下文变量
		controller.setParams(params);
		controller.setRequest(request);
		controller.setResponse(response);
		Method me = null;
		for(Method m : Class.forName(actionClass).getMethods()){
			//如果存在Method级别的注解
			if(m.isAnnotationPresent((RequestMapping.class))){
				RequestMapping anno = m.getAnnotation(RequestMapping.class);
				if(method.equals(anno.value()) && request.getMethod().equalsIgnoreCase(anno.method().getMethod())){
					me = m;
					break;
				}
			}
		}
		me = me == null ? Class.forName(actionClass).getMethod(method) : me;
		
		//自动绑定值
		new AutoImportValue(actionClass, controller, params).importValue();
		
		//先执行init()方法
		Class.forName(actionClass).getMethod("init").invoke(controller, NULLPARAMS);
		//处理对应的controller
		me.invoke(controller, NULLPARAMS);
		
		return true;
	}
	
}
