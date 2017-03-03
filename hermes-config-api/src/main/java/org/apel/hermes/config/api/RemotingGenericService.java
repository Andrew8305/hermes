package org.apel.hermes.config.api;

import java.util.List;
import java.util.Map;

import org.apel.hermes.config.api.domain.JobConfig;

/**
 * 获取配置中心作业配置的接口
 * @author lijian
 *
 */
public interface RemotingGenericService {

	List<String> getTaskIds(String jobBizId);

	List<Map<String, Object>> getInputResources(String jobBizId,String taskBizId);

	List<Map<String, Object>> getOutputResources(String jobBizId,String taskBizId);
	
	JobConfig getJobConfig(String jobBizId);

}
