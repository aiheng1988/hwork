package org.hwork.web.router;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hwork.web.ActionMapping;
import org.hwork.web.Controller;

/**
 * @Description: 将请求分派到对应的地址
 * @author heng.ai@chinacache.com  
 * @date 2012-8-7 下午10:03:46 
 * @version V0.1  
 */

public interface Router {
	public ActionMapping doRouter(HttpServletRequest request, HttpServletResponse response);
	public void setControllers(Map<String, Class<? extends Controller>> controllers);
}
