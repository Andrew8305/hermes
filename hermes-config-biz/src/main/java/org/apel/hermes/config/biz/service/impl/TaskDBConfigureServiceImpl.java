package org.apel.hermes.config.biz.service.impl;

import java.util.List;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hermes.config.biz.dao.TaskDBConfigureRepository;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.domain.TaskDBConfigure;
import org.apel.hermes.config.biz.service.TaskDBConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskDBConfigureServiceImpl extends AbstractBizCommonService<TaskDBConfigure, String> 
	implements TaskDBConfigureService{
	
	
	@Autowired
	private TaskDBConfigureRepository taskDBConfigureRepository;

	@Override
	public List<TaskDBConfigure> findByTaskId(String taskId) {
		return taskDBConfigureRepository.findByTaskId(taskId);
	}

	@Override
	public TaskDBConfigure findByTaskIdAndInDBconfigureId(String id, String inDbId) {
		return taskDBConfigureRepository.findByTaskIdAndInDBconfigureId(id,  inDbId);
	}

	@Override
	public TaskDBConfigure findByTaskIdAndOutDBconfigureId(String id, String outDbId) {
		return taskDBConfigureRepository.findByTaskIdAndOutDBconfigureId(id,  outDbId);
	}

	@Override
	public void deleteByTaskId(String id) {
		
		
		taskDBConfigureRepository.deleteByTaskId(id);
		
	}

	@Override
	public List<TaskDBConfigure> findByJobJobKeyAndTaskTaskKeyAndInDBconfigureIsNotNull(String jobBizId,
			String taskBizId) {
		return taskDBConfigureRepository.findByJobJobKeyAndTaskTaskKeyAndInDBconfigureIsNotNull(jobBizId,taskBizId);
	}

	@Override
	public List<TaskDBConfigure> findByJobJobKeyAndTaskTaskKeyAndOutDBconfigureIsNotNull(String jobBizId,
			String taskBizId) {
		return taskDBConfigureRepository.findByJobJobKeyAndTaskTaskKeyAndOutDBconfigureIsNotNull(jobBizId,taskBizId);
	}
	
	

	

}
