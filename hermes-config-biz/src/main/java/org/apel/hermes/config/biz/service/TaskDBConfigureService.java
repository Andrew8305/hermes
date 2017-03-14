package org.apel.hermes.config.biz.service;

import java.util.List;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.domain.TaskDBConfigure;

public interface TaskDBConfigureService extends BizCommonService<TaskDBConfigure,String>{

	public List<TaskDBConfigure> findByTaskId(String taskId);

	public TaskDBConfigure findByTaskIdAndInDBconfigureId(String id, String inDbId);

	public TaskDBConfigure findByTaskIdAndOutDBconfigureId(String id, String outDbId);

	public void deleteByTaskId(String id);

	public List<TaskDBConfigure> findByJobJobKeyAndTaskTaskKeyAndInDBconfigureIsNotNull(String jobBizId,
			String taskBizId);

	public List<TaskDBConfigure> findByJobJobKeyAndTaskTaskKeyAndOutDBconfigureIsNotNull(String jobBizId,
			String taskBizId);
	

}
