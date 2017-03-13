package org.apel.hermes.config.biz.service;

import java.util.Optional;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.Job;

public interface JobService extends BizCommonService<Job,String>{

	Optional<Job> findByJobKey(String jobBizId);

}
