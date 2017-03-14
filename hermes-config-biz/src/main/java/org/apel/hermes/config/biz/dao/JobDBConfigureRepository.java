package org.apel.hermes.config.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.JobDBConfigure;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JobDBConfigureRepository extends CommonRepository<JobDBConfigure, String>{
	
	List<JobDBConfigure> findAllByJobId(String jobId);
	JobDBConfigure findByJobIdAndInDBconfigureId(String jobId, String inId);
	JobDBConfigure findByJobIdAndOutDBconfigureId(String jobId, String outId);
	
	
	@Modifying
	void deleteByJobId(String jobId);

}
