package org.apel.hermes.config.biz.service;

import java.util.List;
import java.util.Map;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.Task;

public interface TaskService extends BizCommonService<Task,String>{

	/**
	 * 根据作业ID查找任务列表
	 * @param	jobId
	 * @return
	 */
	public List<Task> findByJobId(String jobId);
	/**
	 * 根据作业ID查找任务ID列表
	 * @param	jobBizId
	 * @return
	 */
	public List<String> findTaskIdsByJobId(String jobBizId);
	/**
	 * 根据 作业ID 和 任务业务ID 检查是否重复
	 * @param	jobId
	 * @param taskBizID
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> checkIsRepeat(String jobId,String taskBizId)throws Exception;
	

}
