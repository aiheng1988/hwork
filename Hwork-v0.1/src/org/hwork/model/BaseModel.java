package org.hwork.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hwork.db.DbConnectionManager;

public abstract class BaseModel {
	
	protected DbConnectionManager dbManager = DbConnectionManager.newInstance();
	
	private final static Logger logger = Logger.getLogger(BaseModel.class);
	
	/**
	 * 删除所有信息（单表）
	 */
	public boolean deleteAll(){
		boolean flag = false; 
		try {
			flag = execSql("delete from " + getTableName());
			if(flag){
				logger.debug("已清空表" + getTableName());
			} else {
				logger.debug("没有删除相关数据，请检查表是否存在数据");
			}
		} catch (SQLException e) {
			logger.error("执行删除所有信息的sql语句不正确，请检查表 [" + getTableName() + "] 是否存在", e);
		}
		return flag;
	}
	
	/**
	 * 执行一条sql语句（建议执行update或insert语句）
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean execSql(String sql) throws SQLException{
		boolean flag = false;
		Connection conn = dbManager.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		flag = pstmt.execute();
		conn.close();
		return flag;
	}
	
	/**
	 * 执行一条sql语句（传入预处理数据）
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public boolean execSql(String sql, Object[] params) throws SQLException{
		boolean flag = false;
		Connection conn = dbManager.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for(int index = 0, len = params.length; index < len ; index++ ){
			pstmt.setObject(index + 1, params[index]);
		}
		flag = pstmt.execute();
		return flag;
	}
	
	
	protected abstract String getTableName();

}
