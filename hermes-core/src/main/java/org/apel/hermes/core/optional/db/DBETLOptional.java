package org.apel.hermes.core.optional.db;

import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.pager.Condition;
import org.apel.hermes.core.common.DataSourceFetureDesc;
import org.apel.hermes.core.common.UpdateMode;
import org.apel.hermes.core.listener.ConvertHandler;
import org.apel.hermes.core.optional.ETLOptional;

/**
 * 数据库同步参数类
 * @author lijian
 *
 */
public interface DBETLOptional extends ETLOptional{
	
	boolean multiThreadPaging();
	
	DBETLOptional multiThreadPaging(boolean multiThreadPaging);
	
	boolean isPaging();
	
	DBETLOptional isPaging(boolean isPaging);
	
	UpdateMode updateMode();
	
	DBETLOptional updateMode(UpdateMode updateMode);
	
	DBETLOptional fromTableName(String fromTableName);
	
	String fromTableName();
	
	DBETLOptional toTableName(String toTableName);
	
	String toTableName();
	
	DBETLOptional fromSql(String fromSql);
	
	String fromSql();
	
	DBETLOptional fromCountSql(String fromCountSql);
	
	String fromCountSql();
	
	DBETLOptional addtionFields(Map<String,Object> addtionFields);
	
	Map<String,Object> addtionFields();
	
	DBETLOptional excludeFields(List<String> excludeFields);
	
	List<String> excludeFields();
	
	DBETLOptional dataSourceFetureDesc(DataSourceFetureDesc dataSourceFetureDesc);
	
	DataSourceFetureDesc dataSourceFetureDesc();
	
	DBETLOptional recursion(boolean recursion);
		
	boolean recursion();
		
	DBETLOptional recursionField(String recursionField);
		
	String recursionField();
		
		
	DBETLOptional recursionFieldValue(Object recursionFieldValue);
		
	Object recursionFieldValue();
	
	DBETLOptional isCheckTarget(boolean isCheckTarget);
	
	boolean isCheckTarget();
	
	DBETLOptional checkCondtions(List<Condition> checkCondtions);
	
	List<Condition> checkCondtions();
	
	DBETLOptional inputPk(String inputPk);
	
	String inputPk();
	
	DBETLOptional outputPk(String outputPk);
	
	String outputPk();
	
	DBETLOptional checkPK();
	
	boolean recursionDelete();
	
	DBETLOptional recursionDelete(boolean recursionDelete);
	
	String recursionDeleteField();
	
	DBETLOptional recursionDeleteField(String recursionDeleteField);
	
	Map<String, String> contrast();
	
	DBETLOptional contrast(Map<String, String> contrast);
	
	DBETLOptional convertHandler(ConvertHandler convertHandler);
	
	ConvertHandler convertHandler();
}
