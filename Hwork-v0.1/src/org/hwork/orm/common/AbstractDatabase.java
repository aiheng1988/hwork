package org.hwork.orm.common;

/**
 * @Description: 描述数据库抽象类，定义数据拼装
 * @author heng.ai@chinacache.com  
 * @date 2012-8-2 下午10:03:17 
 * @version V0.1  
 */

public abstract class AbstractDatabase {
	
	/**
	 * 设置表与对象的信息
	 * @param tableName
	 */
	public abstract void setTable(String tableName); 
}
