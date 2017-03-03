package org.apel.hermes.core.factory;

import org.apel.hermes.core.step.ETLStep;

/**
 * 智能step创建工厂，用于创建工具step直接供task使用
 * @author lijian
 *
 * @param <T>
 */
public interface SmartStepFactory<T extends ETLStep> {
	
	T createSmartStep(int order);
	
}
