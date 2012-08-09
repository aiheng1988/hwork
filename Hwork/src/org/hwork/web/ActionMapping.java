package org.hwork.web;

import java.util.Map;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-7 下午10:44:58 
 * @version V0.1  
 */

public class ActionMapping {
	
	private String action;
	
	private String method;
	
	private Map<String,String[]> params;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String[]> getParams() {
		return params;
	}

	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

}
