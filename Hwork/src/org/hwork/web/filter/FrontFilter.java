package org.hwork.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hwork.render.JspRender;
import org.hwork.render.Render;
import org.hwork.utils.StringUtils;
import org.hwork.web.ActionMapping;
import org.hwork.web.ActionMappingExecute;
import org.hwork.web.Constant;
import org.hwork.web.Controller;
import org.hwork.web.Properties;
import org.hwork.web.router.BaseRouter;
import org.hwork.web.router.Router;

public class FrontFilter implements Filter{
	
	private Router router;
	private Map<String, Class<? extends Controller>> controllers = new HashMap<String, Class<? extends Controller>>();
	private static Logger logger = Logger.getLogger(FrontFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String routerClass = filterConfig.getInitParameter("router");
		if(StringUtils.isBlank(routerClass)){
			router = new BaseRouter();
		}else{
			try {
				router = (Router)Class.forName(routerClass).newInstance();
			} catch (InstantiationException e) {
				logger.error("创建实例错误", e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				logger.error("Please check your file web.xml! [" + routerClass + " not found]");
			}
		}
		//查找Controller注解
		try {
			String controllerPackage = new Properties().loadProperties(Constant.propertiesName).getControllerPackage();
			InputStream is = this.getClass().getResourceAsStream(controllerPackage.replaceAll(".", "/"));
			//File file = new File();
		} catch (IOException e) {
			logger.info("采用默认配置");
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		request.setCharacterEncoding(Constant.encoding);
		response.setCharacterEncoding(Constant.encoding);
		
		ActionMapping actionMapping = router.doRouter(request, response);
	
		ActionMappingExecute execute = new ActionMappingExecute(actionMapping, controllers, request, response);
		try {
			if(!execute.doExecute()){
				filterChain.doFilter(request, response);
			}
		} catch (IOException e) {
			logger.info("采用默认配置");
		} catch (ClassNotFoundException e){
			text("对不起，你请求的地址不存在", response);
		} catch (NoSuchMethodException e){
			text("对不起，你请求的地址不存在", response);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		
	}
	
	@Override
	public void destroy() {
		
	}
	
	public void text(String message, HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().write(message);
		response.getWriter().close();
	}

}
