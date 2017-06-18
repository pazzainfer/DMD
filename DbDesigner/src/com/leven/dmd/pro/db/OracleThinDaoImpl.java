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
import com.leven.dmd.pro.util.DBUtil;

public class OracleThinDaoImpl  implements IDatabaseOPDao {

	public List<FieldDomain> getFieldListByTableName(String tableName,DBConfigModel config, Connection conn){
		List<FieldDomain> list = new ArrayList<FieldDomain>();
		PreparedStatement pst = null;
		PreparedStatement priPst = null;
		ResultSet priRs = null;
		ResultSet rs = null;
		StringBuffer sbf = null;
		StringBuffer priSbf = null;
		String primaryKey = "";
		try{
			priSbf = new StringBuffer("");
			priSbf.append("select e.column_name from user_cons_columns e")
				  .append(" inner join user_constraints f on e.TABLE_NAME = f.TABLE_NAME")
				  .append(" and f.CONSTRAINT_TYPE = ?")
				  .append(" where e.TABLE_NAME= ?");

			priPst = conn.prepareStatement(priSbf.toString());
			priPst.setString(1, "P");
			priPst.setString(2, tableName.toUpperCase());
			priRs = priPst.executeQuery();
			while(priRs.next()) {
				primaryKey += priRs.getString("column_name") + ",";
			}
			
			String[] keys = primaryKey.split(",");
			
			sbf = new StringBuffer("");
			sbf.append("select atc.COLUMN_NAME as COLUMN_NAME, atc.DATA_TYPE as DATA_TYPE,atc.DATA_PRECISION,atc.DATA_LENGTH,atc.DATA_SCALE,com.COMMENTS as COMMENTS   from ")
			.append("all_tab_columns atc,(SELECT * FROM USER_COL_COMMENTS WHERE TABLE_NAME=?)com ")
			.append("where atc.TABLE_NAME = ?  and atc.COLUMN_NAME=com.COLUMN_NAME and atc.OWNER=?");
			String sql = sbf.toString();
			pst = conn.prepareStatement(sql);
			pst.setString(1, tableName.toUpperCase());
			pst.setString(2, tableName.toUpperCase());
			pst.setString(3, config.getUsername().toUpperCase());
			rs = pst.executeQuery();
			while(rs.next()) {
				String fieldName = rs.getString("COLUMN_NAME");
				String dataType = rs.getString("DATA_TYPE");
				int fieldLenth = dataType.equals("NUMBER")?rs.getInt("DATA_PRECISION"):rs.getInt("DATA_LENGTH");
				int fieldScale = rs.getInt("DATA_SCALE");
				String comment = rs.getString("COMMENTS");
				FieldDomain fieldDomain = new FieldDomain();
				fieldDomain.setTableName(fieldName);
				fieldDomain.setFieldName(fieldName.trim().toLowerCase());
				fieldDomain.setFieldType(dataType.trim());
				fieldDomain.setFieldLenth(fieldLenth);
				fieldDomain.setFieldScale(fieldScale);
				fieldDomain.setFieldComment(comment==null?"":comment.trim());
				for(String key : keys){
					if(fieldName.equals(key)){
						fieldDomain.setPrimaryKey(true);
					}
				}
				
				if(fieldDomain.getFieldComment()!=null && fieldDomain.getFieldComment().contains("\n")) {
					String newComment = fieldDomain.getFieldComment().replace("\n", "");
					fieldDomain.setFieldComment(newComment);
				}
				list.add(fieldDomain);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(rs);
			DBUtil.close(pst);
		}
		return list;
	}
	
	public List<TableDomain> getTableList(DBConfigModel config, Connection conn) {
		List<TableDomain> tableElements = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			tableElements = new ArrayList<TableDomain>();
			String sql = "select a.TABLE_NAME,b.comments from all_tables a,all_tab_comments b where a.owner = ? and b.OWNER=? and a.table_name=b.table_name order by TABLE_NAME";
			pst = conn.prepareStatement(sql);
			pst.setString(1, config.getUsername().toUpperCase());
			pst.setString(2, config.getUsername().toUpperCase());
			rs = pst.executeQuery();
			while(rs.next()) {
				TableDomain tableNode = new TableDomain();
				String tableName = rs.getString(1);
				String tableComment= rs.getString(2);
				if(tableComment!=null && !tableComment.equals("")) {
					tableNode.setCnName(tableComment);
				}else {
					tableNode.setCnName(tableName);
				}
				tableNode.setTableName(tableName);
				tableElements.add(tableNode);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pst);
		
		}
		return tableElements;
	}
	public Map <String,FieldDomain> getFieldMapByTableName(String tableName,DBConfigModel config,Connection conn){
		Map<String,FieldDomain> map = new HashMap<String,FieldDomain>();
		PreparedStatement pst = null;
		PreparedStatement priPst = null;
		ResultSet priRs = null;
		ResultSet rs = null;
		StringBuffer sbf = null;
		StringBuffer priSbf = null;
		String primaryKey = "";
		try{
			priSbf = new StringBuffer("");
			priSbf.append("select e.column_name from user_cons_columns e")
				  .append(" inner join user_constraints f on e.TABLE_NAME = f.TABLE_NAME")
				  .append(" and f.CONSTRAINT_TYPE = ?")
				  .append(" where e.TABLE_NAME= ?");

			priPst = conn.prepareStatement(priSbf.toString());
			priPst.setString(1, "P");
			priPst.setString(2, tableName.toUpperCase());
			priRs = priPst.executeQuery();
			while(priRs.next()) {
				primaryKey += priRs.getString("column_name") + ",";
			}
			
			String[] keys = primaryKey.split(",");
			
			sbf = new StringBuffer("");
			sbf.append("select atc.COLUMN_NAME as COLUMN_NAME, atc.DATA_TYPE as DATA_TYPE,atc.DATA_PRECISION,atc.DATA_LENGTH,atc.DATA_SCALE,com.COMMENTS as COMMENTS   from ")
			.append("all_tab_columns atc,(SELECT * FROM USER_COL_COMMENTS WHERE TABLE_NAME=?)com ")
			.append("where atc.TABLE_NAME = ?  and atc.COLUMN_NAME=com.COLUMN_NAME and atc.OWNER=?");
			String sql = sbf.toString();
			pst = conn.prepareStatement(sql);
			pst.setString(1, tableName.toUpperCase());
			pst.setString(2, tableName.toUpperCase());
			pst.setString(3, config.getUsername().toUpperCase());
			rs = pst.executeQuery();
			while(rs.next()) {
				String fieldName = rs.getString("COLUMN_NAME");
				String dataType = rs.getString("DATA_TYPE");
				int fieldLenth = dataType.equals("NUMBER")?rs.getInt("DATA_PRECISION"):rs.getInt("DATA_LENGTH");
				int fieldScale = rs.getInt("DATA_SCALE");
				String comment = rs.getString("COMMENTS");
				FieldDomain fieldDomain = new FieldDomain();
				fieldDomain.setTableName(fieldName);
				fieldDomain.setFieldName(fieldName.trim().toLowerCase());
				fieldDomain.setFieldType(dataType.trim());
				fieldDomain.setFieldLenth(fieldLenth);
				fieldDomain.setFieldScale(fieldScale);
				fieldDomain.setFieldComment(comment==null?"":comment.trim());
				for(String key : keys){
					if(fieldName.equals(key)){
						fieldDomain.setPrimaryKey(true);
					}
				}
				
				if(fieldDomain.getFieldComment()!=null && fieldDomain.getFieldComment().contains("\n")) {
					String newComment = fieldDomain.getFieldComment().replace("\n", "");
					fieldDomain.setFieldComment(newComment);
				}
				map.put(fieldName.trim().toLowerCase(), fieldDomain);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(rs);
			DBUtil.close(pst);
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
					tableSql.append(column.getName().toUpperCase()+" "+ColumnTypeMatcher.SCHEMA_ORACLE_TYPE_MAP.get(ColumnType.getColumnType(column.getType())));
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
					tableSql.append(column.getName().toUpperCase()+" "+ColumnTypeMatcher.SCHEMA_ORACLE_TYPE_MAP.get(ColumnType.getColumnType(column.getType())));
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
