package org.apel.hermes.config.biz.service.impl;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.dao.TaskLogRepository;
import org.apel.hermes.config.biz.domain.TaskLog;
import org.apel.hermes.config.biz.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskLogServiceImpl extends AbstractBizCommonService<TaskLog, String> implements TaskLogService{

	@Autowired
	private TaskLogRepository taskLogRepository;
	
	@Override
	protected CommonRepository<TaskLog, String> getRepository() {
		return taskLogRepository;
	}

	@Override
	protected String getPageQl() {
		return "select t from TaskLog t where 1=1";
	}

}
