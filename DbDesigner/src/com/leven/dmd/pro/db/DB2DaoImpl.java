package com.leven.dmd.pro.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class DB2DaoImpl implements IDatabaseOPDao {
	
	public List<FieldDomain> getFieldListByTableName(
			String tableName, DBConfigModel config, Connection conn) {
		List<FieldDomain> fieldList = new ArrayList<FieldDomain>();
		Statement stat = null;
		ResultSet rs = null;
		try{
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from syscat.columns where tabname='"+tableName+"' and (tabschema=current schema or tabschema='"+config.getUsername().toUpperCase()+"')");
			FieldDomain field;
			while(rs.next()){
				field = new FieldDomain();
				String colname = rs.getString("colname");
				field.setTableName(colname);
				field.setFieldName(colname.trim().toLowerCase());
				field.setFieldComment(rs.getString("remarks"));
				if(field.getFieldComment()==null){
					field.setFieldComment(field.getFieldName());
				}
				field.setFieldType(rs.getString("typename"));
				field.setFieldLenth(rs.getInt("length"));
				field.setFieldScale(rs.getInt("scale"));
				Integer key = rs.getInt("keyseq");
				if(key!=null && key==1){
					field.setPrimaryKey(true);
					fieldList.add(0, field);
				}else {
					fieldList.add(field);
				}
			}
		} catch(Exception e){
			
		}finally{
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(stat!=null){
					stat.close();
					stat=null;
				}
			} catch (SQLException e1) {
				
			}
		}
		return fieldList;
	}

	public List<TableDomain> getTableList(DBConfigModel config, Connection conn) {
		List<TableDomain> tableList = new ArrayList<TableDomain>();
		Statement stat = null;
		ResultSet rs = null;
		try{
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from syscat.tables where tabschema=current schema or tabschema='"+config.getUsername()+"'");
			TableDomain table;
			while(rs.next()){
				table = new TableDomain();
				table.setTableName(rs.getString("tabname"));
				table.setCnName(rs.getString("remarks"));
				tableList.add(table);
			}
		} catch(Exception e){
			
		}finally{
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(stat!=null){
					stat.close();
					stat=null;
				}
			} catch (SQLException e1) {
				
			}
		}
		return tableList;
	}


	public Map<String, FieldDomain> getFieldMapByTableName(String tableName,DBConfigModel config, Connection conn) {
		Map<String, FieldDomain> map = new HashMap<String, FieldDomain>();
		Statement stat = null;
		ResultSet rs = null;
		try{
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from syscat.columns where tabname='"+tableName+"' and tabschema=current schema or tabschema='"+config.getUsername().toUpperCase()+"'");
			FieldDomain field;
			while(rs.next()){
				field = new FieldDomain();
				String colname = rs.getString("colname");
				field.setTableName(colname);
				field.setFieldName(colname.trim().toLowerCase());
				field.setFieldComment(rs.getString("remarks"));
				if(field.getFieldComment()==null){
					field.setFieldComment(field.getFieldName());
				}
				field.setFieldType(rs.getString("typename"));
				field.setFieldLenth(rs.getInt("length"));
				field.setFieldScale(rs.getInt("scale"));
				Integer key = rs.getInt("keyseq");
				if(key!=null && key==1){
					field.setPrimaryKey(true);
				}
				map.put(field.getFieldName(),field);
			}
		} catch(Exception e){
			
		}finally{
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(stat!=null){
					stat.close();
					stat=null;
				}
			} catch (SQLException e1) {
				
			}
		}
		return map;
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
					tableSql.append(column.getName().toUpperCase()+" "+ColumnTypeMatcher.SCHEMA_DB2_TYPE_MAP.get(ColumnType.getColumnType(column.getType())));
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
				tableSql.append("in ");
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
		
		tableSql.append(" DROP TABLE "+table.getName().toUpperCase()+" ; \n" );
		
		if(table.isQuote()){
			int index = table.getQuotePath().lastIndexOf("/");
			String Quoted = index>=0?table.getQuotePath().substring(index+1):table.getQuotePath();
			tableSql.append(" CREATE TABLE "+table.getName().toUpperCase()+" AS SELECT * FROM "+Quoted.toUpperCase()+";\n");
		}else {
			tableSql.append(" CREATE TABLE "+table.getName().toUpperCase()+" ( " );
			commentSql.append (" COMMENT ON TABLE "+table.getName().toUpperCase()+"  IS '"+table.getCnName()+"'; \n");
			
			ArrayList<String> keys = new ArrayList<String>();
			if(!columns.isEmpty()){
				for(int i=0;i<columns.size();i++){
					Column column = columns.get(i);
					tableSql.append(column.getName().toUpperCase()+" "+ColumnTypeMatcher.SCHEMA_DB2_TYPE_MAP.get(ColumnType.getColumnType(column.getType())));
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
				tableSql.append("in ");
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