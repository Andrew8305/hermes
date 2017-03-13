package org.apel.hermes.config.biz.dao;

import java.util.Optional;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.Job;
import org.springframework.data.jpa.repository.Query;

public interface JobRepository extends CommonRepository<Job, String>{
	
	@Query
	Optional<Job> findByJobKey(String jobBizId);

}
