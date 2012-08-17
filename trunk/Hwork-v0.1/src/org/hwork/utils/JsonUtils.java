package org.hwork.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hwork.annotation.Json;

/**
 * Json工具类，用户List,Map与Json的转换
 * @author heng.ai
 *
 */
public class JsonUtils {
	
	private static Pattern pattern = Pattern.compile(",(\\w+:\\{\\S+\\}),?");
	
	private static String toJson(Object obj,Class<? extends Annotation> annotationClass , boolean all) throws IllegalArgumentException, IllegalAccessException{
		if(obj == null) 
			return "null";
		if(obj instanceof String){
			return "\"" + (String)obj + "\"";
		}
		if(obj instanceof Integer) 
			return ((Integer)obj).toString();
		if(obj instanceof Double)
			return ((Double)obj).toString();
		if(obj instanceof Float)
			return ((Float)obj).toString();
		if(obj instanceof Short)
			return ((Short)obj).toString();
		if(obj instanceof Long)
			return ((Long)obj).toString();
		if(obj instanceof Byte)
			return ((Byte)obj).toString();
		if(obj instanceof Character)
			return "\"" + ((Character)obj).toString() + "\"";
		if(obj instanceof Boolean)
			return ((Boolean)obj).toString();
		if(obj instanceof Date)
			return "\"" + ((Date)obj).getTime() + "\"";
		StringBuilder json = new StringBuilder(); 
		json.append("{");
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			if(all){
				json.append("\"").append(field.getName()).append("\"").append(":").append(toJson(field.get(obj), annotationClass, all)).append(",");
				continue;
			}
			if(field.isAnnotationPresent(annotationClass)){
				json.append("\"").append(field.getName()).append("\"").append(":").append(toJson(field.get(obj), annotationClass, all)).append(",");
			}
		}
		if(json.lastIndexOf(",") != -1){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("}");
		return json.toString();
	}
	
	public static String toJson(Object obj) throws IllegalArgumentException, IllegalAccessException{
		return toJson(obj, Json.class, true);
	}
	
	public static String toJson(Object obj, boolean isAll) throws IllegalArgumentException, IllegalAccessException{
		return toJson(obj, Json.class, isAll);
	}
	
