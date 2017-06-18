package com.leven.dmd.pro.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DBUtil {
	
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
			e.printStackTrace();
		}
		return conn;
	}
		
		public static void close(Connection conn) {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		public static void close(Statement st) {
			if(st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		public static void close(PreparedStatement pst) {
			if(pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		public static void close(ResultSet rs) {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		public static void setAutoCommit(Connection conn,Boolean autoCommit) {
			if(conn != null) {
				try {
					conn.setAutoCommit(autoCommit);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		public static void rollBack(Connection conn) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}
