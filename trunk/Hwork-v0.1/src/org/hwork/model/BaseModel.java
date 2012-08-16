package org.hwork.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hwork.db.DbConnectionManager;

public abstract class BaseModel {
	
	protected DbConnectionManager dbManager = DbConnectionManager.newInstance();
	
	private final static Logger logger = Logger.getLogger(BaseModel.class);
	
	/**
	 * 删除所有信息（单表），开启事务
	 */
	public boolean deleteAll(){
		boolean flag = false; 
		flag = execSql("delete from " + getTableName(), true);
		if(flag){
			logger.debug("已清空表" + getTableName());
		} else {
			logger.debug("没有删除相关数据，请检查表是否存在数据");
		}
		return flag;
	}
	
	/**
	 * 执行一条sql语句（建议执行update或insert语句）
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean execSql(String sql, boolean transaction) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = dbManager.getConnection();
			if(transaction) conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			flag = pstmt.execute();
			conn.commit();
		} catch(SQLException e){
			if(transaction) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.error("删除数据时出错，回滚数据库失败", e1);
				}
			}
			logger.error("数据库语句执行失败,sql:[ " + sql + " ]", e);
		} finally{
			close(conn, pstmt, null);
		}
		return flag;
	}
	
	/**
	 * 执行一条sql语句（传入预处理数据）
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public boolean execSql(String sql, Object[] params, boolean transaction){
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = dbManager.getConnection();
			if(transaction) conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			for(int index = 0, len = params.length; index < len ; index++ ){
				pstmt.setObject(index + 1, params[index]);
			}
			flag = pstmt.execute();
			if(transaction) conn.commit();
		} catch(SQLException e){
			if(transaction) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.error("删除数据时出错，回滚数据库失败", e1);
				}
			}
			logger.error("数据库语句执行失败,sql:[ " + sql + " ]", e);
		} finally{
			close(conn, pstmt, null);
		}
		return flag;
	}
	
	/**
	 * 查询sql，获取到List数组
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object>[] querySql(String sql){
		HashMap<String, Object>[] maps = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int index = 0;
		try{
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			rs.last();
			int rows = rs.getRow();
			rs.beforeFirst();
			if(rows == 0) return null;
			maps = new HashMap[rows];
			while(rs.next()){
				maps[index] = new HashMap<String, Object>();
				for(int column = 0; column < rsmd.getColumnCount(); column++){
					maps[index].put(rsmd.getColumnName(column+1), rs.getObject(column+1));
				}
				index++;
			}
		} catch(SQLException e){
			logger.error("请检查sql语句是否正确,sql:[ " + sql + " ]", e);
		} finally{
			logger.info("sql:" + sql);
			close(conn, pstmt, rs);
		}
		return maps;
	}
	
	protected abstract String getTableName();
	
	/**
	 * 关闭数据库连接公共方法，一次传入Connection,PreparedStatment,ResultSet
	 * @param conn
	 * @param pstmt
	 * @param rs
	 */
	protected void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
		try{
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(pstmt != null){
				pstmt.close();
				pstmt = null;
			}
			if(conn != null){
				conn.close();
			}
		} catch(SQLException e){
			logger.error("关闭数据库连接失败", e);
		}
	}

}
