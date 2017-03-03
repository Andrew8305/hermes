package org.apel.hermes.core.factory;

import java.util.Map;

import org.apel.hermes.core.resource.ETLResource;

/**
 * 源创建工厂泛型接口
 * @author lijian
 *
 * @param <T>
 */
public interface ETLResourceFactory<T extends ETLResource> {

	T createResource(Map<String, Object> params);
	
	boolean checkParams(Map<String, Object> params);
	
	String type();
}
