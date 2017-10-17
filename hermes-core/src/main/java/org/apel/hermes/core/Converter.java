package org.apel.hermes.core;

import java.util.Map;

import org.apel.hermes.core.optional.ETLOptional;
import org.apel.hermes.core.resource.ETLResource;

/**
 * 同步逻辑划分为三大步骤，为提取，清洗或转换，加载
 * 此为清洗或转换步骤接口
 * @author lijian
 *
 * @param <K>
 * @param <V>
 */
public interface Converter {

	
	Map<String, Object> convert(Map<String, Object> rawRow, ETLResource inputETLResource, ETLResource outputETLResource, ETLOptional optional, String runtimeVersionId);
	
}
