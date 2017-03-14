package org.apel.hermes.config.web;

import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.hermes.config.biz.domain.TaskDBConfigure;
import org.apel.hermes.config.biz.service.TaskDBConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/taskDBConfigure")
public class TaskDBConfigureController {
	
	@Autowired
	private TaskDBConfigureService taskDBConfigureService;
	
	
	//删除
	@RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
	public @ResponseBody List<TaskDBConfigure> findByTaskId(@PathVariable(name="taskId") String taskId){
		return taskDBConfigureService.findByTaskId(taskId);
	}
	
	
	//删除
		@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public @ResponseBody Message delete(@PathVariable(name="id") String id){
			taskDBConfigureService.deleteById(id);
			return MessageUtil.message("taskDbconfigure.delete.success");
		}

}
