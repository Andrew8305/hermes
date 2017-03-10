package org.apel.hermes.config.web;

import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.hermes.config.biz.domain.JobDBConfigure;
import org.apel.hermes.config.biz.service.JobDBConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jobDBConfigure")
public class JobDBConfigureController {
	
	@Autowired
	private JobDBConfigureService jobDBConfigureService;
	
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		jobDBConfigureService.deleteById(id);
		return MessageUtil.message("jobDBConfigure.delete.success");
	}
	
	@RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
	public @ResponseBody List<JobDBConfigure> findAllByJobId(@PathVariable("jobId") String jobId){
		return jobDBConfigureService.findAllByJobId(jobId);
	};

}
