package com.leven.dmd.pro.domain;

public class DBType {
	private String type;
	private String url;
	private String driver;
	
	/**
	 * ���ݿ�����
	 */
	public DBType(){
		
	}
	/**
	 * ���ݿ�����
	 * @param type ��������
	 * @param url ����url��ʽ
	 * @param driver ������ȫ��
	 */
	public DBType(String type, String url, String driver) {
		this.type = type;
		this.url = url;
		this.driver = driver;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
}
