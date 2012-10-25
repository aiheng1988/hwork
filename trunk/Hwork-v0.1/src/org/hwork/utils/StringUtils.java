package org.hwork.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 字符串工具
 * @author heng.ai@chinacache.com  
 * @date 2012-8-7 下午10:30:22 
 * @version V0.1  
 */

public class StringUtils {
	
	/**
	 * 判断字符是否是空白（包括null,空白，空格）
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(str == null){
			return true;
		}else if(str.trim().equals("")){
			return true;
		}
		return false;
	}
	
	/**
	 * 将首字母转大写
	 * @param str
	 * @return
	 */
	public static String toUpperFirst(String str){
		if(!isBlank(str)){
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return str;
	}
	
	public static int counts(String str, String regex){
		int result = 0;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()){
			result++;
		}
		return result;
	}
	
	public static boolean isBaseType(Class<?> type){
		 if("java.lang.Integer".equals(type.getName()) || "int".equals(type.getName())){
             return true;
		 }
	     if("java.lang.Double".equals(type.getName()) || "double".equals(type.getName())){
	    	 return true;
	     }
	     if("java.lang.Float".equals(type.getName()) || "float".equals(type.getName())){
	    	 return true;
	     }
	     if("java.lang.Short".equals(type.getName()) || "short".equals(type.getName())){
	    	 return true;
	     }
	     if("java.lang.Long".equals(type.getName()) || "long".equals(type.getName())){
	    	 return true;
	     }
	     if("java.lang.Byte".equals(type.getName()) || "byte".equals(type.getName())){
	    	 return true;
	     }
	     if("java.lang.Boolean".equals(type.getName()) || "boolean".equals(type.getName())){
	    	 return true;
	     }
	     if("java.util.Date".equals(type.getName())){
	    	 return true;
	     }
	     return false;
	}

}
