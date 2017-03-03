package org.apel.hermes.core.listener;

import java.util.Map;

import org.apel.hermes.core.optional.ETLOptional;
import org.apel.hermes.core.resource.ETLResource;

/**
 * optional的关于数据库数据转换的回调接口参数
 * 通过设置convertHandler可以以参数化的形式对step中的转换操作进行自定义化
 * @author lijian
 *
 */
public interface ConvertHandler {
	
	public Map<String, Object> convert(Map<String, Object> rawRow, ETLResource inputETLResource, ETLResource outputETLResource, ETLOptional optional);
	
}
