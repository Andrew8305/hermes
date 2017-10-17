package org.apel.hermes.config.provider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apel.dubbo.starter.config.SpringService;
import org.apel.hermes.common.consist.ETLResourceConsist;
import org.apel.hermes.config.api.RemotingGenericService;
import org.apel.hermes.config.api.domain.JobConfig;
import org.apel.hermes.config.biz.domain.DBConfigure;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.domain.TaskDBConfigure;
import org.apel.hermes.config.biz.service.JobService;
import org.apel.hermes.config.biz.service.TaskDBConfigureService;
import org.apel.hermes.config.biz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(timeout=8000)
@SpringService
public class RemotingGenericServiceImpl implements RemotingGenericService{

	@Autowired
	private JobService jobService;
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskDBConfigureService taskDBConfigureService;
	
	
	
	@Override
	public List<String> getTaskIds(String jobBizId) {
		List<Task> taskList = taskService.findAllByJobJobKey(jobBizId);
		List<String> taskIds = taskList.stream().map(e->e.getId()).collect(Collectors.toList());
		return taskIds;
	}

	@Override
	public List<Map<String, Object>> getInputResources(String jobBizId,String taskBizId) {
		List<TaskDBConfigure> taskDBConfigureList = taskDBConfigureService.findByJobJobKeyAndTaskTaskKeyAndInDBconfigureIsNotNull(jobBizId,taskBizId);
		List<DBConfigure> dbConfigure = taskDBConfigureList.stream().map(e->e.getInDBconfigure()).collect(Collectors.toList());
		return getDBConfigure(dbConfigure);
		
	}
	
	
	private List<Map<String,Object>> getDBConfigure(List<DBConfigure> dbconfigures){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(DBConfigure dbconfigure:dbconfigures){
			Map<String,Object> datasource = new HashMap<String,Object>();
			datasource.put(ETLResourceConsist.ID, dbconfigure.getDbKey());//数据源ID
			datasource.put(ETLResourceConsist.NAME, dbconfigure.getDbName());//数据源名称
			datasource.put(ETLResourceConsist.TYPE, dbconfigure.TYPE_CODE);//db
			
			result.add(datasource);
		}
		return result;
	}
	

	@Override
	public List<Map<String, Object>> getOutputResources(String jobBizId,String taskBizId) {
		List<TaskDBConfigure> taskDBConfigureList = taskDBConfigureService.findByJobJobKeyAndTaskTaskKeyAndOutDBconfigureIsNotNull(jobBizId,taskBizId);
		List<DBConfigure> dbConfigure = taskDBConfigureList.stream().map(e->e.getInDBconfigure()).collect(Collectors.toList());
		return getDBConfigure(dbConfigure);
	}

	@Override
	public JobConfig getJobConfig(String jobBizId) {
		Optional<Job> jobEntity = jobService.findByJobKey(jobBizId);
		if(jobEntity.isPresent()){
			JobConfig jobConfig=new JobConfig();
			Job  job = jobEntity.get();
			jobConfig.setId(job.getJobKey());
			jobConfig.setDesc(job.getDescription());
			jobConfig.setName(job.getJobName());
			jobConfig.setSchedule(job.getSchedule());
			return jobConfig;
		}
		return null;
	}
	
	
}
