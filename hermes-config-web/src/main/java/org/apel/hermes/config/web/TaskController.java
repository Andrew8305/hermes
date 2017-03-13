package org.apel.hermes.config.web;

import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.service.TaskService;
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
@RequestMapping("/task")
public class TaskController {
	
	private final static String INDEX_URL = "task_index";
	
	@Autowired
	private TaskService taskService;
	
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
		taskService.pageQuery(pageBean);
		return pageBean;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(@RequestBody Task task){
		String result = taskService.save(task);
		System.out.println("------------");
		if(!result.equals("")){
			return MessageUtil.message("task.create.success");
		}else{
			return MessageUtil.message("task.key.repeat");
		}
	}
	
	//更新
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Message update(@RequestBody Task task){
		task.setId(task.getId());
		String ret = taskService.modify(task);
		if(ret.equals("")){
			return MessageUtil.message("task.key.repeat");
		}
		return MessageUtil.message("task.update.success");
		
		
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Task view(@PathVariable String id){
		return taskService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		taskService.deleteById(id);
		return MessageUtil.message("task.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		taskService.deleteById(ids);
		return MessageUtil.message("task.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Task> getAll(){
		return taskService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	
	
}
