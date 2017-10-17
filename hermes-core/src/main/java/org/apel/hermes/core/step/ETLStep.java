package org.apel.hermes.core.step;

import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.core.context.ETLContextSetting;
import org.apel.hermes.core.resource.ETLResource;

/**
 * 所有同步step的顶层接口
 * @author lijian
 *
 */
public interface ETLStep extends ETLContextSetting{
	
	/**
	 * 处理一个步骤逻辑，所有的同步逻辑都会在此方法当中去实现 
	 * @param inputETLResource 输入源
	 * @param outputETLResource 输出源
	 */
	void doStep(ETLResource inputETLResource, ETLResource outputETLResource, StepLogCollector stepLogCollector, String runtimeVersionId);
	
	/**
	 * step在task当中顺序
	 */
	int order();
	
	void order(int order);
	
	/**
	 * 所隶属于哪一个task
	 */
	String taskId();
	
	/**
	 * 所隶属于哪一个task
	 */
	void taskId(String taskId);
	
	/**
	 * 检查是否是一个有效的step
	 */
	void check();
	
}
