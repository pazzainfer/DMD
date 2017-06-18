package com.leven.dmd.pro.domain;
/**
 * 数据库表对象
 * @author leven
 * create at 2013-11-1 上午3:16:33
 */
public class TableDomain {
	private String tableName;
	private String cnName;
	private String owner;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}