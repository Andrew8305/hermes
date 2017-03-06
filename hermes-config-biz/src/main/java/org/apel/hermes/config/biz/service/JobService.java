package org.apel.hermes.config.biz.service;

import java.util.Map;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.Job;

public interface JobService extends BizCommonService<Job,String>{

	Map<String, String> checkRef(String code);

	/**
	 * 检查作业业务ID是否重复
	 * @param jobBizId
	 * @return
	 * @throws Exception
	 */
	Map<String, String> checkIsRepeat(String jobBizId)throws Exception;

	Job findByJobBizId(String jobBizId);

	void batchDelete(String[] ids)throws Exception;

	
	

}
