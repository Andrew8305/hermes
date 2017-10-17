package org.apel.hermes.core.step.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.core.common.CheckCondition;
import org.apel.hermes.core.common.CheckCondition.CheckResult;
import org.apel.hermes.core.common.DataSourceFeture;
import org.apel.hermes.core.common.UpdateMode;
import org.apel.hermes.core.exception.DBETLException;
import org.apel.hermes.core.listener.StepListener;
import org.apel.hermes.core.optional.ETLOptional;
import org.apel.hermes.core.optional.db.DBETLOptional;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.resource.db.DBETLResource;
import org.apel.hermes.core.util.DBUtil;
import org.springframework.util.CollectionUtils;

/**
 * 数据库同步智能step，可用于工具step直接由泛型工厂创建
 * @author lijian
 *
 */
public class SmartDBETLStep extends MultiFunctionDBETLStep implements StepListener {
	
	private static Logger LOG = Logger.getLogger(SmartDBETLStep.class);
	
	private static final String ORACLE_ROW_NUM_FIELD = "r_";
	
	@Override
	public String getCountSql() {
		if(!StringUtils.isEmpty(optional().fromCountSql())){
			return optional().fromCountSql();
		}else{
			return "select count(*) from " + optional().fromTableName();
		}
	}

	@Override
	public String getMainSql() {
		if(!StringUtils.isEmpty(optional().fromSql())){
			return optional().fromSql();
		}else{
			return "select * from " + optional().fromTableName();
		}
	}

	@Override
	public Map<String, Object> convert(Map<String, Object> rawRow,
			ETLResource inputETLResource,
			ETLResource outputETLResource, ETLOptional optional, String runtimeVersionId) {
		//排除oracle的rownum字段
		rawRow.remove(ORACLE_ROW_NUM_FIELD.toUpperCase());
		rawRow.remove(ORACLE_ROW_NUM_FIELD.toLowerCase());
		
		if(dbOptional.excludeFields().size() != 0){//排除指定的字段
			for (String excludeField : dbOptional.excludeFields()) {
				rawRow.remove(excludeField);
			}
		}
		if(dbOptional.addtionFields().size() != 0){//附加格外的字段
			rawRow.putAll(dbOptional.addtionFields());
		}
		if(dbOptional.dataSourceFetureDesc() != null){//附加数据库特性字段
			if(dbOptional.dataSourceFetureDesc().getDataSourceFeture() == DataSourceFeture.ID){
				rawRow.put(dbOptional.dataSourceFetureDesc().getId(), inputETLResource.id());
			}else if(dbOptional.dataSourceFetureDesc().getDataSourceFeture() == DataSourceFeture.NAME){
				rawRow.put(dbOptional.dataSourceFetureDesc().getName(), inputETLResource.name());
			}else{
				rawRow.put(dbOptional.dataSourceFetureDesc().getId(), inputETLResource.id());
				rawRow.put(dbOptional.dataSourceFetureDesc().getName(), inputETLResource.name());
			}
		}
		//如果要进行数据的版本控制，则添加版本字段
		if (this.optional().checkVersion()){
			rawRow.put(getVersionCheckField(), runtimeVersionId);
		}
		Map<String, Object> resultRow = new HashMap<>();
		if(dbOptional.contrast().size() != 0){//转换输入源和输出源的字段信息
			for (String key : rawRow.keySet()) {
				if(dbOptional.contrast().get(key) != null){
					resultRow.put(dbOptional.contrast().get(key), rawRow.get(key));
				}else{
					resultRow.put(key, rawRow.get(key));
				}
			}
		}else{
			resultRow = rawRow;
		}
		//如果参数中含有回调接口，则调用回调接口进行转换
		if(this.optional().convertHandler() != null){
			resultRow = this.optional().convertHandler().convert(resultRow, inputETLResource, outputETLResource, optional);
		}
		return resultRow;
	}

