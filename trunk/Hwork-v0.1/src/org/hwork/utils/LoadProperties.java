package org.hwork.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hwork.web.Constant;

/**
 * @Description: TODO
 * @author heng.ai@chinacache.com  
 * @date 2012-8-9 上午12:06:07 
 * @version V0.1  
 */

public class LoadProperties {
	
	private Properties properties = new Properties();
	
	public LoadProperties(String file){
		if(StringUtils.isBlank(file))
			throw new RuntimeException("配置文件路径不能为空");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
		try {
				properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException("配置文件不存在，文件：" + file, e);
		} finally{
			if(is != null){
				 try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			is = null;
		}
	}
	
	public String getValue(String key){
		String value = null;
		value = properties.getProperty(key);
		return value;
	}
	
}
