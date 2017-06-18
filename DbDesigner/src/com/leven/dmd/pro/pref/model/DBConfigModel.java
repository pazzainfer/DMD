package com.leven.dmd.pro.pref.model;

import java.io.Serializable;

public class DBConfigModel implements Serializable {
	/**
	 * @author leven
	 * 2012-12-5 ����01:00:34
	 */
	private static final long serialVersionUID = -8235188865736972482L;
	
	private String name = "";
	private String type = "";
	private String driver = "";
	private String url = "";
	private String jarPath = "";
	private String username = "";
	private String password = "";
	private boolean isDefault = false;
	/**
	 * ����Դ����ʵ����
	 */
	public DBConfigModel(){
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getJarPath() {
		return jarPath;
	}
	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
