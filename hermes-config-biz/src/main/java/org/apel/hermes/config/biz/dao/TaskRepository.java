package org.apel.hermes.config.biz.dao;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends CommonRepository<Task, String>{

	@Query
	@Modifying
	void deleteByJobIdAndDbOutputId(String jobId, String id);
	@Query
	@Modifying
	void deleteByJobIdAndDbInputId(String jobId, String id);
	
	@Query
	Task findByJobIdAndTaskKey(String id, String key);

}
