package org.apel.hermes.config.biz.dao;

import java.util.List;

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
	
	Task findByJobIdAndTaskKey(String id, String key);
	
	
	List<Task> findAllByJobJobKey(String jobBizId);
	
	List<Task> findAllByJobJobKeyAndTaskKeyAndDbInputIsNotNull(String jobKey, String taskKey);
	
	List<Task> findAllByJobJobKeyAndTaskKeyAndDbOutputIsNotNull(String jobBizId, String taskBizId);
	
	
	@Query
	@Modifying
	void deleteByJobId(String id);

}
