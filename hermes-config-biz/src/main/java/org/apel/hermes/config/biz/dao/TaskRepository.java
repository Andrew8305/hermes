package org.apel.hermes.config.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends CommonRepository<Task, String>{

	public List<Task> findByJobId(String jobId);

	public List<Task> findByJobIdAndTaskBizId(String jobId, String taskBizId);
	
	@Modifying
	@Query
	public void deleteByJobId(String id);



}
