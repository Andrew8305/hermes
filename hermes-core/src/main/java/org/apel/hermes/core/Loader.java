package org.apel.hermes.core;

import java.util.List;
import java.util.Map;

import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.core.optional.ETLOptional;
import org.apel.hermes.core.resource.ETLResource;

/**
 * 同步逻辑划分为三大步骤，为提取，清洗或转换，加载
 * 此为加载步骤接口
 * @author lijian
 *
 * @param <K>
 * @param <V>
 */
public interface Loader  {
	
	StepLogCollector load(List<Map<String, Object>> newData, ETLResource outputETLResource, ETLOptional optional);
	
}
