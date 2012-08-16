package org.hwork.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hwork.annotation.Table;
import org.hwork.utils.GenericsUtils;
import org.hwork.utils.StringUtils;

public abstract class Model<T> extends BaseModel {
	
	private final static Logger logger = Logger.getLogger(Model.class);
	
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
	
	/**
	 * 获取模型的Class
	 * @return
	 */
	protected Class<?> getModelClass(){
		return GenericsUtils.getSuperClassGenericType(this.getClass());
	}
	
	/**
	 * 获取所有信息(单表)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() throws SQLException{
		List<T> lists = new ArrayList<T>();
		HashMap<String, Object>[] datas = querySql("select * from " + getTableName());
		try {
			for(int i = 0, len = datas.length; i < len; i++){
				T entity = (T)this.getModelClass().newInstance();
				for(Map.Entry<String, Object> obj : datas[i].entrySet()){
					//调用set方法设置值
					entity.getClass().getMethod("set" + StringUtils.toUpperFirst(obj.getKey()), new Class<?>[]{entity.getClass().getDeclaredField(obj.getKey()).getType()}).invoke(entity, obj.getValue());
				}
				lists.add(entity);
			}
		} catch (Exception e){
			logger.error("实例化模型出错", e);
		}
		return lists;
	}
	
}
