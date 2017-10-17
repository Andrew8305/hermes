package org.apel.hermes.core.step.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.core.TablePageable;
import org.apel.hermes.core.common.DataSourceFetureDesc;
import org.apel.hermes.core.listener.StepListener;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.resource.db.DBETLResource;
import org.apel.hermes.core.thread.PagingThreadPool;
import org.apel.hermes.core.util.DBTypeEnumPager;
import org.apel.hermes.core.util.ETLListenerUtil;


/**
 * 数据库同步的分页step
 * @author lijian
 *
 * @param <K>
 * @param <V>
 */
public abstract class MultiFunctionDBETLStep extends GenericDBETLStep implements TablePageable{
	
	protected static final int DEFAULT_ROWS_PER_PAGE = 50;
	protected static final String DEFAULT_VERSION_CHECK_FIELD = "CHECK_VERSION";

	@Override
	public int getRowsPerPage() {
		return DEFAULT_ROWS_PER_PAGE;
	}

	@Override
	public List<Map<String, Object>> extract(ETLResource inputETLResource) {
		throw new IllegalStateException("DB分页ETL步骤不允许自定义extract方法");
	}
	
	@Override
	public void doStep(DBETLResource inputResource,
			DBETLResource outputResource, StepLogCollector stepLogCollector, String runtimeVersionId) {
		//检查同步数据的版本字段
		checkVersionField(outputResource);
		
		if(dbOptional.recursion()){//自关联表的递归提取
			stepLogCollector.addLog("进行递归提取数据,提取源语句:" + this.getMainSql());
			recursionExtract(inputResource, outputResource, dbOptional.recursionFieldValue(), runtimeVersionId);
		}else if(dbOptional.isPaging()){//分页多线程提取
			stepLogCollector.addLog("进行分页提取数据,提取源语句:" + this.getMainSql());
			pagingExtract(inputResource, outputResource, stepLogCollector, runtimeVersionId);
		}else{//常规提取
			stepLogCollector.addLog("进行常规提取数据,提取源语句:" + this.getMainSql());
			ordinaryExtract(inputResource, outputResource, stepLogCollector, runtimeVersionId);
		}
		
		//检查版本信息将目标库中的脏数据清楚
		handlDirtyDataByVersion(inputResource, outputResource, runtimeVersionId);
	}
	

	/**
	 * 自关联表的递归提取
	 */
	void recursionExtract(DBETLResource inputResource,
			DBETLResource outputResource, Object recursionFieldValue, String runtimeVersionId){
		String sql = "";
		if(recursionFieldValue == null){
			sql = getMainSql() + " where " + dbOptional.recursionField() + " is null";
		}else{
			sql = getMainSql() + " where " + dbOptional.recursionField() + "=?";
		}
		
		//监听器回调点
		ETLListenerUtil.stepBeforeExtract(stepListener, inputResource, outputResource, dbOptional);
		List<Map<String, Object>> queryResult = null;
		if(recursionFieldValue == null){
			queryResult = inputResource.getJdbcTemplate().queryForList(sql);
		}else{
			queryResult = inputResource.getJdbcTemplate().queryForList(sql, recursionFieldValue);
		}
		//监听器回调点
		ETLListenerUtil.stepAfterExtract(stepListener, inputResource, outputResource, dbOptional);
		
		List<Map<String, Object>> newData = new ArrayList<>();
		for (Map<String, Object> map : queryResult) {
			//监听器回调点
			ETLListenerUtil.stepBeforeConvert(stepListener, inputResource, outputResource, dbOptional);
			
			Map<String, Object> convertedSingleData = convert(map, inputResource, outputResource, this.dbOptional,runtimeVersionId);
			
			//监听器回调点
			ETLListenerUtil.stepAfterConvert(stepListener, inputResource, outputResource, dbOptional);
			
			if(convertedSingleData != null){
				newData.add(convertedSingleData);
			}
		}
		
		//监听器回调点
		ETLListenerUtil.stepBeforeLoad(stepListener, inputResource, outputResource, dbOptional, newData);
		
		load(newData, outputResource, dbOptional, runtimeVersionId);
		
		//监听器回调点
		ETLListenerUtil.stepAfterLoad(stepListener, inputResource, outputResource, dbOptional);
		
		for (Map<String, Object> map : queryResult) {
			recursionExtract(inputResource, outputResource, map.get(dbOptional.inputPk()), runtimeVersionId);
		}
	}
	
