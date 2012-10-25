package org.hwork.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtils {
	 public static Object convertType(Object value, Class<?> type) {
         if(value instanceof String){
                 String result = (String) value;
                 if("java.lang.Integer".equals(type.getName()) || "int".equals(type.getName())){
                         return Integer.parseInt(result);
                 }
                 if("java.lang.Double".equals(type.getName()) || "double".equals(type.getName())){
                     return Double.parseDouble(result);
                 }
                 if("java.lang.Float".equals(type.getName()) || "float".equals(type.getName())){
                     return Float.parseFloat(result);
                 }
                 if("java.lang.Short".equals(type.getName()) || "short".equals(type.getName())){
                     return Short.parseShort(result);
                 }
                 if("java.lang.Long".equals(type.getName()) || "long".equals(type.getName())){
                     return Long.parseLong(result);
                 }
                 if("java.lang.Byte".equals(type.getName()) || "byte".equals(type.getName())){
                     return Byte.parseByte(result);
                 }
                 if("java.lang.Boolean".equals(type.getName()) || "boolean".equals(type.getName())){
                     return Boolean.parseBoolean(result);
                 }
                 if("java.util.Date".equals(type.getName())){
                         Date date = convertDate(result);
                         if(date != null) return date;
                 }
                 
                 return result;
         }
         if(value instanceof String[]){
                 String[] result = (String[]) value;
                 if("[Ljava.lang.Integer;".equals(type.getName()) || "[I".equals(type.getName())){
                         Integer[] integers = new Integer[result.length];
                         int i = 0;
                         for(String res : result){
                                 integers[i++] = Integer.parseInt(res);
                         }
                         return integers;
                 }
                 return result;
         }
         return value;
 }

	 private static Date convertDate(String value){
         Date date = null;
         SimpleDateFormat sdf = null;
         if(value.indexOf("-") != -1 && value.indexOf(":") == -1){
                 sdf = new SimpleDateFormat("yyyy-MM-dd");
                 try {
                         date = sdf.parse(value);
                 } catch (ParseException e) {
                         date = null;
                 }
                 return date;
         }
         if(value.indexOf("/") != -1){
                 sdf = new SimpleDateFormat("yyyy/MM/dd");
                 try {
                         date = sdf.parse(value);
                 } catch (ParseException e) {
                         date = null;
                 }
                 return date;
         }
         if(value.indexOf("-") != -1 && value.indexOf(":") != -1){
                 sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 try {
                         date = sdf.parse(value);
                 } catch (ParseException e) {
                         date = null;
                 }
                 return date;
         }
         return date;
	 }
}
