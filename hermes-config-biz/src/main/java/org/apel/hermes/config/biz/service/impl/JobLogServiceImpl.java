package org.apel.hermes.config.biz.service.impl;

import java.util.Date;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.dao.JobLogRepository;
import org.apel.hermes.config.biz.dao.StepLogRepository;
import org.apel.hermes.config.biz.dao.TaskLogRepository;
import org.apel.hermes.config.biz.domain.JobLog;
import org.apel.hermes.config.biz.domain.StepLog;
import org.apel.hermes.config.biz.domain.TaskLog;
import org.apel.hermes.config.biz.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class JobLogServiceImpl extends AbstractBizCommonService<JobLog, String> implements JobLogService{

	@Autowired
	private JobLogRepository jobLogRepository;
	@Autowired
	private TaskLogRepository taskLogRepository;
	@Autowired
	private StepLogRepository stepLogRepository;
	
	@Override
	protected CommonRepository<JobLog, String> getRepository() {
		return jobLogRepository;
	}

	@Override
	protected String getPageQl() {
		return "select t from JobLog t where 1=1";
	}

	@Override
	public void saveLog(JobLog jobLog) {
		Assert.notNull(jobLog);
		jobLog.setCreateDate(new Date());
		jobLogRepository.store(jobLog);
		for(TaskLog taskLog:jobLog.getTaskLogs()){
			taskLog.setJobLogId(jobLog.getId());
			taskLog.setCreateDate(new Date());
			taskLogRepository.store(taskLog);
			for(StepLog stepLog:taskLog.getStepLogs()){
				stepLog.setTaskLogID(taskLog.getId());
				stepLog.setCreateDate(new Date());
				stepLogRepository.store(stepLog);
			}
		}
	}

}
