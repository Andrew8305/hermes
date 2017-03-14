package org.apel.hermes.config.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.domain.TaskDBConfigure;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskDBConfigureRepository extends CommonRepository<TaskDBConfigure, String>{
	
	
	public List<TaskDBConfigure> findByTaskId(String taskId);

	public TaskDBConfigure findByTaskIdAndInDBconfigureId(String id, String inDbId);

	public TaskDBConfigure findByTaskIdAndOutDBconfigureId(String id, String outDbId);
	
	@Query
	@Modifying
	public void deleteByTaskId(String id);

	public List<TaskDBConfigure> findByJobJobKeyAndTaskTaskKeyAndInDBconfigureIsNotNull(String jobBizId,
			String taskBizId);

	public List<TaskDBConfigure> findByJobJobKeyAndTaskTaskKeyAndOutDBconfigureIsNotNull(String jobBizId,
			String taskBizId);

}
