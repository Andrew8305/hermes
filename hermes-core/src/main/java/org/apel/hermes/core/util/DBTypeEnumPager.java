package org.apel.hermes.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apel.hermes.core.exception.DBETLException;
import org.apel.hermes.core.optional.db.DBETLOptional;

/**
 * 策略枚举
 * 用于对不同类型的数据库数据源进行分页
 * @author lijian
 *
 */
public enum DBTypeEnumPager {
	
	MYSQL("MySQL") {
		@Override
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional) {
			int offset = (page - 1) * rowPerPage;
			sql = sql + " LIMIT " + offset + "," + rowPerPage;
			return sql;
		}
	},MSSQL("MsSql") {
		@Override
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional) {
			if(StringUtils.isEmpty(optional.fromTableName())
					|| StringUtils.isEmpty(optional.inputPk())){
				throw new DBETLException("sqlserver数据库使用分页，必须指定inputPk和fromTableName");
			}
			String pageSql = "SELECT TOP " + rowPerPage + " * FROM " + optional.fromTableName() 
					+ " WHERE " + optional.inputPk() + " NOT IN(SELECT TOP " 
					+ (page - 1)*rowPerPage + " " + optional.inputPk() + " FROM " + optional.fromTableName() + ")";
			return pageSql;
		}
	},ORACLE("Oracle") {
		@Override
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional) {
			return toOraclePageSql(sql, page, rowPerPage);
		}
	},DM("DM DBMS") {
		@Override
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional) {
			return toOraclePageSql(sql, page, rowPerPage);
		}
	};
	
	private static String toOraclePageSql(String sql, int page, int rowPerPage){
		int startRowNum = (page - 1) * rowPerPage + 1;
		int endRowNum = startRowNum + rowPerPage - 1;
		String pageSql = 
		"SELECT * FROM ( "
		+ "		SELECT rownum r_,a.* "
		+ "		FROM ( "
		+ 			sql
		+ "			) a "
		+ "		) b "
		+ "WHERE b.r_>=" + startRowNum + " AND b.r_<=" + endRowNum;
		return pageSql;
	}
	
	public abstract String doPage(String sql, int rowPerPage, int page, DBETLOptional optional);
	
	public static DBTypeEnumPager convert(String desc){
		for (DBTypeEnumPager em : DBTypeEnumPager.values()) {
			if(em.desc.equals(desc)){
				return em;
			}
		}
		return null;
	}
	
	private String desc;
	
	private DBTypeEnumPager(String desc){
		this.desc = desc;
	}
	
	public String toString(){
		return this.desc;
	}
	
}
