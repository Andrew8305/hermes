package org.apel.hermes.core.step.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.core.TablePageable;
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
	
	protected static final int DEFAULT_PAGE_SIZE = 500;

	@Override
	public int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public List<Map<String, Object>> extract(ETLResource inputETLResource) {
		throw new IllegalStateException("DB分页ETL步骤不允许自定义extract方法");
	}
	
	@Override
	public void doStep(DBETLResource inputResource,
			DBETLResource outputResource, StepLogCollector stepLogCollector) {
		if(dbOptional.recursion()){//自关联表的递归提取
			stepLogCollector.addLog("进行递归提取数据,提取源语句:" + this.getMainSql());
			recursionExtract(inputResource, outputResource, dbOptional.recursionFieldValue());
		}else if(dbOptional.isPaging()){//分页提取
			stepLogCollector.addLog("进行分页提取数据,提取源语句:" + this.getMainSql());
			pagingExtract(inputResource, outputResource, stepLogCollector);
		}else{//常规提取
			stepLogCollector.addLog("进行常规提取数据,提取源语句:" + this.getMainSql());
			ordinaryExtract(inputResource, outputResource, stepLogCollector);
		}
	}
	
	/**
	 * 自关联表的递归提取
	 */
	void recursionExtract(DBETLResource inputResource,
			DBETLResource outputResource, Object recursionFieldValue){
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
			
			Map<String, Object> convertedSingleData = convert(map, inputResource, outputResource, this.dbOptional);
			
			//监听器回调点
			ETLListenerUtil.stepAfterConvert(stepListener, inputResource, outputResource, dbOptional);
			
			if(convertedSingleData != null){
				newData.add(convertedSingleData);
			}
		}
		
		//监听器回调点
		ETLListenerUtil.stepBeforeLoad(stepListener, inputResource, outputResource, dbOptional, newData);
		
		load(newData, outputResource, dbOptional);
		
		//监听器回调点
		ETLListenerUtil.stepAfterLoad(stepListener, inputResource, outputResource, dbOptional);
		
		for (Map<String, Object> map : queryResult) {
			recursionExtract(inputResource, outputResource, map.get(dbOptional.inputPk()));
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
		
		public StepPagingThread(DBETLResource inputETLResource,
				DBETLResource outputETLResource, int currentPage, String sql, StepLogCollector stepLogCollector) {
			this.inputETLResource = inputETLResource;
			this.outputETLResource = outputETLResource;
			this.currentPage = currentPage;
			this.sql = sql;
			this.stepLogCollector = stepLogCollector;
		}

		@Override
		public String call() throws Exception {
			pageDetail(inputETLResource, outputETLResource, sql, currentPage, stepListener, stepLogCollector);
			return null;
		}
		
	}

	/**
	 * 分页提取方法
	 */
	private void pagingExtract(DBETLResource inputResource,
			DBETLResource outputResource, StepLogCollector stepLogCollector) {
		int totalRecord = inputResource.getJdbcTemplate().queryForObject(getCountSql(), Integer.class);
		String mainSql = getMainSql();
		int totalPage = (totalRecord  +  DEFAULT_PAGE_SIZE  - 1) / DEFAULT_PAGE_SIZE;
		if(dbOptional.multiThreadPaging()){
			if(totalPage == 1){//当分页数据只有一页时，不开线程，直接执行
				pageDetail(inputResource, outputResource, mainSql, 1, stepListener, stepLogCollector);
			}else{//当分页数据有多页时，多线程执行
				PagingThreadPool<String> threadPool = new PagingThreadPool<>(totalPage);
				for (int i = 1; i <= totalPage; i++) {
					//对每一页的数据进行ETL逻辑
					threadPool.addCallable(new StepPagingThread(inputResource, outputResource, i, mainSql, stepLogCollector));
				}
				//执行线程池中的线程
				threadPool.execute();
			}
		}else{
			for (int i = 1; i <= totalPage; i++) {
				//对每一页的数据进行ETL逻辑
				pageDetail(inputResource, outputResource, mainSql, i, stepListener, stepLogCollector);
			}
		}
	}

	private void pageDetail(DBETLResource inputResource,
			DBETLResource outputResource, String mainSql, int i, StepListener stepListener, StepLogCollector stepLogCollector) {
		String pageSql = 
				DBTypeEnumPager.convert(inputResource.type()).doPage(mainSql, DEFAULT_PAGE_SIZE, i, dbOptional);
		doDetail(inputResource, outputResource, pageSql, stepLogCollector);
	}

	private void doDetail(DBETLResource inputResource,
			DBETLResource outputResource, String sql, StepLogCollector stepLogCollector) {
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
			
			Map<String, Object> convertedSingleData = convert(extractSingleData, inputResource, outputResource, dbOptional);
			
			//监听器回调点
			ETLListenerUtil.stepAfterConvert(stepListener, inputResource, outputResource, dbOptional);
			
			if(convertedSingleData != null){
				convertedData.add(convertedSingleData);
			}
		}
		
		//监听器回调点
		ETLListenerUtil.stepBeforeLoad(stepListener, inputResource, outputResource, dbOptional, convertedData);
		
		StepLogCollector loadLog = load(convertedData, outputResource, dbOptional);
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
			DBETLResource outputResource, StepLogCollector stepLogCollector) {
		doDetail(inputResource, outputResource, getMainSql(), stepLogCollector);
	}

	
	
}
