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

}
