package com.leven.dmd.pro.domain;

import java.util.HashMap;
import java.util.Map;

public class DBTypeConstant {
	private static Map<String,DBType> dbTypeMap = new HashMap<String,DBType>();
	
	public static String dbTypeArray[] = new String[]{
		"DB2","Informix","JavaDB/Derby server","JavaDB/Derby embedded","JDBC/ODBC Bridge"
		,"Mimer","MySQL","Oracle Thin","Oracle OCI","PostgreSQL","SQL Server","Sybase"
	};
	
	public static DBType DB2 = new DBType("DB2"
			,"jdbc:db2://<server>:<port50000>/<database>"
			,"com.ibm.db2.jcc.DB2Driver");
	public static DBType INFORMIX = new DBType("Informix"
			,"jdbc:informix-sqli://<server>:<port1533>/<database>:informixserver=<dbservername>"
			,"com.informix.jdbc.IfxDriver");
	public static DBType DERBY_SERVER = new DBType("JavaDB/Derby server"
			,"jdbc:derby://<server>:<port1527>/<databaseName>"
			,"org.apache.derby.jdbc.ClientDriver");
	public static DBType DERBY_EMBEDDED = new DBType("JavaDB/Derby embedded"
			,"jdbc:derby:<databaseName>"
			,"org.apache.derby.jdbc.ClientDriver");
	public static DBType JDBC_ODBC = new DBType("JDBC/ODBC Bridge"
			,"jdbc:odbc:<datasource>","sun.jdbc.odbc.JdbcOdbcDriver");
	public static DBType MIMER = new DBType("Mimer"
			,"jdbc:mimer:<protocol>://<server>:<port1360>/<database>"
			,"com.mimer.jdbc.Driver");
	public static DBType MYSQL = new DBType("MySQL"
			,"jdbc:mysql://<server>:<port3306>/<database>"
			,"com.mysql.jdbc.Driver");
	public static DBType ORACLE_THIN = new DBType("Oracle Thin"
			,"jdbc:oracle:thin:@<server>:<port1521>:<sid>"
			,"oracle.jdbc.driver.OracleDriver");
	public static DBType ORACLE_OCI = new DBType("Oracle OCI"
			,"jdbc:oracle:oci:@<server>:<port1521>:<sid>"
			,"oracle.jdbc.driver.OracleDriver");
	public static DBType POSTGRESQL = new DBType("PostgreSQL"
			,"jdbc:postgresql://<server>:<port5432>/<database>"
			,"org.postgresql.Driver");
	public static DBType SQL_SERVER = new DBType("SQL Server"
			,"jdbc:jtds:sqlserver://<server>:<port1433>;DatabaseName=<database>"
			,"com.microsoft.jdbc.sqlserver.SQLServerDriver");
	public static DBType SYBASE = new DBType("Sybase"
			,"jdbc:jtds:sybase://<server>:<port5000>;DatabaseName=<database>"
			,"com.sybase.jdbc.SybDriver");
	
	static {
		dbTypeMap.put("DB2", DB2);
		dbTypeMap.put("Informix", INFORMIX);
		dbTypeMap.put("JavaDB/Derby server", DERBY_SERVER);
		dbTypeMap.put("JavaDB/Derby embedded", DERBY_EMBEDDED);
		dbTypeMap.put("JDBC/ODBC Bridge", JDBC_ODBC);
		dbTypeMap.put("Mimer", MIMER);
		dbTypeMap.put("MySQL", MYSQL);
		dbTypeMap.put("Oracle Thin", ORACLE_THIN);
		dbTypeMap.put("Oracle OCI", ORACLE_OCI);
		dbTypeMap.put("PostgreSQL", POSTGRESQL);
		dbTypeMap.put("SQL Server", SQL_SERVER);
		dbTypeMap.put("Sybase", SYBASE);
	}
	
	public static DBType getDBType(String type){
		return dbTypeMap.get(type);
	}
}
