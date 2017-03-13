package org.apel.hermes.config.biz.service.impl;

import java.util.List;

import org.apel.gaia.commons.exception.PlatformException;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hermes.config.biz.dao.TaskRepository;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl extends AbstractBizCommonService<Task, String> implements TaskService{
	
	@Autowired
	private TaskRepository taskRepository;
	
	
	
	@Override
	public void deleteByJobIdAndDbInputId(String jobId, String id) {
		taskRepository.deleteByJobIdAndDbInputId(jobId, id);
	}

	@Override
	public void deleteByJobIdAndDbOutputId(String jobId, String id) {
		taskRepository.deleteByJobIdAndDbOutputId(jobId,id);
	}
	
	@Override
	public String save(Task task){
		String key = task.getTaskKey();
		Task taskEntity  = taskRepository.findByJobIdAndTaskKey(task.getJob().getId(),key);
		if(taskEntity!=null){
			return "";
		}
		super.save(task);
		return task.getId();
		
	}
	
	@Override
	public String modify(Task task){
		String key = task.getTaskKey();
		Task taskEntity  = taskRepository.findByJobIdAndTaskKey(task.getJob().getId(),key);
		if(taskEntity!=null){
			return "";
		}
		super.update(task);
		return task.getId();
	}

	@Override
	public List<Task> findAllByJobJobKey(String jobBizId) {
		return taskRepository.findAllByJobJobKey(jobBizId);
	}

	@Override
	public List<Task> findAllByJobJobKeyAndTaskKeyAndDbInputIsNotNull(String jobKey, String taskKey) {
		
		return taskRepository.findAllByJobJobKeyAndTaskKeyAndDbInputIsNotNull(jobKey,taskKey);
	}

	@Override
	public List<Task> findAllByJobJobKeyAndTaskKeyAndDbOutputIsNotNull(String jobBizId, String taskBizId) {
		return taskRepository.findAllByJobJobKeyAndTaskKeyAndDbOutputIsNotNull(jobBizId, taskBizId);
	}

	

}
