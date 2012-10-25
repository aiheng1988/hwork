package org.hwork.web.router;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hwork.utils.StringUtils;
import org.hwork.web.ActionMapping;
import org.hwork.web.Constant;

/**
 * @Description: 路由所有的请求，并交给相应模块处理
 * @author heng.ai@chinacache.com  
 * @date 2012-8-7 下午10:36:51 
 * @version V0.1  
 */

public final class BaseRouter implements Router {
	

	private final static Logger logger = Logger.getLogger(BaseRouter.class);

	@Override
	public ActionMapping doRouter(HttpServletRequest request,
			HttpServletResponse response) {
		ActionMapping actionMapping = new ActionMapping();
		
		String contextPath = request.getContextPath();
		String realUri = StringUtils.isBlank(contextPath) ? request.getRequestURI() : request.getRequestURI().substring(contextPath.length());
		
		realUri = realUri.substring(1);
		if(realUri.endsWith("/")){
			realUri = realUri.substring(0, realUri.length() -1);
		}
		
		Map<String,String[]> params = new HashMap<String, String[]>();
		//处理默认首页的情况
		actionMapping.setAction("Index");
		actionMapping.setMethod("index");
		actionMapping.setParams(params);
		
		if(!StringUtils.isBlank(realUri)){
			String[] strs = realUri.split("/");
			switch(strs.length){
			case 1:
				actionMapping.setAction(strs[0]);
				break;
			case 2:
				actionMapping.setAction(strs[0]);
				actionMapping.setMethod(strs[1]);
				break;
			default:
				actionMapping.setAction(strs[0]);
				actionMapping.setMethod(strs[1]);
				for (int i = 2,len = strs.length; i < len; i+=2){
					String value = "";
					if(len > i+1){
						try {
							value = URLDecoder.decode(strs[i+1], Constant.encoding);
						} catch (UnsupportedEncodingException e) {
							logger .info("不支持的编码 " + Constant.encoding);
						}
					}
					String[] values = value.split(",");
					params.put(strs[i], values);
				}
			}
		}
		
		//加入表单提交的数据
		if("POST".equalsIgnoreCase(request.getMethod())){
			Map<String, String[]> postParams = request.getParameterMap();
			params.putAll(postParams);
		}
		actionMapping.setParams(params);
		
		return actionMapping;
	}

}
