package org.hwork.common;

public enum RequestMethod {
	
	POST("POST"),GET("GET"),DELETE("DELETE"),UPDATE("UPDATE");
	
	private String method;
	
	private RequestMethod(String method){
		this.method = method;
	}
	
	public String getMethod(){
		return method;
	}
}
