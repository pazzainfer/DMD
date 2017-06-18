package com.leven.dmd.pro.db;

import com.leven.dmd.pro.domain.DBTypeConstant;

public class DBAdapter {
	/**
	 * 根据数据库类型标识符，返回数据库操作对象。
	 * @param type 数据库类型
	 * @return
	 */
	public static IDatabaseOPDao getAdapter(String type) {
		IDatabaseOPDao domain = null;
		if(type==null){
			return null;
		}else if(DBTypeConstant.ORACLE_THIN.getType().equals(type)){
			domain = new OracleThinDaoImpl();
		}else if(DBTypeConstant.DB2.getType().equals(type)){
			domain = new DB2DaoImpl();
		}else if(DBTypeConstant.INFORMIX.getType().equals(type)){
			domain = new InformixDaoImpl();
		}
		return domain;
	}

}
