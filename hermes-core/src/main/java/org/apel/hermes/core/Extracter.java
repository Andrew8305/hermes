package org.apel.hermes.core;

import java.util.List;
import java.util.Map;

import org.apel.hermes.core.resource.ETLResource;

/**
 * 同步逻辑划分为三大步骤，为提取，清洗或转换，加载
 * 此为提取步骤接口
 * @author lijian
 *
 * @param <K>
 * @param <V>
 */
public interface Extracter {

	List<Map<String, Object>> extract(ETLResource inputETLResource);
	
}
