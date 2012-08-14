package org.hwork.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class ProxyConnection implements InvocationHandler {
	
	private final static String CLOSE_METHOD_NAME = "close";
	private DbConnectionManager dbManager;
	private Connection connection;
	
	public ProxyConnection(){}
	
	public ProxyConnection(Connection connection){
		this.connection = connection;
		dbManager = DbConnectionManager.newInstance();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object obj = null;
		if(CLOSE_METHOD_NAME.equals(method.getName())){
			dbManager.close(this.connection);
		}else{
			obj = method.invoke(this.connection, args);
		}
		return obj;
	}
	
	public Connection getConnection(){
		Class<?>[] interfaces = this.connection.getClass().getInterfaces();
		if(interfaces==null||interfaces.length==0)
		interfaces = new Class[]{Connection.class};
		Connection proxy = (Connection)Proxy.newProxyInstance(this.connection.getClass().getClassLoader(), 
				interfaces, this); 
		return proxy;
	}
	
	public void close(){
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
