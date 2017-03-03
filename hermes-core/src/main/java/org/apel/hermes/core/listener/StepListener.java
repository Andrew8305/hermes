package org.apel.hermes.core.listener;

import java.util.List;
import java.util.Map;

import org.apel.hermes.core.optional.ETLOptional;
import org.apel.hermes.core.resource.ETLResource;

/**
 * 同步step监听器
 * @author lijian
 *
 */
public interface StepListener {

	void beforeExtract(ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional);
	
	void afterExtract(ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional);
	
	void beforeConvert(ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional);
	
	void afterConvert(ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional);
	
	void beforeLoad(ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional, List<Map<String, Object>> data);
	
	void afterLoad(ETLResource inputEtlResource, ETLResource outputEtlResource, ETLOptional optional);
	
	void onStart(ETLResource inputEtlResource, ETLResource outputEtlResource);
	
	void onEnd(ETLResource inputEtlResource, ETLResource outputEtlResource);
	
}
