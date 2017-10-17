package org.apel.hermes.core.step.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.core.Converter;
import org.apel.hermes.core.Extracter;
import org.apel.hermes.core.Loader;
import org.apel.hermes.core.optional.db.DBETLOptional;
import org.apel.hermes.core.optional.db.DefaultDBETLOptional;
import org.apel.hermes.core.resource.db.DBETLResource;
import org.apel.hermes.core.util.ETLListenerUtil;

/**
 * 数据库同步的通用step
 * @author lijian
 *
 */
public abstract class GenericDBETLStep extends DBETLStep implements Extracter, Converter , Loader{

	public GenericDBETLStep(){
		configOptional(dbOptional);
	}

	protected DBETLOptional dbOptional = new DefaultDBETLOptional();
	
	@Override
	public void doStep(DBETLResource inputResource,
			DBETLResource outputResource, StepLogCollector stepLogCollector, String runtimeVersionId) {
		//监听器回调点
		ETLListenerUtil.stepBeforeExtract(stepListener, inputResource, outputResource, dbOptional);
		
		List<Map<String, Object>> rawData = extract(inputResource);
		stepLogCollector.addLog("提取了" + rawData.size() + "条数据");
		
		//监听器回调点
		ETLListenerUtil.stepAfterExtract(stepListener, inputResource, outputResource, dbOptional);
		
		List<Map<String, Object>> newData = new ArrayList<>();
		for (Map<String, Object> rawSingleData : rawData) {
			
			//监听器回调点
			ETLListenerUtil.stepBeforeConvert(stepListener, inputResource, outputResource, dbOptional);
			
			Map<String, Object> newSingleData = convert(rawSingleData, inputResource, outputResource, dbOptional, runtimeVersionId);
			
			//监听器回调点
			ETLListenerUtil.stepAfterConvert(stepListener, inputResource, outputResource, dbOptional);
			
			if(newSingleData != null){
				newData.add(newSingleData);
			}
		}
		//监听器回调点
		ETLListenerUtil.stepBeforeLoad(stepListener, inputResource, outputResource, dbOptional, newData);
		
		load(newData, outputResource, dbOptional, runtimeVersionId);
		
		stepLogCollector.addLog("加载影响了" + newData.size() + "条数据");
		
		//监听器回调点
		ETLListenerUtil.stepAfterLoad(stepListener, inputResource, outputResource, dbOptional);
	}
	
	public DBETLOptional optional() {
		return dbOptional;
	}

	public void setDbOptional(DBETLOptional dbOptional) {
		this.dbOptional = dbOptional;
	}
	
	protected void configOptional(DBETLOptional dbOptional) {
		//空实现，子类实现可以配置optional
	}

}
	