	/**
	 * 分页线程内部类
	 * @author lijian
	 *
	 */
	class StepPagingThread implements Callable<String>{

		private DBETLResource inputETLResource;
		private DBETLResource outputETLResource;
		private int currentPage;
		private String sql;
		private StepLogCollector stepLogCollector;
		private String runtimeVersionId;
		
		public StepPagingThread(DBETLResource inputETLResource,
				DBETLResource outputETLResource, int currentPage, 
				String sql, StepLogCollector stepLogCollector,
				String runtimeVersionId) {
			this.inputETLResource = inputETLResource;
			this.outputETLResource = outputETLResource;
			this.currentPage = currentPage;
			this.sql = sql;
			this.stepLogCollector = stepLogCollector;
			this.runtimeVersionId = runtimeVersionId;
		}

		@Override
		public String call() throws Exception {
			pageDetail(inputETLResource, outputETLResource, sql, currentPage, stepListener, stepLogCollector, runtimeVersionId);
			return null;
		}
		
	}
	
	/**
	 * 检查目标数据库目标表中的版本字段是否存在，如果不存在就ddl创建一个字段
	 */
	private void checkVersionField(DBETLResource outputResource) {
		synchronized (MultiFunctionDBETLStep.class) {
			if (this.dbOptional.checkVersion()){
				String versionCheckField = getVersionCheckField();
				try (Connection conn = outputResource.getDs().getConnection();){
					PreparedStatement pst = conn.prepareStatement("select * from " + this.optional().toTableName());
					ResultSet rs = pst.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					boolean fieldExist = false;
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						String columnName = rsmd.getColumnName(i);
						if (versionCheckField.equalsIgnoreCase(columnName)){
							fieldExist = true;
							break;
						}
					}
					String dataType = "VARCHAR(255)";
					if (outputResource.type().equals("Oracle")){
						dataType = "VARCHAR2(255)";
					}
					if (!fieldExist){
						String addColumnSql = "ALTER TABLE " + this.optional().toTableName() + " ADD " + versionCheckField + " " + dataType;
						conn.createStatement().executeUpdate(addColumnSql);
					}
				} catch (SQLException e) { 
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 清楚目标数据库中目标表的非此次同步版本的脏数据(另组合输入数据源的标识字段条件)
	 */
	private void handlDirtyDataByVersion(DBETLResource inputResource,
			DBETLResource outputResource, String runtimeVersionId) {
		String versionCheckField = getVersionCheckField();
		DataSourceFetureDesc dataSourceFetureDesc = this.optional().dataSourceFetureDesc();
		if (dataSourceFetureDesc != null){
			String dsId = dataSourceFetureDesc.getId();
			if(!StringUtils.isEmpty(dsId)){
				String deleteSql = "delete from " + this.optional().toTableName() 
						+ " where (" + versionCheckField + " <> ? and " + dsId + " = ?)"
						+ " or (" + versionCheckField + " is null and " + dsId + " = ?)";
				outputResource.getJdbcTemplate().update(deleteSql, runtimeVersionId, inputResource.id(), inputResource.id());
			}
		}
	}
	
	private int calculateRowsPerPage(){
		int rowsPerPage = dbOptional.rowsPerPage();
		if(rowsPerPage <= 0 || rowsPerPage > 300){
			rowsPerPage = getRowsPerPage();
		}
		if(rowsPerPage <= 0 || rowsPerPage > 300){
			rowsPerPage = DEFAULT_ROWS_PER_PAGE;
		}
		return rowsPerPage;
	}
	
	/**
	 * 获取版本检查字段
	 * @return
	 */
	protected String getVersionCheckField(){
		String versionCheckField = this.optional().versionCheckField();
		if (StringUtils.isEmpty(versionCheckField)){
			versionCheckField = DEFAULT_VERSION_CHECK_FIELD;
		}
		return versionCheckField;
	}

	/**
	 * 分页提取方法
	 */
	private void pagingExtract(DBETLResource inputResource,
			DBETLResource outputResource, StepLogCollector stepLogCollector, String runtimeVersionId) {
		int totalRecord = inputResource.getJdbcTemplate().queryForObject(getCountSql(), Integer.class);
		String mainSql = getMainSql();
		int rowsPerPage = calculateRowsPerPage();//计算每页显示的条数
		int totalPage = (totalRecord  +  rowsPerPage  - 1) / rowsPerPage;
		if(dbOptional.multiThreadPaging()){
			if(totalPage == 1){//当分页数据只有一页时，不开线程，直接执行
				pageDetail(inputResource, outputResource, mainSql, 1, stepListener, stepLogCollector, runtimeVersionId);
			}else{//当分页数据有多页时，多线程执行
				PagingThreadPool<String> threadPool = new PagingThreadPool<>(dbOptional.pageThreadPoolSize());
				for (int i = 1; i <= totalPage; i++) {
					//对每一页的数据进行ETL逻辑
					threadPool.addCallable(new StepPagingThread(inputResource, outputResource, i, mainSql, stepLogCollector, runtimeVersionId));
				}
				//执行线程池中的线程
				threadPool.execute();
			}
		}else{
			for (int i = 1; i <= totalPage; i++) {
				//对每一页的数据进行ETL逻辑
				pageDetail(inputResource, outputResource, mainSql, i, stepListener, stepLogCollector, runtimeVersionId);
			}
		}
	}

	private void pageDetail(DBETLResource inputResource,
			DBETLResource outputResource, String mainSql, int i, StepListener stepListener, StepLogCollector stepLogCollector, String runtimeVersionId) {
		String pageSql = 
				DBTypeEnumPager.convert(inputResource.type()).doPage(mainSql, calculateRowsPerPage(), i, dbOptional, inputResource);
		doDetail(inputResource, outputResource, pageSql, stepLogCollector, runtimeVersionId);
	}

	private void doDetail(DBETLResource inputResource,
			DBETLResource outputResource, String sql, StepLogCollector stepLogCollector, String runtimeVersionId) {
		//监听器回调点
		ETLListenerUtil.stepBeforeExtract(stepListener, inputResource, outputResource, dbOptional);
		
		List<Map<String, Object>> extractData = inputResource.getJdbcTemplate().queryForList(sql);
		stepLogCollector.addLog("提取了数据" + extractData.size() + "条");
		
		//监听器回调点
		ETLListenerUtil.stepAfterExtract(stepListener, inputResource, outputResource, dbOptional);
		
		List<Map<String, Object>> convertedData = new ArrayList<>();
		for (Map<String, Object> extractSingleData : extractData) {
			//监听器回调点
			ETLListenerUtil.stepBeforeConvert(stepListener, inputResource, outputResource, dbOptional);
			
			Map<String, Object> convertedSingleData = convert(extractSingleData, inputResource, outputResource, dbOptional, runtimeVersionId);
			
			//监听器回调点
			ETLListenerUtil.stepAfterConvert(stepListener, inputResource, outputResource, dbOptional);
			
			if(convertedSingleData != null){
				convertedData.add(convertedSingleData);
			}
		}
		
		//监听器回调点
		ETLListenerUtil.stepBeforeLoad(stepListener, inputResource, outputResource, dbOptional, convertedData);
		
		StepLogCollector loadLog = load(convertedData, outputResource, dbOptional, runtimeVersionId);
		stepLogCollector.getLogs().addAll(loadLog.getLogs());
		stepLogCollector.setResultDesc(loadLog.getResultDesc());
		stepLogCollector.setResult(stepLogCollector.isResult());
		
		//监听器回调点
		ETLListenerUtil.stepAfterLoad(stepListener, inputResource, outputResource, dbOptional);
	}

	/**
	 * 常规提取方法
	 */
	private void ordinaryExtract(DBETLResource inputResource,
			DBETLResource outputResource, StepLogCollector stepLogCollector, String runtimeVersionId) {
		doDetail(inputResource, outputResource, getMainSql(), stepLogCollector, runtimeVersionId);
	}

	
	
}
