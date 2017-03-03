package org.apel.hermes.core;

/**
 * 数据库同步逻辑当中的分页接口
 * @author lijian
 *
 */
public interface TablePageable {
	
	String getCountSql();
	
	String getMainSql();
	
	int getPageSize();
	
}
