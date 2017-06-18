package com.leven.dmd.gef.model;

import java.util.HashMap;
import java.util.Map;
/**
 * �ֶ����Ͷ��չ�ϵ��̬��
 * @author leven
 * 2012-11-21 ����11:52:25
 */
public class ColumnTypeMatcher {
	/**
	 * DB2�ֶ����Ͷ��չ�ϵ
	 */
	public static Map<String,ColumnType> DB2_SCHEMA_TYPE_MAP = new HashMap<String,ColumnType>();
	public static Map<ColumnType, String> SCHEMA_DB2_TYPE_MAP = new HashMap<ColumnType, String>();
	/**
	 * ORACLE�ֶ����Ͷ��չ�ϵ
	 */
	public static Map<String,ColumnType> ORACLE_SCHEMA_TYPE_MAP = new HashMap<String,ColumnType>();
	public static Map<ColumnType, String> SCHEMA_ORACLE_TYPE_MAP = new HashMap<ColumnType, String>();
	/**
	 * INFORMIX�ֶ����Ͷ��չ�ϵ
	 */
	public static Map<String,ColumnType> INFORMIX_SCHEMA_TYPE_MAP = new HashMap<String,ColumnType>();
	public static Map<ColumnType, String> SCHEMA_INFORMIX_TYPE_MAP = new HashMap<ColumnType, String>();
	
	static{
		DB2_SCHEMA_TYPE_MAP.put("VARCHAR",ColumnType.VARCHAR);
		DB2_SCHEMA_TYPE_MAP.put("CHAR",ColumnType.CHAR);
		DB2_SCHEMA_TYPE_MAP.put("INTEGER",ColumnType.INTEGER);
		DB2_SCHEMA_TYPE_MAP.put("BIGINT",ColumnType.LONG);
		DB2_SCHEMA_TYPE_MAP.put("DECIMAL",ColumnType.NUMBER);
		DB2_SCHEMA_TYPE_MAP.put("MONEY",ColumnType.MONEY);
		DB2_SCHEMA_TYPE_MAP.put("DATE",ColumnType.DATE);
		DB2_SCHEMA_TYPE_MAP.put("TIMESTAMP",ColumnType.TIMESTAMP);
		DB2_SCHEMA_TYPE_MAP.put("BLOB",ColumnType.BLOB);
		DB2_SCHEMA_TYPE_MAP.put("DBCLOB",ColumnType.CLOB);
		
		ORACLE_SCHEMA_TYPE_MAP.put("VARCHAR2",ColumnType.VARCHAR);
		ORACLE_SCHEMA_TYPE_MAP.put("CHAR",ColumnType.CHAR);
		ORACLE_SCHEMA_TYPE_MAP.put("INTEGER",ColumnType.INTEGER);
		ORACLE_SCHEMA_TYPE_MAP.put("LONG",ColumnType.LONG);
		ORACLE_SCHEMA_TYPE_MAP.put("NUMBER",ColumnType.NUMBER);
		ORACLE_SCHEMA_TYPE_MAP.put("MONEY",ColumnType.MONEY);
		ORACLE_SCHEMA_TYPE_MAP.put("DATE",ColumnType.DATE);
		ORACLE_SCHEMA_TYPE_MAP.put("TIMESTAMP",ColumnType.TIMESTAMP);
		ORACLE_SCHEMA_TYPE_MAP.put("BLOB",ColumnType.BLOB);
		ORACLE_SCHEMA_TYPE_MAP.put("CLOB",ColumnType.CLOB);
		
		INFORMIX_SCHEMA_TYPE_MAP.put("varchar",ColumnType.VARCHAR);
		INFORMIX_SCHEMA_TYPE_MAP.put("char",ColumnType.CHAR);
		INFORMIX_SCHEMA_TYPE_MAP.put("integer",ColumnType.INTEGER);
		INFORMIX_SCHEMA_TYPE_MAP.put("long",ColumnType.LONG);
		INFORMIX_SCHEMA_TYPE_MAP.put("decimal",ColumnType.NUMBER);
		INFORMIX_SCHEMA_TYPE_MAP.put("money",ColumnType.MONEY);
		INFORMIX_SCHEMA_TYPE_MAP.put("date",ColumnType.DATE);
		INFORMIX_SCHEMA_TYPE_MAP.put("datetime",ColumnType.TIMESTAMP);
		INFORMIX_SCHEMA_TYPE_MAP.put("blob",ColumnType.BLOB);
		INFORMIX_SCHEMA_TYPE_MAP.put("clob",ColumnType.CLOB);
		
		/////////////////////////////
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.VARCHAR, "VARCHAR");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.CHAR, "CHAR");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.INTEGER, "INTEGER");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.LONG, "BIGINT");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.NUMBER, "DECIMAL");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.MONEY, "MONEY");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.DATE, "DATE");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.TIMESTAMP, "TIMESTAMP");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.BLOB, "BLOB");
		SCHEMA_DB2_TYPE_MAP.put(ColumnType.CLOB, "DBCLOB");
		
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.VARCHAR, "VARCHAR2");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.CHAR, "CHAR");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.INTEGER, "INTEGER");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.LONG, "LONG");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.NUMBER, "NUMBER");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.MONEY, "MONEY");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.DATE, "DATE");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.TIMESTAMP, "TIMESTAMP");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.BLOB, "BLOB");
		SCHEMA_ORACLE_TYPE_MAP.put(ColumnType.CLOB, "CLOB");
		
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.VARCHAR, "varchar");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.CHAR, "char");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.INTEGER, "integer");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.LONG, "long");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.NUMBER, "decimal");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.MONEY, "money");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.DATE, "DATE");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.TIMESTAMP, "datetime");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.BLOB, "blob");
		SCHEMA_INFORMIX_TYPE_MAP.put(ColumnType.CLOB, "clob");
	}
	/**
	 * 根据建模字段类型，获取真实字段类型
	 * @author leven
	 * 2012-11-21 11:48:02
	 * @param type 建模字段类型
	 * @param dbType 数据库类型
	 * @return
	 */
	public static String getType(String type,String dbType){
		if(dbType.toUpperCase().indexOf("ORACLE")>-1){
			return SCHEMA_ORACLE_TYPE_MAP.get(ColumnType.getColumnType(type));
		}else if (dbType.toUpperCase().indexOf("DB2")>-1){
			return SCHEMA_DB2_TYPE_MAP.get(ColumnType.getColumnType(type));
		}else if (dbType.toUpperCase().indexOf("INFORMIX")>-1){
			return SCHEMA_INFORMIX_TYPE_MAP.get(ColumnType.getColumnType(type));
		}else {
			return "VARCHAR";
		}
	}
	/**
	 * 根据真实字段类型，获取建模字段类型
	 * @author leven
	 * 2012-11-21 11:48:02
	 * @param type 真实字段类型
	 * @param dbType 数据库类型
	 * @return
	 */
	public static ColumnType getSchemaType(String type,String dbType){
		if(dbType.toUpperCase().indexOf("ORACLE")>-1){
			return ORACLE_SCHEMA_TYPE_MAP.get(type);
		}else if (dbType.toUpperCase().indexOf("DB2")>-1){
			return DB2_SCHEMA_TYPE_MAP.get(type);
		}else if (dbType.toUpperCase().indexOf("INFORMIX")>-1){
			return INFORMIX_SCHEMA_TYPE_MAP.get(type);
		}else {
			return ColumnType.VARCHAR;
		}
	}
	
}
