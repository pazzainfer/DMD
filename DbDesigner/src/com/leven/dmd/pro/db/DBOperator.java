package com.leven.dmd.pro.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.pro.domain.FieldDomain;
import com.leven.dmd.pro.domain.TableDomain;
import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DBOperator {
	/**
	 * 获取指定表中的的所有字段
	 * @param tableName
	 * @param dbConfig
	 * @param conn
	 * @return
	 */
	public static List<FieldDomain> getFieldListByTableName(String tableName, DBConfigModel dbConfig, Connection conn){
		IDatabaseOPDao domain = DBAdapter.getAdapter(dbConfig.getType());
		if(domain!=null){
			return domain.getFieldListByTableName(tableName, dbConfig, conn);
		}
		return null;
	}
	/**
	 * 获取指定表中的的所有字段
	 * @param tableName
	 * @param dbConfig
	 * @param conn
	 * @return
	 */
	public static Map<String,FieldDomain> getFieldMapByTableName(String tableName, DBConfigModel dbConfig,Connection conn){
		IDatabaseOPDao domain = DBAdapter.getAdapter(dbConfig.getType());
		if(domain!=null){
			return domain.getFieldMapByTableName(tableName, dbConfig, conn);
		}
		return null;
	}
	/**
	 * 获取指定库中的所有数据表
	 * @param dbConfig
	 * @param conn
	 * @return
	 */
	public static List<TableDomain> getTableList(DBConfigModel dbConfig,Connection conn){
		IDatabaseOPDao domain = DBAdapter.getAdapter(dbConfig.getType());
		if(domain!=null){
			return domain.getTableList(dbConfig, conn);
		}
		return null;
	}
	/**
	 * 获取创建表所需的所有SQL语句。
	 * @param table
	 * @param ifExists
	 * @param dbType
	 * @return
	 */
	public static List<String> getTableSqls(Table table, boolean ifExists, String dbType) {
		IDatabaseOPDao domain = DBAdapter.getAdapter(dbType);
		if(domain!=null){
			return domain.getTableSqls(table, ifExists);
		}
		return new ArrayList<String>();
	}
	/**
	 * 获取建表SQL(用于导出DDL)
	 */
	public static String getTableDDL(Table table, String dbType) {
		IDatabaseOPDao domain = DBAdapter.getAdapter(dbType);
		if(domain!=null){
			return domain.getTableDDL(table);
		}
		return "";
	}
}
