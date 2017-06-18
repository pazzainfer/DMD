package com.leven.dmd.pro.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.ColumnTypeMatcher;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.gef.model.TablespaceFile;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.pro.db.DBOperator;
import com.leven.dmd.pro.domain.FieldDomain;
import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DbTableUtil {
	/**
	 * 根据数据表对象，将表创建至数据库中
	 * @param table
	 * @param ifExists
	 * @param config
	 * @return 是否创建成功
	 */
	public static boolean createTableByDomian(Table table,boolean ifExists,DBConfigModel config){
		Connection conn;
		try {
			conn = DBUtil.getConnection(config);
			List<String> createTableSql = DBOperator.getTableSqls(table,ifExists,config.getType());
			PreparedStatement rateNormInsPs=null;
			if(conn!=null){
				try {
					conn.setAutoCommit(false);
					for(String sql : createTableSql){
						try {
							if("".equals(sql.trim())){
								continue;
							}
							rateNormInsPs = conn.prepareStatement(sql);
							rateNormInsPs.executeUpdate();
							rateNormInsPs.close();
						} catch (SQLException e) {
							System.out.print(e.toString());
							System.out.println(sql);
							System.out.println("/--------------------------------------/");
							throw e;
						}
					}
					
					conn.commit();
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					DBUtil.rollBack(conn);
					conn.setAutoCommit(true);
					DBUtil.close(conn);
					return false;
				} finally {
					if(rateNormInsPs!=null){
						rateNormInsPs.close();
					}
					conn.close();
				}
			}
		} catch (Exception e1) {
			return false;
		}
		return true;
	}
	public static boolean CheckTableExist(Table table,DBConfigModel config){
		boolean exist = true;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(config);
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			rs = databaseMetaData.getTables(null, config.getUsername().toUpperCase(), table.getName().toUpperCase(), new String[]{"TABLE"});
			if(rs.next()){
				exist = true;
			} else {
				exist = false;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return true;
		} finally{
			try {
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exist;
	}
	/**
	 * 返回视图创建语句
	 * @param view
	 * @param dbType
	 * @return
	 */
	public static String getViewSql(DBView view, String dbType) {
		StringBuffer content = new StringBuffer();
		try {
			if(view != null){
				content.append("CREATE VIEW ");
				content.append(view.getName());
				content.append(" AS ");
				content.append(view.getQuerySql());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return content.toString();
	}
	
	public static String getTablespaceSql(Tablespace space, String dbType) {
		StringBuffer content = new StringBuffer();
		try {
			if(space != null){
				content.append("CREATE TABLESPACE ");
				content.append(space.getName());
				content.append(" DATAFILE ");
				for(int i=0;i<space.getFileList().size();i++){
					TablespaceFile file = space.getFileList().get(i);
					content.append("'");
					content.append(file.getFilePath());
					content.append("' size ");
					content.append(file.getSize());
					content.append("M");
					if(i!=(space.getFileList().size()-1)){
						content.append(",");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return content.toString();
	}
	
	public static String getSchemaDllContent(Schema schema,String dbType) {
		Table table;
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < schema.getAllTables().size(); i++) {
			table = schema.getAllTables().get(i);
			try {
				content.append(DBOperator.getTableDDL(table,dbType));
				content.append("\n");
			} catch (Exception e) {
				continue;
			}
		}
		for(SequenceTemplate sequence : schema.getSchemaTemplate().getSequenceTemplates()){
			content.append(getSequenceSql(sequence,dbType));
			content.append("\n");
		}
		return content.toString();
	}
	
	public static String getSequenceSql(SequenceTemplate sequence,String dbType){
		StringBuffer sb = new StringBuffer();
		if(sequence.getDescription()!=null && !"".equals(sequence.getDescription())){
			sb.append("/* ");
			sb.append(sequence.getDescription());
			sb.append(" */\n");
		}
		if(dbType.toLowerCase().contains("oracle")){
			sb.append("CREATE SEQUENCE ");
			sb.append(sequence.getName());
			sb.append(" INCREMENT BY ");
			sb.append(sequence.getIncrementby());
			sb.append(" START WITH ");
			sb.append(sequence.getStartwith());
			if(sequence.getMinvalue().equals("NOMINVALUE")){
				sb.append(" NOMINVALUE");
			}else {
				sb.append(" MINVALUE ");
				sb.append(sequence.getMinvalue());
			}
			if(sequence.getMaxvalue().equals("NOMAXVALUE")){
				sb.append(" NOMAXVALUE");
			}else {
				sb.append(" MAXVALUE ");
				sb.append(sequence.getMaxvalue());
			}
			if(sequence.isCycle()){
				sb.append(" CYCLE");
			}else {
				sb.append(" NOCYCLE");
			}
			if(sequence.getCache().equals("NOCACHE")){
				sb.append(" NOCACHE");
			}else {
				sb.append(" CACHE ");
				sb.append(sequence.getCache());
			}
		} else if(dbType.toLowerCase().contains("db2")){
			sb.append("CREATE SEQUENCE ");
			sb.append(sequence.getName());
			sb.append(" AS INTEGER START WITH ");
			sb.append(sequence.getStartwith());
			sb.append(" INCREMENT BY ");
			sb.append(sequence.getIncrementby());
			if(sequence.getMinvalue().equals("NOMINVALUE")){
				sb.append(" NO MINVALUE");
			}else {
				sb.append(" MINVALUE ");
				sb.append(sequence.getMinvalue());
			}
			if(sequence.getMaxvalue().equals("NOMAXVALUE")){
				sb.append(" NO MAXVALUE");
			}else {
				sb.append(" MAXVALUE ");
				sb.append(sequence.getMaxvalue());
			}
			if(sequence.isCycle()){
				sb.append(" CYCLE");
			}else {
				sb.append(" NO CYCLE");
			}
			if(sequence.getCache().equals("NOCACHE")){
				sb.append(" NO CACHE");
			}else {
				sb.append(" CACHE ");
				sb.append(sequence.getCache());
				sb.append(" ORDER;");
			}
		} else {
			return "";
		}
		return sb.toString();
	}
	
	public static String getPackageDllContent(TablePackage tablePackage,String dbType) {
		Table table;
		StringBuffer content = new StringBuffer();
		for (int j = 0; j < tablePackage.getTables().size(); j++) {
			table = tablePackage.getTables().get(j);
			try {
				content.append(DBOperator.getTableDDL(table,dbType));
				content.append("\n");
			} catch (Exception e) {
				continue;
			}
		}
		for (TablePackage tablePackage1 : tablePackage.getTablePackages()) {
			content.append(getPackageDllContent(tablePackage1,dbType));
		}
		return content.toString();
	}
	
	public static Schema checkTablesToDbBySchema(Schema schema,DBConfigModel config){
		Connection conn = null;
		List<Table> tableList = schema.getAllTables();
		Table table  = null;
		try {
			conn = DBUtil.getConnection(config);
			
			for(int j=0;j<tableList.size();j++){
				 table = tableList.get(j);
				 Map<String,FieldDomain> dbMap = DBOperator.getFieldMapByTableName(table.getName(), config, conn);
				if(dbMap!=null && dbMap.size()>0){
					if(!checkMap(table.getColumns(),dbMap,config.getType())){
						table.modifyStatus(IEditorConstant.TABLE_STATUS_CHANGED);
					 }else{
						 table.modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
					 }
				}else{
					table.modifyStatus(IEditorConstant.TABLE_STATUS_ADDED);
				}	 
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return schema;
	}
	private static boolean checkMap(List<Column> colList, Map<String, FieldDomain> fldmap, String dbType) {
		if (colList == null || fldmap == null)
			return true;
		else if (colList.size() != fldmap.size())
			return false;
		else if (colList.size() == 0 && fldmap.size() == 0)
			return true;
		else {
			for (Column col : colList) {
				FieldDomain fld = fldmap.get(col.getName().toUpperCase());
				if(fld == null){
					fld = fldmap.get(col.getName().toLowerCase());
				}
				if (fld != null) {
					if (null==col.getCnName()||"".equals(col.getCnName())){
						if(null!=fld.getFieldComment()&&!"".equals(fld.getFieldComment())) {
							return false;
						}
					}else {
						if(null==fld.getFieldComment()||"".equals(fld.getFieldComment())) {
							return false;
						}else if(!fld.getFieldComment().equals(col.getCnName())){
							return false;
						}
					}
					if (null == col.getType() || !ColumnTypeMatcher.getType(col.getType(), dbType).equals(fld.getFieldType())) {
							return false;
					}else {
						if(ColumnType.hasLength(col.getType())){
							if(col.getLength() != fld.getFieldLenth()){
								return false;
							}
						}
						if(ColumnType.hasScale(col.getType())){
							if(col.getScale() != fld.getFieldScale()){
								return false;
							}
						}
					}
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}
}