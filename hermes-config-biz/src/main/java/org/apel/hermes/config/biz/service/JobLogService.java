package org.apel.hermes.config.biz.service;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.JobLog;

public interface JobLogService extends BizCommonService<JobLog,String>{

	/**
	 * 级联保存日志
	 */
	public void saveLog(JobLog jobLog);
}
