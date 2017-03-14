package org.apel.hermes.config.biz.service.impl;

import java.util.List;
import java.util.Optional;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.util.BeanUtils;
import org.apel.hermes.config.biz.dao.JobRepository;
import org.apel.hermes.config.biz.domain.DBConfigure;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.domain.JobDBConfigure;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.service.DBConfigureService;
import org.apel.hermes.config.biz.service.JobDBConfigureService;
import org.apel.hermes.config.biz.service.JobService;
import org.apel.hermes.config.biz.service.TaskDBConfigureService;
import org.apel.hermes.config.biz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobServiceImpl extends AbstractBizCommonService<Job, String> implements JobService{

	
	@Autowired
	private DBConfigureService dBConfigureService;
	
	@Autowired
	private JobDBConfigureService jobDBConfigureService;
	
	@Autowired
	private TaskDBConfigureService taskDBConfigureService;
	
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private TaskService taskService;
	
	
	
	@Override
	public String save(Job job){
		String jobId = super.save(job);
		List<String> inIds = job.getInList();
		//保存输入源
		for(String id:inIds){
			DBConfigure dbconfigure = dBConfigureService.findById(id);
			JobDBConfigure jobDBConfigure = new JobDBConfigure();
			jobDBConfigure.setInDBconfigure(dbconfigure);
			jobDBConfigure.setJob(job);
			jobDBConfigureService.save(jobDBConfigure);
		}
		List<String> outIds = job.getOutList();
		for(String id:outIds){
			DBConfigure dbconfigure = dBConfigureService.findById(id);
			JobDBConfigure jobDBConfigure = new JobDBConfigure();
			jobDBConfigure.setOutDBconfigure(dbconfigure);
			jobDBConfigure.setJob(job);
			jobDBConfigureService.save(jobDBConfigure);
		}
		return jobId;
	}
	
	@Override
	public void update(Job job){
		String id = job.getId();
		Job jobEntity  = jobService.findById(id);
		BeanUtils.copyNotNullProperties(job, jobEntity);
		
		
		List<String> inList = job.getInList();
		
		List<String> outList = job.getOutList();
		
		for(String inId:inList){
			JobDBConfigure inJobDB = jobDBConfigureService.findByJobIdAndInDBconfigureId(id,inId);
			if(inJobDB==null){
				inJobDB = new JobDBConfigure();
				
				DBConfigure dbconfigure = dBConfigureService.findById(inId);
				inJobDB.setInDBconfigure(dbconfigure);
				inJobDB.setJob(jobEntity);
				jobDBConfigureService.save(inJobDB);
			}
		}
		
		
		for(String outId:outList){
			JobDBConfigure outJobDB = jobDBConfigureService.findByJobIdAndOutDBconfigureId(id,outId);
			if(outJobDB==null){
				outJobDB = new JobDBConfigure();
				
				DBConfigure dbconfigure = dBConfigureService.findById(outId);
				outJobDB.setOutDBconfigure(dbconfigure);
				outJobDB.setJob(jobEntity);
				jobDBConfigureService.save(outJobDB);
			}
		}
		
		
	}

	@Override
	public Optional<Job> findByJobKey(String jobBizId) {
		return jobRepository.findByJobKey(jobBizId);
	}
	
	
	@Override
	public void deleteById(String... ids){
		for(String id:ids){
			jobDBConfigureService.deleteByJobId(id);
			List<Task> taskList = taskService.findByJobId(id);
			for(Task task:taskList){
				taskDBConfigureService.deleteByTaskId(task.getId());
			}
			taskService.deleteByJobId(id);
			super.deleteById(id);
		}
	}
	

}
