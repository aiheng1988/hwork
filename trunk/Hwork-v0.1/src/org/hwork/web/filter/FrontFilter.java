package org.hwork.web.filter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hwork.annotation.ControllerName;
import org.hwork.utils.LoadProperties;
import org.hwork.utils.StringUtils;
import org.hwork.web.ActionMapping;
import org.hwork.web.ActionMappingExecute;
import org.hwork.web.Constant;
import org.hwork.web.Controller;
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
		String controllerPackage = new LoadProperties(Constant.propertiesName).getValue("controllerPackage");
		if(controllerPackage == null || "".equals(controllerPackage)){
			controllerPackage = "controllers";
		}
		ServletContext context = filterConfig.getServletContext();
		String realPath = context.getRealPath("/WEB-INF/classes");
		realPath = realPath + File.separator + controllerPackage.replace(".", File.separator) + File.separator;
		try {
			scanPackage(realPath);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("包扫描出错，有部分包不存在", e);
		}
		//将controllers设置到router中
		router.setControllers(controllers);
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
		controllers.clear(); //清空
	}
	
	public void text(String message, HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().write(message);
		response.getWriter().close();
	}
	
	@SuppressWarnings("unchecked")
	private void scanPackage(String realPath) throws ClassNotFoundException{
		//开始包扫描(递归扫描)
		File file = new File(realPath);
		if(!file.exists()){
			throw new RuntimeException("请检查 hwork.properties 文件中controllerPackage属性的包路径是否存在");
		}
		File[] files = file.listFiles();
		for(File f : files){
			if(f.isDirectory()){
				scanPackage(f.getPath());
			}
			if(f.getName().endsWith(".class")){
				String sep = null;
				String packagePath = null;
				if("/".equals(File.separator)){
					sep = "/";
				}else if("\\".equals(File.separator)){
					sep = "\\\\";
				}
				packagePath = f.getAbsolutePath().split("classes" + sep)[1].replace(File.separator, ".").replace(".class", "");
				Class<?> clazz = Class.forName(packagePath);
				Class<?> superClazz = clazz.getSuperclass();
				if(superClazz != null && superClazz.getName().equals("org.hwork.web.Controller") && clazz.isAnnotationPresent(ControllerName.class)){
					controllers.put(clazz.getAnnotation(ControllerName.class).value(), (Class<? extends Controller>)clazz);
				}
			}
		}
	}

}