	@Override
	public StepLogCollector load(List<Map<String, Object>> newData,
			ETLResource outputETLResource, ETLOptional optional, String runtimeVersionId) {
		StepLogCollector stepLogCollector = StepLogCollector.create();
		DBETLResource outputResource = (DBETLResource)outputETLResource;
		//如果在目标表中没有出现之前获取的数据时，要将之排除
		newData = deleteWrongColumns(newData, outputResource);
		if(newData.size() == 0){
			return stepLogCollector;
		}
		//加载条数计数器
		int count = 0;
		for (Map<String, Object> singleNewData : newData) {
			//计算insert字段和update字段
			String insertMainSql = "INSERT INTO " + this.dbOptional.toTableName();
			String updateMainSql = "UPDATE " + this.dbOptional.toTableName() + " SET ";
			StringBuffer fieldSb = new StringBuffer();
			StringBuffer valuesSb = new StringBuffer();
			StringBuffer updateSb = new StringBuffer();
			List<Object> params = new ArrayList<>();
			List<String> fields = new ArrayList<>();
			for (String field : singleNewData.keySet()) {
				fieldSb.append(field + ",");
				valuesSb.append("?,");
				updateSb.append(field + "=?,");
				fields.add(field);
			}
			for (String field : fields) {
				params.add(singleNewData.get(field));
			}
			insertMainSql += 
					" (" + fieldSb.subSequence(0, fieldSb.length() - 1) + ")"
					+ " VALUES (" + valuesSb.substring(0, valuesSb.length() - 1) + ")";
			updateMainSql += updateSb.subSequence(0, updateSb.length() - 1);
			try {
				//当需要对目标数据源进行检查时
				if(this.dbOptional.isCheckTarget()){
					CheckResult checkResult = CheckCondition.checkSql(outputResource, dbOptional, singleNewData);
					String countSql = "SELECT COUNT(*) FROM " + dbOptional.toTableName() + checkResult.getWhereClause();
					Integer checkCount = outputResource.getJdbcTemplate().queryForObject(countSql, Integer.class, checkResult.getValues().toArray());
					if(checkCount != 0){
						if(this.dbOptional.updateMode() == UpdateMode.UPDATE){//更新模式，只更新目标源数据，不删除已有数据
							params.addAll(checkResult.getValues());
							outputResource.getJdbcTemplate().update(updateMainSql + checkResult.getWhereClause(), params.toArray());
						}else if(this.dbOptional.updateMode() == UpdateMode.DELETE){//删除模式，先删除已有数据，再插入
							if(dbOptional.recursionDelete()){//递归删除
								Object pkValue = singleNewData.get(dbOptional.outputPk());
								recursiveDelete(outputResource, pkValue, dbOptional, checkResult);
							}else{//普通删除
								String deleteSql = "DELETE FROM " + dbOptional.toTableName() + checkResult.getWhereClause();
								outputResource.getJdbcTemplate().update(deleteSql, checkResult.getValues().toArray());
							}
							outputResource.getJdbcTemplate().update(insertMainSql, params.toArray());
							count++;
						}else{//忽略模式
							LOG.info("忽略模式，检查目标数据库存在指定数据，但并不做任何操作");
						}
					}else{
						outputResource.getJdbcTemplate().update(insertMainSql, params.toArray());
						count++;
					}
				}else {
					outputResource.getJdbcTemplate().update(insertMainSql, params.toArray());
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage());
				stepLogCollector.setResult(false);
				stepLogCollector.setResultDesc(e.getMessage());
			}
		}
		stepLogCollector.addLog("加载影响了数据" + count + "条");
		return stepLogCollector;
	}

