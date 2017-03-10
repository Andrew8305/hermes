package org.apel.hermes.config.web;

import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/job")
public class JobController {
	
	private final static String INDEX_URL = "job/job_index";
	
	@Autowired
	private JobService jobService;
	
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
		jobService.pageQuery(pageBean);
		return pageBean;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(@RequestBody Job job){
		jobService.save(job);
		System.out.println("-----------");
		return MessageUtil.message("job.create.success");
	}
	
	//更新
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Message update(@RequestBody Job job){
		
		jobService.update(job);
		return MessageUtil.message("job.update.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Job view(@PathVariable String id){
		return jobService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		jobService.deleteById(id);
		return MessageUtil.message("job.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		jobService.deleteById(ids);
		return MessageUtil.message("job.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Job> getAll(){
		return jobService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	
	
}
