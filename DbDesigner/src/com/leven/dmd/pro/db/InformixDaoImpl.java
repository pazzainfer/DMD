package com.leven.dmd.pro.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.ColumnTypeMatcher;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.pro.domain.FieldDomain;
import com.leven.dmd.pro.domain.TableDomain;
import com.leven.dmd.pro.pref.model.DBConfigModel;

public class InformixDaoImpl implements IDatabaseOPDao {
	
	public List<FieldDomain> getFieldListByTableName(
			String tableName, DBConfigModel jdbcInfo, Connection conn) {
		List<FieldDomain> fieldDomainList = new ArrayList<FieldDomain>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			String strSql = "select colname,collength,coltype,nullable,case coltype "+
				"when '0' then 'CHAR' "+
				"when '1' then 'SMALLINT' "+
				"when '2' then 'INTEGER' "+
				"when '3' then 'FLOAT' "+
				"when '4' then 'SMALLFLOAT' "+
				"when '5' then 'DECIMAL' "+
				"when '6' then 'SERIAL' "+
				"when '7' then 'DATE' "+
				"when '8' then 'MONEY' "+
				"when '9' then 'NULL' "+
				"when '10' then 'DATETIME' "+
				"when '11' then 'BYTE' "+
				"when '12' then 'TEXT' "+
				"when '13' then 'VARCHAR' "+
				"when '14' then 'INTERVAL' "+
				"when '15' then 'NCHAR' "+
				"when '16' then 'NVARCHAR'"+
				"when '17' then 'INT8' "+
				"when '18' then 'SERIAL8' "+
				"when '19' then 'SET' "+
				"when '20' then 'MULTISET' "+
				"when '21' then 'LIST' "+
				"when '22' then 'Unnamed ROW' "+
				"when '40' then 'LVARCHAR' "+
				"when '41' then 'CLOB' "+
				"when '43' then 'BLOB' "+
				"when '44' then 'BOOLEAN' "+
				"when '256' then 'CHAR' "+
				"when '257' then 'SMALLINT' "+
				"when '258' then 'INTEGER' "+
				"when '259' then 'FLOAT' "+
				"when '260' then 'REAL' "+
				"when '261' then 'DECIMAL' "+
				"when '262' then 'SERIAL' "+
				"when '263' then 'DATE' "+
				"when '264' then 'MONEY' "+
				"when '266' then 'DATETIME' "+
				"when '267' then 'BYTE' "+
				"when '268' then 'TEXT' "+
				"when '269' then 'VARCHAR' "+
				"when '270' then 'INTERVAL' "+
				"when '271' then 'NCHAR' "+
				"when '272' then 'NVARCHAR'"+
				"when '273' then 'INT8' "+
				"when '274' then 'SERIAL8' "+
				"when '275' then 'SET' "+
				"when '276' then 'MULTISET'"+ 
				"when '277' then 'LIST' "+
				"when '278' then 'Unnamed ROW' "+ 
				"when '296' then 'LVARCHAR' "+
				"when '297' then 'CLOB' "+
				"when '298' then 'BLOB' "+
				"when '299' then 'BOOLEAN' "+
				"when '4118' then 'Named ROW' "+
				"end as typename from syscolumns where tabid=(select tabid from systables where tabname=?)";
			pst = conn.prepareStatement(strSql);
			pst.setString(1, tableName);
			rs = pst.executeQuery();
			FieldDomain info = null;
			while(rs.next()){
				info = new FieldDomain();
				info.setTableName(tableName);
				info.setFieldName(rs.getString("colname"));
				info.setFieldLenth(rs.getInt("collength"));
				info.setFieldType(rs.getString("typename"));
				info.setPrimaryKey(rs.getInt("nullable")==0 ? true : false);
				fieldDomainList.add(info);
			}
		}catch(SQLException e) {
			
		}finally{
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(pst!=null){
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				
			}
		}
		return fieldDomainList;
	}

	public List<TableDomain> getTableList(DBConfigModel config, Connection conn) {
		List<TableDomain> fieldDomainList = new ArrayList<TableDomain>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			String strSql = "select * from systables where owner=? order by tabname";
			pst = conn.prepareStatement(strSql);
			pst.setString(1, config.getUsername());
			rs = pst.executeQuery();
			TableDomain info = null;
			while(rs.next()){
				info = new TableDomain();
				info.setTableName(rs.getString("tabname"));
				info.setCnName("");
				info.setOwner(config.getUsername());
				fieldDomainList.add(info);
			}
		}catch(SQLException e) {
			
		}finally{
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(pst!=null){
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				
			}
		}
		return fieldDomainList;
	}

	public Map<String, FieldDomain> getFieldMapByTableName(String tableName,
			DBConfigModel config, Connection conn) {
		Map<String, FieldDomain> fieldMap = new HashMap<String, FieldDomain>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			String strSql = "select colname,collength,coltype,nullable,case coltype "+
				"when '0' then 'CHAR' "+
				"when '1' then 'SMALLINT' "+
				"when '2' then 'INTEGER' "+
				"when '3' then 'FLOAT' "+
				"when '4' then 'SMALLFLOAT' "+
				"when '5' then 'DECIMAL' "+
				"when '6' then 'SERIAL' "+
				"when '7' then 'DATE' "+
				"when '8' then 'MONEY' "+
				"when '9' then 'NULL' "+
				"when '10' then 'DATETIME' "+
				"when '11' then 'BYTE' "+
				"when '12' then 'TEXT' "+
				"when '13' then 'VARCHAR' "+
				"when '14' then 'INTERVAL' "+
				"when '15' then 'NCHAR' "+
				"when '16' then 'NVARCHAR'"+
				"when '17' then 'INT8' "+
				"when '18' then 'SERIAL8' "+
				"when '19' then 'SET' "+
				"when '20' then 'MULTISET' "+
				"when '21' then 'LIST' "+
				"when '22' then 'Unnamed ROW' "+
				"when '40' then 'LVARCHAR' "+
				"when '41' then 'CLOB' "+
				"when '43' then 'BLOB' "+
				"when '44' then 'BOOLEAN' "+
				"when '256' then 'CHAR' "+
				"when '257' then 'SMALLINT' "+
				"when '258' then 'INTEGER' "+
				"when '259' then 'FLOAT' "+
				"when '260' then 'REAL' "+
				"when '261' then 'DECIMAL' "+
				"when '262' then 'SERIAL' "+
				"when '263' then 'DATE' "+
				"when '264' then 'MONEY' "+
				"when '266' then 'DATETIME' "+
				"when '267' then 'BYTE' "+
				"when '268' then 'TEXT' "+
				"when '269' then 'VARCHAR' "+
				"when '270' then 'INTERVAL' "+
				"when '271' then 'NCHAR' "+
				"when '272' then 'NVARCHAR'"+
				"when '273' then 'INT8' "+
				"when '274' then 'SERIAL8' "+
				"when '275' then 'SET' "+
				"when '276' then 'MULTISET'"+ 
				"when '277' then 'LIST' "+
				"when '278' then 'Unnamed ROW' "+ 
				"when '296' then 'LVARCHAR' "+
				"when '297' then 'CLOB' "+
				"when '298' then 'BLOB' "+
				"when '299' then 'BOOLEAN' "+
				"when '4118' then 'Named ROW' "+
				"end as typename from syscolumns where tabid=(select tabid from systables where tabname=?)";
			pst = conn.prepareStatement(strSql);
			pst.setString(1, tableName);
			rs = pst.executeQuery();
			FieldDomain info = null;
			while(rs.next()){
				info = new FieldDomain();
				info.setTableName(tableName);
				info.setFieldName(rs.getString("colname"));
				info.setFieldLenth(rs.getInt("collength"));
				info.setFieldType(rs.getString("typename"));
				info.setPrimaryKey(rs.getInt("nullable")==0 ? true : false);
				fieldMap.put(tableName, info);
			}
		}catch(SQLException e) {
			
		}finally{
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(pst!=null){
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				
			}
		}
		return fieldMap;
	}

	public List<String> getTableSqls(Table table, boolean ifExists) {
		List<String> sqlList = new ArrayList<String>();
		ArrayList<Column> columns  = table.getColumns();
		StringBuffer tableSql = new StringBuffer("");
		List<String> commentList = new ArrayList<String>();
		if(ifExists){
			sqlList.add("DROP TABLE "+table.getName().toUpperCase());
		}
		if(table.isQuote()){
			int index = table.getQuotePath().lastIndexOf("/");
			String Quoted = index>=0?table.getQuotePath().substring(index+1):table.getQuotePath();
			sqlList.add("CREATE TABLE "+table.getName().toUpperCase()+" AS SELECT * FROM "+Quoted.toUpperCase());
		}else {
			tableSql.append("CREATE TABLE "+table.getName().toUpperCase()+" (" );
			
			ArrayList<String> keys = new ArrayList<String>();
			if(!columns.isEmpty()){
				for(int i=0;i<columns.size();i++){
					Column column = columns.get(i);
					tableSql.append(column.getName().toUpperCase()+" "+ColumnTypeMatcher.SCHEMA_INFORMIX_TYPE_MAP.get(ColumnType.getColumnType(column.getType())));
					if(ColumnType.hasLength(column.getType())){
						if(ColumnType.hasScale(column.getType())){
							tableSql.append("("+column.getLength()+","+column.getScale()+")");
						}else {
							tableSql.append("("+column.getLength()+")");
						}
					}
					if(column.isPk()){
						keys.add(column.getName());
						tableSql.append(" not null");
					}
					if(i!=columns.size()-1){
						tableSql.append(",");
					}
					if(column.getCnName()!=null && !column.getCnName().equals("")){
						commentList.add("COMMENT ON COLUMN "+table.getName().toUpperCase()+"."+ column.getName().toUpperCase()+" IS '"+column.getCnName()+"'");
					}
				}
			}		
			tableSql.append(")");
			if(table.getTablespace()!=null && !"".equals(table.getTablespace())){
				tableSql.append("TABLESPACE ");
				tableSql.append(table.getTablespace());
			}
			sqlList.add(tableSql.toString());
			if(table.getTablespace()!=null && !"".equals(table.getTablespace())){
				sqlList.add(table.getOtherSql());
			}
			if(keys.size()>0){
				tableSql = new StringBuffer();
				tableSql.append("alter table " + table.getName().toUpperCase() + " add primary key(");
				for(int i=0;i<keys.size();i++){
					tableSql.append(keys.get(i));
					if(i != (keys.size()-1)){
						tableSql.append(",");
					} else {
						tableSql.append(")");
					}
				}
				sqlList.add(table.getOtherSql());
			}
			
			if(table.getIndexes().size()>0){
				TableIndex indexTemp;
				for(Iterator<TableIndex> it = table.getIndexes().iterator();it.hasNext();){
					indexTemp = it.next();
					if(!indexTemp.isAutoCreated()){
						tableSql = new StringBuffer();
						tableSql.append("CREATE INDEX ");
						tableSql.append(indexTemp.getName());
						tableSql.append(" ON "+table.getName()+" (" + indexTemp.getColumns() + ")");
						sqlList.add(table.getOtherSql());
					}
				}
			}
		}
		sqlList.addAll(commentList);
		return sqlList;
	}

	public String getTableDDL(Table table) {
		ArrayList<Column> columns  = table.getColumns();
		StringBuffer tableSql = new StringBuffer("");
		StringBuffer commentSql = new StringBuffer("");
		
		tableSql.append(" DROP TABLE "+table.getName().toUpperCase()+"; \n" );
		
		if(table.isQuote()){
			int index = table.getQuotePath().lastIndexOf("/");
			String Quoted = index>=0?table.getQuotePath().substring(index+1):table.getQuotePath();
			tableSql.append(" CREATE TABLE "+table.getName().toUpperCase()+" AS SELECT * FROM "+Quoted.toUpperCase()+";\n");
		}else {
			tableSql.append(" CREATE TABLE "+table.getName().toUpperCase()+" ( " );
			commentSql.append (" COMMENT ON TABLE "+table.getName().toUpperCase()+" IS '"+table.getCnName()+"'; \n");
			
			ArrayList<String> keys = new ArrayList<String>();
			if(!columns.isEmpty()){
				for(int i=0;i<columns.size();i++){
					Column column = columns.get(i);
					tableSql.append(column.getName().toUpperCase()+" "+ColumnTypeMatcher.SCHEMA_INFORMIX_TYPE_MAP.get(ColumnType.getColumnType(column.getType())));
					if(ColumnType.hasLength(column.getType())){
						if(ColumnType.hasScale(column.getType())){
							tableSql.append("("+column.getLength()+","+column.getScale()+")");
						}else {
							tableSql.append("("+column.getLength()+")");
						}
					}
					if(column.isPk()){
						keys.add(column.getName());
						tableSql.append(" not null");
					}
					if(i!=columns.size()-1){
						tableSql.append(", \n");
					}
					if(column.getCnName()!=null && !column.getCnName().equals("")){
						commentSql.append(" COMMENT ON COLUMN "+table.getName().toUpperCase()+"."+ column.getName().toUpperCase()+" IS '"+column.getCnName()+"' ; \n" );
					}
				}
			}		
			tableSql.append("\n) ");
			if(table.getTablespace()!=null && !"".equals(table.getTablespace())){
				tableSql.append("TABLESPACE ");
				tableSql.append(table.getTablespace());
			}
			tableSql.append(";\n");
			if(table.getTablespace()!=null && !"".equals(table.getTablespace())){
				tableSql.append(table.getOtherSql());
				tableSql.append("\n");
			}
			if(keys.size()>0){
				tableSql.append("alter table " + table.getName().toUpperCase() + " add primary key(");
				for(int i=0;i<keys.size();i++){
					tableSql.append(keys.get(i));
					if(i != (keys.size()-1)){
						tableSql.append(",");
					} else {
						tableSql.append(");\n");
					}
				}
			}
			
			if(table.getIndexes().size()>0){
				TableIndex indexTemp;
				for(Iterator<TableIndex> it = table.getIndexes().iterator();it.hasNext();){
					indexTemp = it.next();
					if(!indexTemp.isAutoCreated()){
						tableSql.append("CREATE INDEX ");
						tableSql.append(table.getName() + "_" + indexTemp.getColumns().replaceAll(",", "_"));
						tableSql.append(" ON "+table.getName()+" (" + indexTemp.getColumns() + ");\n");
					}
				}
			}
		}
		return tableSql.toString()+commentSql.toString();
	}
}
