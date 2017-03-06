package org.apel.hermes.config.biz.service.impl;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.dao.StepLogRepository;
import org.apel.hermes.config.biz.domain.StepLog;
import org.apel.hermes.config.biz.service.StepLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StepLogServiceImpl extends AbstractBizCommonService<StepLog, String> implements StepLogService{

	@Autowired
	private StepLogRepository stepLogRepository;
	
	@Override
	protected CommonRepository<StepLog, String> getRepository() {
		return stepLogRepository;
	}

	@Override
	protected String getPageQl() {
		return "select t from StepLog t where 1=1";
	}

}
