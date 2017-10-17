package org.apel.hermes.core.step.db;

import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.core.context.ETLContext;
import org.apel.hermes.core.listener.StepListener;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.resource.db.DBETLResource;
import org.apel.hermes.core.step.ETLStep;
import org.apel.hermes.core.util.ETLListenerUtil;

/**
 * 数据库同步step
 * @author lijian
 *
 */
public abstract class DBETLStep implements ETLStep{
	
	protected ETLContext context;
	protected String taskId;
	protected StepListener stepListener;
	protected int order;
	
	public DBETLStep(){
		if(this instanceof StepListener){
			this.stepListener = (StepListener)this;
		}
	}
	
	@Override
	public void doStep(ETLResource inputETLResource,
			ETLResource outputETLResource, StepLogCollector stepLogCollector, String runtimeVersionId) {
		//监听器回调点
		ETLListenerUtil.stepOnStart(stepListener, inputETLResource, outputETLResource);
		
		doStep((DBETLResource)inputETLResource, (DBETLResource)outputETLResource, stepLogCollector, runtimeVersionId);
		
		//监听器回调点
		ETLListenerUtil.stepOnEnd(stepListener, inputETLResource, outputETLResource);
	}
	
	@Override
	public int order() {
		return this.order;
	}
	
	@Override
	public void order(int order) {
		this.order = order;
	}

	public abstract void doStep(DBETLResource inputResource, DBETLResource outputResource, StepLogCollector stepLogCollector, String runtimeVersionId);
	
	public StepListener getStepListener(){
		return this.stepListener;
	}
	
	public void setStepListener(StepListener stepListener){
		this.stepListener = stepListener;
	}

	@Override
	public void setContext(ETLContext context) {
		this.context = context;
	}

	@Override
	public ETLContext getContext() {
		return this.context;
	}

	@Override
	public void taskId(String taskId) {
		this.taskId = taskId;
	}
	
	
}