	private List<Map<String, Object>> deleteWrongColumns(List<Map<String, Object>> newData, DBETLResource outputResource) {
		List<Map<String, Object>> result = new ArrayList<>();
		if(!CollectionUtils.isEmpty(newData)){
			try (Connection conn = outputResource.getDs().getConnection();){
				List<String> columnNames = DBUtil.getColumnNames(conn, this.dbOptional.toTableName());
				for (Map<String, Object> singleNewData : newData) {
					Map<String, Object> deletedWrongSingleData = new HashMap<>();
					c:for (String columnName : columnNames) {
						if(singleNewData.get(columnName) != null){
							deletedWrongSingleData.put(columnName, singleNewData.get(columnName));
							continue c;
						}
						if(singleNewData.get(columnName.toLowerCase()) != null){
							deletedWrongSingleData.put(columnName.toLowerCase(), singleNewData.get(columnName.toLowerCase()));
							continue c;
						}
						if(singleNewData.get(columnName.toUpperCase()) != null){
							deletedWrongSingleData.put(columnName.toUpperCase(), singleNewData.get(columnName.toUpperCase()));
						}
					}
					if(deletedWrongSingleData.size() != 0){
						result.add(deletedWrongSingleData);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	private void recursiveDelete(DBETLResource outputResource, Object pk, DBETLOptional dbOptional, CheckResult checkResult) {
		String searchSql = "SELECT " + dbOptional.outputPk() + " FROM " + dbOptional.toTableName() + " WHERE " + dbOptional.recursionDeleteField() + " = ?";
		List<Map<String, Object>> childrenData = outputResource.getJdbcTemplate().queryForList(searchSql, pk);
		if(childrenData.size() != 0){
			for (Map<String, Object> map : childrenData) {
				recursiveDelete(outputResource, map.get(dbOptional.outputPk()), dbOptional, checkResult);
			}
		}
		String deleteSql = "DELETE FROM " + dbOptional.toTableName() + " where " + dbOptional.outputPk() + "=?";
		outputResource.getJdbcTemplate().update(deleteSql, pk);
	}

	@Override
	public String taskId() {
		return null;
	}
	
	@Override
	public void check() {
		if(StringUtils.isEmpty(optional().fromSql()) && StringUtils.isEmpty(optional().fromTableName())){
			String errorMsg = "数据库同步任务[" + taskId + "]步骤[" + order() + "]中没有识别到提取数据的sql语句，请检查是否配置了同步参数fromTableName或fromSql";
			LOG.error(errorMsg);
			throw new DBETLException(errorMsg);
		}
		if(StringUtils.isEmpty(optional().fromSql()) && StringUtils.isEmpty(optional().fromTableName())){
			String errorMsg = "数据库同步任务[" + taskId + "]步骤[" + order() + "]中没有识别到提取数据的sql语句，请检查是否配置了同步参数fromTableName或fromSql";
			LOG.error(errorMsg);
			throw new DBETLException(errorMsg);
		}
		if(StringUtils.isEmpty(optional().toTableName())){
			String errorMsg = "数据库同步任务[" + taskId + "]步骤[" + order() + "]中没有识别到存储数据的sql，请检查是否配置了同步参数toTableName";
			LOG.error(errorMsg);
			throw new DBETLException(errorMsg);
		}
		if(dbOptional.recursion()){
			if(StringUtils.isEmpty(dbOptional.recursionField())
					|| StringUtils.isEmpty(dbOptional.inputPk())){
				String errorMsg = "数据库同步任务[" + taskId + "]步骤[" + order() + "]中递归读取数据中没有指定递归字段或主键字段，请检查是否配置了同步参数recursionField或者pk";
				LOG.error(errorMsg);
				throw new DBETLException(errorMsg);
			}
		}
		if(dbOptional.isCheckTarget()){
			if(dbOptional.checkCondtions().size() == 0){
				String errorMsg = "数据库同步任务[" + taskId + "]步骤[" + order() + "]中检查操作当中没有指定检查字段，请检查是否配置了同步参数checkCondtions";
				LOG.error(errorMsg);
				throw new DBETLException(errorMsg);
			}
		}
	}
	
	@Override
	public void beforeExtract(ETLResource inputEtlResource,
			ETLResource outputEtlResource, ETLOptional optional) {
		
	}

	@Override
	public void afterExtract(ETLResource inputEtlResource,
			ETLResource outputEtlResource, ETLOptional optional) {
		
	}

	@Override
	public void beforeConvert(ETLResource inputEtlResource,
			ETLResource outputEtlResource, ETLOptional optional) {
		
	}

	@Override
	public void afterConvert(ETLResource inputEtlResource,
			ETLResource outputEtlResource, ETLOptional optional) {
		
	}

	@Override
	public void beforeLoad(ETLResource inputEtlResource,
			ETLResource outputEtlResource, ETLOptional optional, List<Map<String, Object>> data) {
		
	}

	@Override
	public void afterLoad(ETLResource inputEtlResource,
			ETLResource outputEtlResource, ETLOptional optional) {
		
	}

	@Override
	public void onStart(ETLResource inputEtlResource,
			ETLResource outputEtlResource) {
		
	}

	@Override
	public void onEnd(ETLResource inputEtlResource,
			ETLResource outputEtlResource) {
		
	}
	
}
