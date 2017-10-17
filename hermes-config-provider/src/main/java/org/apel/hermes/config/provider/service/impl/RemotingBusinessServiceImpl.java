package org.apel.hermes.config.provider.service.impl;

import org.apel.dubbo.starter.config.SpringService;
import org.apel.gaia.util.BeanUtils;
import org.apel.hermes.config.api.RemotingBusinessService;
import org.apel.hermes.config.api.domain.JobLogCollector;
import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.config.api.domain.TaskLogCollector;
import org.apel.hermes.config.biz.domain.JobLog;
import org.apel.hermes.config.biz.domain.StepLog;
import org.apel.hermes.config.biz.domain.TaskLog;
import org.apel.hermes.config.biz.service.JobLogService;
import org.apel.hermes.config.provider.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(timeout=8000)
@SpringService
public class RemotingBusinessServiceImpl implements RemotingBusinessService {
	
	@Autowired
	private JobLogService jobLogService;
	

	@Override
	public void saveLog(JobLogCollector jobLogCollector) {
		if(jobLogCollector!=null){
			//作业日志
			JobLog jobLog = new JobLog();
			BeanUtils.copyNotNullProperties(jobLogCollector, jobLog);
			jobLog.setJobBizId(jobLogCollector.getJobId());
			//任务日志
			for(TaskLogCollector taskLogCollector:jobLogCollector.getTaskLogCollectors()){
				TaskLog taskLog = new TaskLog();
				BeanUtils.copyNotNullProperties(taskLogCollector, taskLog);
				taskLog.setId(null);
				taskLog.setTaskBizId(taskLogCollector.getId());
				//步骤日志
				for(StepLogCollector stepLogCollector:taskLogCollector.getStepLogCollectors()){
					StepLog stepLog = new StepLog();
					BeanUtils.copyNotNullProperties(stepLogCollector, stepLog);
					stepLog.setSeq(stepLogCollector.getOrder());
					stepLog.setLogDetails(ConvertUtil.listString2String(stepLogCollector.getLogs(), "<br>"));
					taskLog.getStepLogs().add(stepLog);
				}
				jobLog.getTaskLogs().add(taskLog);
			}
			jobLogService.saveLog(jobLog);
		}
	}

}
