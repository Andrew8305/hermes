package org.apel.hermes.config.web;

import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.hermes.config.biz.domain.DBConfigure;
import org.apel.hermes.config.biz.service.DBConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dBConfigure")
public class DBConfigureController {
	
	private final static String INDEX_URL = "dbconfigure/dBConfigure_index";
	
	@Autowired
	private DBConfigureService dBConfigureService;
	
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
		dBConfigureService.pageQuery(pageBean);
		return pageBean;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(DBConfigure dBConfigure){
		String ret = dBConfigureService.save(dBConfigure);
		if(!ret.equals("")){
			return MessageUtil.message("dBConfigure.create.success");
		}else{
			return MessageUtil.message("dBConfigure.repeat.error");
		}
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, DBConfigure dBConfigure){
		dBConfigure.setId(id);
		String ret = dBConfigureService.modify(dBConfigure);
		if(!ret.equals("")){
			
			return MessageUtil.message("dBConfigure.update.success");
		}else{
			return MessageUtil.message("dBConfigure.repeat.error");
		}
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody DBConfigure view(@PathVariable String id){
		return dBConfigureService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		dBConfigureService.deleteById(id);
		return MessageUtil.message("dBConfigure.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		dBConfigureService.deleteById(ids);
		return MessageUtil.message("dBConfigure.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<DBConfigure> getAll(){
		return dBConfigureService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	
	
}
