package org.hwork.model;

import org.hwork.annotation.Table;
import org.hwork.utils.GenericsUtils;

public abstract class Model<T> extends BaseModel {
	
	/**
	 * 获取表信息
	 */
	protected String getTableName(){
		String tableName = null;
		Class<?> clazz = GenericsUtils.getSuperClassGenericType(this.getClass());
		if(clazz.isAnnotationPresent(Table.class)){
			tableName = clazz.getAnnotation(Table.class).value();
		} else {
			tableName = clazz.getSimpleName().toLowerCase();
		}
		return tableName;
	}

}
