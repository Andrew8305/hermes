package org.apel.hermes.core.remoting;

import java.util.List;
import java.util.Map;

import org.apel.hermes.config.api.domain.JobConfig;
import org.apel.hermes.config.api.domain.JobLogCollector;

public interface RemotingService {
	/**
	 * 保存日志
	 * @param jobLogCollector
	 */
	public void saveLog(JobLogCollector jobLogCollector);
	
	
	List<String> getTaskIds(String jobId);

	List<Map<String, Object>> getInputResources(String jobBizId,String taskBizId);

	List<Map<String, Object>> getOutputResources(String jobBizId,String taskBizId);
	
	JobConfig getJobConfig(String jobId);
}
