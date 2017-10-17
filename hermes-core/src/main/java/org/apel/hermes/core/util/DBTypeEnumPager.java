package org.apel.hermes.core.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apel.hermes.core.enums.InputPKType;
import org.apel.hermes.core.exception.DBETLException;
import org.apel.hermes.core.optional.db.DBETLOptional;
import org.apel.hermes.core.resource.db.DBETLResource;

/**
 * 策略枚举
 * 用于对不同类型的数据库数据源进行分页
 * @author lijian
 *
 */
public enum DBTypeEnumPager {
	
	MYSQL("MySQL") {
		@Override
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional, DBETLResource inputETLResource) {
			int offset = (page - 1) * rowPerPage;
			sql = sql + " LIMIT " + offset + "," + rowPerPage;
			return sql;
		}
	},MSSQL("MsSql") {
		@Override
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional, DBETLResource inputETLResource) {
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
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional, DBETLResource inputETLResource) {
			return getOracleRealPageSql(sql, rowPerPage, page, optional, inputETLResource);
		}
	},DM("DM DBMS") {
		@Override
		public String doPage(String sql, int rowPerPage, int page, DBETLOptional optional, DBETLResource inputETLResource) {
			return getOracleRealPageSql(sql, rowPerPage, page, optional, inputETLResource);
		}
		
	};
	
	private static String getOracleRealPageSql(String sql, int rowPerPage,
			int page, DBETLOptional optional, DBETLResource inputETLResource) {
		String pagingSearchIdSql = optional.pagingSearchIdSql();
		if (StringUtils.isEmpty(pagingSearchIdSql)){
			if(StringUtils.isEmpty(optional.fromTableName())
					|| StringUtils.isEmpty(optional.inputPk())){
				throw new DBETLException("oracle增强分页[pagingSearchIdSql]，必须指定inputPk和fromTableName");
			}
			pagingSearchIdSql = "select " + optional.inputPk() + " from " + optional.fromTableName();
		}
		String oraclePageIdSql = toOraclePageSql(pagingSearchIdSql, page, rowPerPage);
		List<Map<String, Object>> ids = inputETLResource.getJdbcTemplate().queryForList(oraclePageIdSql);
		String realPagingSql = "";
		if (ids.size() > 0){
			String idFilterClause = optional.idFilterClause();
			InputPKType pkType = optional.inputPKType();
			if (StringUtils.isEmpty(idFilterClause)){
				idFilterClause = " where " + optional.inputPk() + " in (?)";
			}
			StringBuffer replaceSb = new StringBuffer();
			switch (pkType) {
			case LONG:
				for (Map<String, Object> map : ids) {
					Object object = map.get(optional.inputPk());
					replaceSb.append(object.toString() + ",");
				}
				break;
			default:
				for (Map<String, Object> map : ids) {
					Object object = map.get(optional.inputPk());
					replaceSb.append("'" + object.toString() + "',");
				}
				break;
			}
			idFilterClause = idFilterClause.replace("?", replaceSb.subSequence(0, replaceSb.length() - 1));
			realPagingSql = sql + " " + idFilterClause;
		}else{
			String noneClause = optional.noneClause();
			if (StringUtils.isEmpty(noneClause)){
				noneClause = " where 1=2";
			}
			realPagingSql = sql + " " + noneClause;
		}
		return realPagingSql;
	}
	
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
	
	public abstract String doPage(String sql, int rowPerPage, int page, DBETLOptional optional, DBETLResource inputETLResource);
	
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
