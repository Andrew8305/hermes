package org.apel.hermes.config.web;

import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.hermes.config.biz.service.JobLogService;
import org.apel.hermes.config.biz.service.StepLogService;
import org.apel.hermes.config.biz.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jobLog")
public class JobLogController {
	
	private final static String INDEX_URL = "jobLog/jobLog_index";
	@Autowired
	private JobLogService jobLogService;
	@Autowired
	private TaskLogService taskLogService;
	@Autowired
	private StepLogService stepLogService;
	
	//首页
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(){
		return INDEX_URL;
	}
	
	//列表查询
	@RequestMapping
	public @ResponseBody PageBean list(QueryParams queryParams){
		JqGridUtil.getPageBean(queryParams);
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		jobLogService.pageQuery(pageBean);
		return pageBean;
	}
	
	//列表查询
	@RequestMapping("taskLog")
	public @ResponseBody PageBean taskList(QueryParams queryParams){
		JqGridUtil.getPageBean(queryParams);
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		taskLogService.pageQuery(pageBean);
		return pageBean;
	}
	
	//列表查询
	@RequestMapping("taskLog/stepLog")
	public @ResponseBody PageBean stepList(QueryParams queryParams){
		JqGridUtil.getPageBean(queryParams);
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		stepLogService.pageQuery(pageBean);
		return pageBean;
	}
	
	
}
