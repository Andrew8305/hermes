package org.apel.hermes.config.biz.service.impl;

import java.util.List;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hermes.config.biz.dao.TaskRepository;
import org.apel.hermes.config.biz.domain.DBConfigure;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.domain.TaskDBConfigure;
import org.apel.hermes.config.biz.service.TaskDBConfigureService;
import org.apel.hermes.config.biz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl extends AbstractBizCommonService<Task, String> implements TaskService{
	
	@Autowired
	private TaskRepository taskRepository;
	
	
	
	@Autowired
	private TaskDBConfigureService taskDBConfigureService;
	
	
	
	
	
	@Override
	public void deleteById(String ...ids){
		
		for(String id:ids){
			taskDBConfigureService.deleteByTaskId(id);
			super.deleteById(id);
		}
		
	}
	
	
	@Override
	public String save(Task task){
		String key = task.getTaskKey();
		Task taskEntity  = taskRepository.findByJobIdAndTaskKey(task.getJob().getId(),key);
		if(taskEntity!=null){
			return "";
		}
		task.setId("");
		super.save(task);
		//保存输入源、输出源
		List<DBConfigure> outList = task.getOutList();
		for(DBConfigure dbconfigure:outList){
			TaskDBConfigure taskDBConfigure = new TaskDBConfigure();
			taskDBConfigure.setTask(task);
			taskDBConfigure.setJob(task.getJob());
			taskDBConfigure.setOutDBconfigure(dbconfigure);
			taskDBConfigureService.save(taskDBConfigure);
		}
		List<DBConfigure> inList = task.getInList();
		for(DBConfigure dbconfigure:inList){
			TaskDBConfigure taskDBConfigure = new TaskDBConfigure();
			taskDBConfigure.setTask(task);
			taskDBConfigure.setJob(task.getJob());
			taskDBConfigure.setInDBconfigure(dbconfigure);
			taskDBConfigureService.save(taskDBConfigure);
		}
		return task.getId();
		
	}
	
	@Override
	public String modify(Task task){
		String key = task.getTaskKey();
		Task taskEntity  = taskRepository.findByJobIdAndTaskKey(task.getJob().getId(),key);
		if(taskEntity!=null && !task.getId().equals(taskEntity.getId())){
			return "";
		}
		super.update(task);
		
		
		List<DBConfigure> outList = task.getOutList();
		
		for(DBConfigure dbconfigure:outList){
			String outDbId = dbconfigure.getId();
			TaskDBConfigure taskDbConfigure = taskDBConfigureService.findByTaskIdAndOutDBconfigureId(task.getId(),outDbId);
			if(taskDbConfigure==null){
				TaskDBConfigure taskDBConfigure = new TaskDBConfigure();
				taskDBConfigure.setTask(task);
				taskDBConfigure.setJob(task.getJob());
				taskDBConfigure.setOutDBconfigure(dbconfigure);
				taskDBConfigureService.save(taskDBConfigure);
			}
			
		}
		List<DBConfigure> inList = task.getInList();
		for(DBConfigure dbconfigure:inList){
			String inDbId = dbconfigure.getId();
			TaskDBConfigure taskDbConfigure = taskDBConfigureService.findByTaskIdAndInDBconfigureId(task.getId(),inDbId);
			if(taskDbConfigure==null){
				TaskDBConfigure taskDBConfigure = new TaskDBConfigure();
				taskDBConfigure.setTask(task);
				taskDBConfigure.setJob(task.getJob());
				taskDBConfigure.setInDBconfigure(dbconfigure);
				taskDBConfigureService.save(taskDBConfigure);
			}
		}
		
		return task.getId();
	}

	@Override
	public List<Task> findAllByJobJobKey(String jobBizId) {
		return taskRepository.findAllByJobJobKey(jobBizId);
	}



	@Override
	public List<Task> findAllByJobJobKeyAndTaskKeyAndDbOutputIsNotNull(String jobBizId, String taskBizId) {
		//return taskRepository.findAllByJobJobKeyAndTaskKeyAndDbOutputIsNotNull(jobBizId, taskBizId);
		return null;
	}

	@Override
	public void deleteByJobId(String id) {
		taskRepository.deleteByJobId(id);
		
	}

	@Override
	public List<Task> findByJobId(String jobId) {
		return taskRepository.findByJobId(jobId);
	}

	

}
