package org.hwork.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Render {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public Render(){}
	
	public void setRequest(HttpServletRequest request){
		this.request = request;
	}
	
	public void setResponse(HttpServletResponse response){
		this.response = response;
	}
	
	public abstract void render(String view);

}
