package org.apel.hermes.config.biz.service;

import org.apel.gaia.commons.exception.PlatformException;
import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.Task;

public interface TaskService extends BizCommonService<Task,String>{

	void deleteByJobIdAndDbInputId(String string, String id)throws PlatformException;

	void deleteByJobIdAndDbOutputId(String string, String id);
	 String modify(Task task);

}
