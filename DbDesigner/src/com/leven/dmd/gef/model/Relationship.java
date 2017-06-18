package com.leven.dmd.gef.model;
/**
 * ���ϵʵ�����
 * @author lifeng
 * 2012-7-11 ����03:28:20
 */
@SuppressWarnings("serial")
public class Relationship extends PropertyAwareObject {

	private String name;
	
	private Table primaryKeyTable;
	private Table foreignKeyTable;
	
	private String foreignKeyColumn;
	private String primaryKeyColumn;

	//预留属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;

	/**
	 * @param foreignTable
	 * @param primaryKeyTable
	 * @param foreignKeyColumn
	 */
	public Relationship(Table foreignTable, Table primaryTable,String foreignColumn,String primaryColumn) {
		super();
		this.primaryKeyTable = primaryTable;
		this.foreignKeyTable = foreignTable;
		this.setForeignKeyColumn(foreignColumn);
		this.setPrimaryKeyColumn(primaryColumn);
		this.primaryKeyTable.addPrimaryKeyRelationship(this);
		this.foreignKeyTable.addForeignKeyRelationship(this);
	}
	public Relationship(Relationship relationship) {
		super();
		this.primaryKeyTable = relationship.getPrimaryKeyTable();
		this.foreignKeyTable = relationship.getForeignKeyTable();
		this.setForeignKeyColumn(relationship.getForeignKeyColumn());
		this.setPrimaryKeyColumn(relationship.getPrimaryKeyColumn());
	}

	public void editRelation(Relationship relationship) {
		this.primaryKeyTable.removePrimaryKeyRelationship(this);
		this.primaryKeyTable.addPrimaryKeyRelationship(relationship);
		this.foreignKeyTable.removeForeignKeyRelationship(this);
		this.foreignKeyTable.addForeignKeyRelationship(relationship);
	}
	
	public void Reconnection(Relationship relationship) {
		this.primaryKeyTable.removePrimaryKeyRelationship(this);
		relationship.primaryKeyTable.addPrimaryKeyRelationship(relationship);
		this.foreignKeyTable.removeForeignKeyRelationship(this);
		relationship.foreignKeyTable.addForeignKeyRelationship(relationship);
	}

	public void setPrimaryKeyColumn(String primaryKeyColumn) {
		this.primaryKeyColumn = primaryKeyColumn;
	}
	public void setForeignKeyColumn(String foreignKeyColumn) {
		this.foreignKeyColumn = foreignKeyColumn;
	}
	
	/**
	 * @return Returns the foreignKeyColumn.
	 */
	public String getForeignKeyColumn() {
		return foreignKeyColumn;
	}

	/**
	 * @return Returns the foreignKeyTable.
	 */
	public Table getForeignKeyTable() {
		return foreignKeyTable;
	}

	/**
	 * @return Returns the primaryKeyTable.
	 */
	public Table getPrimaryKeyTable() {
		return primaryKeyTable;
	}

	/**
	 * @param sourceTable
	 *            the primary key table you are connecting to
	 */
	public void setPrimaryKeyTable(Table targetPrimaryKey) {
		this.primaryKeyTable = targetPrimaryKey;
	}

	/**
	 * @param sourceForeignKey
	 *            the foreign key table you are connecting from
	 */
	public void setForeignKeyTable(Table sourceForeignKey) {
		this.foreignKeyTable = sourceForeignKey;
	}
	public String getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}