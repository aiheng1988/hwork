package org.hwork.web;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-9 上午12:06:07 
 * @version V0.1  
 */

public class Properties {
	
	private String controllerPackage = Constant.controllerPackage;
	private String defaultRender = Constant.defaultRender;
	
	public Properties loadProperties(String file) throws IOException{
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
		java.util.Properties properties = new java.util.Properties();
		try {
			if(is != null){
				properties.load(is);
				this.controllerPackage = properties.get("controllersPackage").toString();
				this.defaultRender = properties.get("defaultRender").toString();
			}
		} catch (IOException e) {
			throw new IOException("对不起配置文件不存在", e);
		} finally{
			if(is != null) is.close();
			is = null;
		}
		return this;
	}
	
	public String getControllerPackage(){
		return this.controllerPackage;
	}

	public String getDefaultRender() {
		return this.defaultRender;
	}

}
