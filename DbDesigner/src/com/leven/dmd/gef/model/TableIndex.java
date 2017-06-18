package com.leven.dmd.gef.model;

import java.io.Serializable;

/**
 * ������
 * @author leven
 * 2012-9-6 ����02:00:33
 */
public class TableIndex implements Serializable{
	private String name = "";
	private String columns = "";
	/**
	 * ��������
	 */
	private String type = "Normal";
	private String comments = "";
	/**
	 * 是否字段设置主键时自动生成的，如果是的话，建表的时候不需要创建。
	 */
	private boolean isAutoCreated = false;
	//备用属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	
	/**
	 * 表索引
	 * @param name
	 * @param columns
	 * @param type
	 * @param isAutoCreated
	 */
	public TableIndex(String name, String columns, String type,boolean isAutoCreated) {
		this.name = name;
		this.columns = columns;
		this.type = type;
		this.isAutoCreated = isAutoCreated;
	}
	
	public TableIndex(String name, String columns, String type,String comments) {
		this.name = name;
		this.columns = columns;
		this.type = type;
		this.comments = comments;
	}
	/**
	 * ������
	 * @param name ���
	 */
	public TableIndex(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public boolean isAutoCreated() {
		return isAutoCreated;
	}
	public void setAutoCreated(boolean isAutoCreated) {
		this.isAutoCreated = isAutoCreated;
	}
}