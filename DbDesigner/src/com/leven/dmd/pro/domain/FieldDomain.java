package com.leven.dmd.pro.domain;

public class FieldDomain {

	private String fieldName;

	private String tableName;
	
	private String fieldType;

	private String fieldComment;

	private boolean isPrimaryKey;

	private int fieldLenth;

	private int fieldScale;
	
	private boolean isNullable;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String fieldTableName) {
		this.tableName = fieldTableName;
	}

	public int getFieldLenth() {
		return fieldLenth;
	}

	public void setFieldLenth(int fieldLenth) {
		this.fieldLenth = fieldLenth;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean primiryKeyOrNot) {
		this.isPrimaryKey = primiryKeyOrNot;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType
	 *            the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return the fieldComment
	 */
	public String getFieldComment() {
		return fieldComment;
	}

	/**
	 * @param fieldComment
	 *            the fieldComment to set
	 */
	public void setFieldComment(String fieldComment) {
		this.fieldComment = fieldComment;
	}

	public int getFieldScale() {
		return fieldScale;
	}

	public void setFieldScale(int fieldScale) {
		this.fieldScale = fieldScale;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
}