	/**
	 * 将对象转换为json数据
	 * annotationclass为自定义annotation，可以自己定义要列入json的字段
	 * @param obj
	 * @param annotationClass
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static String toJson(Object obj, Class<? extends Annotation> annotationClass) throws IllegalArgumentException, IllegalAccessException{
		return toJson(obj, annotationClass, false);
	}
	
	public static String toJson(List<?> list) throws IllegalArgumentException, IllegalAccessException{
		return toJson(list, true);
	}
	
	public static String toJson(List<?> list, boolean isAll) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder json = new StringBuilder(); 
		json.append("[");
		for (Object o : list) {
			if(o  instanceof Object[]){
				json.append(toJson((Object[])o, isAll)).append(",");
				continue;
			}
			if(o  instanceof List<?>){
				json.append(toJson((List<?>)o, isAll)).append(",");
				continue;
			}
			if(o  instanceof Map<?,?>){
				json.append(toJson((Map<?,?>)o, isAll)).append(",");
				continue;
			}
			json.append(toJson(o, isAll)).append(",");
		}
		if(json.lastIndexOf(",") != -1){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("]");
		return json.toString();
	}
	
	public static String toJson(Map<String,?> map, boolean isAll) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder json = new StringBuilder(); 
		json.append("[");
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			if(entry.getValue() instanceof Object[]){
				json.append("{\"").append(entry.getKey()).append("\"").append(":").append(toJson((Object[])entry.getValue(), isAll)).append("},");
				continue;
			}
			if(entry.getValue() instanceof List<?>){
				json.append("{\"").append(entry.getKey()).append("\"").append(":").append(toJson((List<?>)entry.getValue(), isAll)).append("},");
				continue;
			}
			if(entry.getValue() instanceof Map<?,?>){
				json.append("{\"").append(entry.getKey()).append("\"").append(":").append(toJson((Map<?,?>)entry.getValue(), isAll)).append("},");
				continue;
			}
			json.append("{\"").append(entry.getKey()).append("\"").append(":").append(toJson(entry.getValue(), isAll)).append("},");
		}
		if(json.lastIndexOf(",") != -1){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("]");
		return json.toString();
	}
	
	public static String toJson(Map<String,?> map) throws IllegalArgumentException, IllegalAccessException{
		return toJson(map, true);
	}
	
	public static String toJson(Object[] objs, boolean isAll) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder json = new StringBuilder();
		json.append("[");
		for (Object obj : objs) {
			if(obj  instanceof Object[]){
				json.append(toJson((Object[])obj, isAll)).append(",");
				continue;
			}
			if(obj instanceof List<?>){
				json.append(toJson((List<?>)obj, isAll)).append(",");
				continue;
			}
			if(obj  instanceof Map<?,?>){
				json.append(toJson((Map<?,?>)obj, isAll)).append(",");
				continue;
			}
			json.append(toJson(obj, isAll)).append(",");
		}
		if(json.lastIndexOf(",") != -1){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		json.append("]");
		return json.toString();
	}
	
	public static String toJson(Object[] objs) throws IllegalArgumentException, IllegalAccessException{
		return toJson(objs, true);
	}
	
	/**
	 * 下面定义解析json的数据
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> fromJsonAsMap(String json, Class<T> clazz) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException{
		if(json == null)
			return null;
		if(!json.startsWith("[") && !json.endsWith("]")){
			throw new IllegalAccessException("要转换的Json数据的格式不正确");
		}
		Map<String, T> map = null;
		if(StringUtils.isBlank(json)) return null;
		if(!json.trim().startsWith("\"")) return null;
		json = json.replace("[", "").replace("]", "");
		map = new HashMap<String, T>();
		String[] arrs = json.replace("\"", "").split("\\}[,]\"");
		for (String str : arrs) {
			if(str.split(":")[1].startsWith("{")){
				map.put(str.split(":")[0], fromJsonAsObject(str.split(":")[1], clazz));
				continue;
			}
			if(str.split(":")[1].startsWith("[")){
				throw new IllegalAccessException("暂不支持如此复杂的数据结构");
			}
			map.put(str.split(":")[0], (T)str.split(":")[1]);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromJsonAsList(String json, Class<T> clazz) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException{
		if(json == null)
			return null;
		if(!json.startsWith("[") && !json.endsWith("]")){
			throw new IllegalAccessException("要转换的Json数据的格式不正确");
		}
		List<T> list = null;
		json = json.replace("[", "").replace("]", "");
		if(StringUtils.isBlank(json)) return null;
		if(!json.trim().startsWith("{")) return null;
		list = new ArrayList<T>();
		String[] arrs = json.replace("\"", "").split("\\}[,]");
		for (String str : arrs) {
			if(str.startsWith("{")){
				list.add(fromJsonAsObject(str, clazz));
				continue;
			}
			if(str.startsWith("[")){
				throw new IllegalAccessException("暂不支持如此复杂的数据结构");
			}
			list.add((T)str);
		}
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public static <T> T[] fromJsonAsArray(String json, Class<T> clazz) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException{
		if(json == null)
			return null;
		if(!json.startsWith("[") && !json.endsWith("]")){
			throw new IllegalAccessException("要转换的Json数据的格式不正确");
		}
		T[] arr = null;
		json = json.replace("[", "").replace("]", "");
		if(StringUtils.isBlank(json)) return null;
		if(!json.trim().startsWith("{")) return null;
		String[] arrs = json.replace("\"", "").split("\\}[,]");
		int i = 0;
		for (String str : arrs) {
			if(str.startsWith("{")){
				arr[i] = fromJsonAsObject(str, clazz);
				continue;
			}
			if(str.startsWith("[")){
				throw new IllegalAccessException("暂不支持如此复杂的数据结构");
			}
			arr[i] = (T) str;
		}
		return arr;
	}
	
	public static <T> T fromJsonAsObject(String json, Class<T> clazz) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException{
		if(StringUtils.isBlank(json)) return null;
		if(json.startsWith("{")){
			json = json.substring(1);
		}
		if(json.endsWith("}") && StringUtils.counts(json, "\\}") > StringUtils.counts(json, "\\{")){
			json = json.substring(0, json.length() - 1);
		}
		String[] _arrs = json.replace("\"", "").split(",(\\w+:\\{\\S+\\}),?");
		List<String> lists = new ArrayList<String>();
		Collections.addAll(lists, _arrs);
		Matcher m = pattern.matcher(json.replace("\"", ""));
		while(m.find()){
			lists.add(m.group(1));
		}
		List<String> arrs = new ArrayList<String>();
		for(String s : lists){
			if(!s.contains("{")){
				for(String ss : s.split(",")){
					arrs.add(ss);
				}
			} else{
				arrs.add(s);
			}
		}
		T obj = clazz.newInstance();
		for(String str : arrs){
			Field field = clazz.getDeclaredField(str.split(":")[0].trim());
			field.setAccessible(true);
			if(field.getType().getName().equals("java.lang.String")){
				field.set(obj, str.split(":")[1].trim());
				continue;
			}
			if(field.getType().getName().equals("java.lang.Integer") || field.getType().getName().equals("int")){
				field.set(obj, Integer.valueOf(str.split(":")[1].trim()));
				continue;
			}
			if(field.getType().getName().equals("java.lang.Double") || field.getType().getName().equals("double")){
				field.set(obj, Double.valueOf(str.split(":")[1].trim()));
				continue;
			}
			if(field.getType().getName().equals("java.lang.Long") || field.getType().getName().equals("long")){
				field.set(obj, Long.valueOf(str.split(":")[1].trim()));
				continue;
			}
			if(field.getType().getName().equals("java.lang.Float") || field.getType().getName().equals("float")){
				field.set(obj, Float.valueOf(str.split(":")[1].trim()));
				continue;
			}
			if(field.getType().getName().equals("java.lang.Short") || field.getType().getName().equals("short")){
				field.set(obj, Short.valueOf(str.split(":")[1].trim()));
				continue;
			}
			if(field.getType().getName().equals("java.lang.Byte") || field.getType().getName().equals("byte")){
				field.set(obj, Byte.valueOf(str.split(":")[1].trim()));
				continue;
			}
			if(field.getType().getName().equals("java.lang.Boolean") || field.getType().getName().equals("boolean")){
				field.set(obj, Boolean.valueOf(str.split(":")[1].trim()));
				continue;
			}
			if(field.getType().getName().equals("java.util.Date")){
				if("null".equals(str.split(":")[1].trim())){
					field.set(obj, new Date());
				}else{
					field.set(obj, new Date(Long.valueOf(str.split(":")[1].trim())));
				}
				continue;
			}
			if("null".equals(str.split(":", 2)[1].trim())){
				field.set(obj, null);
			} else{
				field.set(obj, fromJsonAsObject(str.split(":", 2)[1], field.getType()));
			}
		}
		return obj;
	}

}
