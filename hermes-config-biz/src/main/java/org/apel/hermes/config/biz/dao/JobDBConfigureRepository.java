package org.apel.hermes.config.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.JobDBConfigure;
import org.springframework.data.jpa.repository.Query;

public interface JobDBConfigureRepository extends CommonRepository<JobDBConfigure, String>{
	
	@Query
	List<JobDBConfigure> findAllByJobId(String jobId);
	@Query
	JobDBConfigure findByJobIdAndInDBconfigureId(String jobId, String inId);
	@Query
	JobDBConfigure findByJobIdAndOutDBconfigureId(String jobId, String outId);

}
