package com.leven.dmd.pro.pref.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DBConfigUtil {
	/**
	 * ��ȡ��ݿ�t��
	 * @author leven
	 * 2012-12-5 ����10:12:32
	 * @param config
	 * @return
	 */
	public static Connection getConnection(DBConfigModel config) {
		Connection conn = null;
		try {
			String[] jdbcPaths = config.getJarPath().split(";");
			 URL[] urls = new URL[jdbcPaths.length];
			for(int i=0;i<jdbcPaths.length;i++) {
				String jdbcPath = jdbcPaths[i];
				File file = new File(jdbcPath);
				URL jdbcDriverURL = file.toURL();
				urls[i] = jdbcDriverURL;
			}
			
		     URLClassLoader urlclassLoader = new URLClassLoader( urls,ClassLoader.getSystemClassLoader());  
		     Driver driver = (Driver)urlclassLoader.loadClass(config.getDriver()).newInstance();
		     Properties prop = new Properties();
		     prop.setProperty("user",config.getUsername());   
		     prop.setProperty("password",config.getPassword());
		     conn = driver.connect(config.getUrl(), prop);
		}catch(Exception e) {
			return null;
		}
		return conn;
	}
	/**
	 * 数据源配置是否可用
	 * @author leven
	 * 2012-12-5 10:12:43
	 * @param config
	 * @return
	 */
	public static boolean isConfigAvailable(DBConfigModel config){
		Connection conn = getConnection(config);
		if(conn == null){
			return false;
		}else {
			try {
				conn.close();
			} catch (SQLException e) {
				return true;
			}
			return true;
		}
	}
	
	
	/**
	 * 执行指定的SQL语句，如果执行成功，则返回null，否则返回异常信息
	 * @param sql
	 * @param config
	 * @return
	 */
	public static String checkSQL(String sql,DBConfigModel config){
		Connection conn = null;
		Statement stat = null;
		try {
			conn = getConnection(config);
			stat = conn.createStatement();
			stat.execute(sql);
		} catch (Exception e1) {
			System.out.print(e1.toString());
			System.out.println(sql);
			System.out.println("/--------------------------------------/");
			return e1.toString();
		} finally{
			try {
				if(stat != null){
					stat.close();
					stat = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				return e.toString();
			}
		}
		return null;
	}
}
