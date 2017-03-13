package org.apel.hermes.config.biz.service;

import java.util.List;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.domain.JobDBConfigure;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JobDBConfigureService extends BizCommonService<JobDBConfigure,String>{

	List<JobDBConfigure> findAllByJobId(String jobId);

	JobDBConfigure findByJobIdAndInDBconfigureId(String jobId, String inId);

	JobDBConfigure findByJobIdAndOutDBconfigureId(String jobId, String outId);
	
	
	void deleteByJobId(String id);
	
	
	

}
