package com.leven.dmd.pro.db;
 
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.pro.domain.FieldDomain;
import com.leven.dmd.pro.domain.TableDomain;
import com.leven.dmd.pro.pref.model.DBConfigModel;
/**
 * 数据库操作接口
 * @author leven
 * create at 2013-11-1 上午3:13:28
 */
public interface IDatabaseOPDao {
	/**
	 * 获取指定表名下的所有字段列表
	 * @param tableName 表名
	 * @param config 数据源配置
	 * @param conn 数据库链接
	 * @return 字段集合
	 */
	public List<FieldDomain> getFieldListByTableName(String tableName,DBConfigModel config, Connection conn);
	/**
	 * 获取指定表名下的所有字段
	 * @param tableName 表名
	 * @param config 数据源配置
	 * @param conn 数据库链接
	 * @return 字段MAP
	 */
	public Map<String,FieldDomain> getFieldMapByTableName(String tableName,DBConfigModel config,Connection conn);
	/**
	 * 返回库中所有的数据表集合
	 * @param config 数据源配置
	 * @param conn 数据库链接
	 * @return
	 */
	public List<TableDomain> getTableList(DBConfigModel config,Connection conn) ;
	/**
	 * 返回建表的SQL语句
	 * @param table
	 * @param ifExists 是否已存在表(需要drop语句?)
	 * @return
	 */
	public List<String> getTableSqls(Table table, boolean ifExists) ;
	/**
	 * 返回建表的SQL语句,用于DDL导出
	 * @param table
	 * @return
	 */
	public String getTableDDL(Table table) ;
}
