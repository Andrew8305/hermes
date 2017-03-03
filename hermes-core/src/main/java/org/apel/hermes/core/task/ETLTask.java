package org.apel.hermes.core.task;

import java.util.List;

import org.apel.hermes.config.api.domain.JobLogCollector;
import org.apel.hermes.core.context.ETLContextSetting;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.step.ETLStep;


/**
 * ETLtask顶层接口，会管理和组织所有的同步step
 * @author lijian
 *
 */
public interface ETLTask extends ETLContextSetting{
	
	/**
	 * 回调接口，用于用户自己添加额外的工具step
	 */
	void additionInnerStep(List<ETLStep> additionSteps);
	
	/**
	 * 添加在spring环境当中自定义的step
	 */
	void addScanCustomSteps(List<ETLStep> scanCustomSteps);
	
	void addScanCustomStep(ETLStep scanCustomStep);
	
	/**
	 * 对当前包含的所有step进行排序
	 */
	void sortSteps();
	
	/**
	 * 获取所有的step
	 */
	List<ETLStep> getAllSteps();
	 
	/**
	 * 任务id
	 */
	String id();
	
	/**
	 * 开启一个任务
	 */
	void start(JobLogCollector jobLogCollector);
	
	void setInputETLResources(List<ETLResource> inputResources);
	
	void setoutputETLResources(List<ETLResource> outputResources);
	
	List<ETLResource> getInputETLResource();
	
	List<ETLResource> getOutputETLResource();
	
	String jobId();
	
}
