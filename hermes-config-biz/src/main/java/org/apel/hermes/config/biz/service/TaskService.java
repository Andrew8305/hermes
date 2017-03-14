package org.apel.hermes.config.biz.service;

import java.util.List;

import org.apel.gaia.commons.exception.PlatformException;
import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.Task;
import org.springframework.data.jpa.repository.Query;

public interface TaskService extends BizCommonService<Task,String>{

	 String modify(Task task);
	 
	
	List<Task> findAllByJobJobKey(String jobBizId);
	

	List<Task> findAllByJobJobKeyAndTaskKeyAndDbOutputIsNotNull(String jobBizId, String taskBizId);

	void deleteByJobId(String id);

	List<Task> findByJobId(String id);

}
