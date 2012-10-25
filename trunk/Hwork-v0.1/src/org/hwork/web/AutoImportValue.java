package org.hwork.web;

import java.lang.reflect.Field;
import java.util.Map;

import org.hwork.annotation.Import;
import org.hwork.utils.ConvertUtils;

public class AutoImportValue {
	
	private String actionClass;
	private Controller controller;
	private Map<String, String[]> params;
	
	public AutoImportValue(String actionClass, Controller controller,
			Map<String, String[]> params) {
		super();
		this.actionClass = actionClass;
		this.controller = controller;
		this.params = params;
	}

	public void importValue() throws Exception{
		//给Import注入对象
		Object importObj = null;
		for(Field field : Class.forName(actionClass).getDeclaredFields()){
			field.setAccessible(true);
			if(field.isAnnotationPresent(Import.class)){
				if(importObj == null){
					importObj = field.getType().newInstance();
				}
				field.set(controller, importObj);
				continue;
			}
			//自动绑定值
			if(params.containsKey(field.getName())){
				 Object obj = null;
	             if(params.get(field.getName()).length <= 1){
	                  obj = ConvertUtils.convertType(params.get(field.getName())[0], field.getType());
	             } else{
	                  obj = ConvertUtils.convertType(params.get(field.getName()), field.getType());
	             }
	             field.set(controller, obj);
	             continue;
			}
			Object entityObj = null;
			Object subentityObj = null;
			for(String paramsKey : params.keySet()){
				if(paramsKey.indexOf(".") != -1){
					String[] paramsObj = paramsKey.split("\\.", 2);
					if(paramsObj[0].equals(field.getName())){
						if(entityObj == null){
							entityObj = field.getType().newInstance();
						}
						Field entityField = null;
						if(paramsObj[1].indexOf(".") == -1){
							entityField = field.getType().getDeclaredField(paramsObj[1]);
						} else {
							entityField = field.getType().getDeclaredField(paramsObj[1].split("\\.")[0]);
						}
						entityField.setAccessible(true);
						Object entityVal = null;
						if(params.get(paramsKey).length <= 1){
							if(paramsObj[1].indexOf(".") == -1){
								entityVal = ConvertUtils.convertType(params.get(paramsKey)[0], entityField.getType());
							} else{
								if(subentityObj == null){
									subentityObj = entityField.getType().newInstance();
								}
								Field subentityField = entityField.getType().getDeclaredField(paramsObj[1].split("\\.")[1]);
								subentityField.setAccessible(true);
								subentityField.set(subentityObj, ConvertUtils.convertType(params.get(paramsKey)[0], subentityField.getType()));
								entityField.set(entityObj, subentityObj);
							}
			            } else{
			            	//数组只支持一级注入（不支持blog.users.usernames）
			            	if(paramsObj[1].indexOf(".") == -1){
			            		entityVal = ConvertUtils.convertType(params.get(paramsKey), entityField.getType());
			            	}
			            }
						if(paramsObj[1].indexOf(".") == -1){
							entityField.set(entityObj, entityVal);
						}
						field.set(controller, entityObj);
					}
				}
			}
		}
	}

}
