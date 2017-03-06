package org.apel.hermes.config.provider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apel.gaia.util.BeanUtils;
import org.apel.hermes.common.consist.ETLResourceConsist;
import org.apel.hermes.config.api.RemotingGenericService;
import org.apel.hermes.config.api.domain.JobConfig;
import org.apel.hermes.config.biz.domain.DataSource;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.service.DataSourceService;
import org.apel.hermes.config.biz.service.JobService;
import org.apel.hermes.config.biz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(timeout=8000)
@org.springframework.stereotype.Service
public class RemotingGenericServiceImpl implements RemotingGenericService{

	@Autowired
	private JobService jobService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private DataSourceService dataSourceService;
	
	@Override
	public List<String> getTaskIds(String jobBizId) {
		return taskService.findTaskIdsByJobId(jobBizId);
	}

	@Override
	public List<Map<String, Object>> getInputResources(String jobBizId,String taskBizId) {
		
		return getListMap(dataSourceService.getInputDataSource(jobBizId,taskBizId));
	}

	@Override
	public List<Map<String, Object>> getOutputResources(String jobBizId,String taskBizId) {
		return getListMap(dataSourceService.getOutputDataSource(jobBizId,taskBizId));
	}

	@Override
	public JobConfig getJobConfig(String jobBizId) {
		Job job=jobService.findByJobBizId(jobBizId);
		if(job!=null){
			JobConfig jobConfig=new JobConfig();
			BeanUtils.copyNotNullProperties(job, jobConfig);
			jobConfig.setId(job.getJobBizId());
			jobConfig.setDesc(job.getDescription());
			return jobConfig;
		}
		return null;
	}
	
	private List<Map<String, Object>> getListMap(Map<String,List<DataSource>> sources){
		List<Map<String, Object>> result = new ArrayList<>();
		for(String key:sources.keySet()){
			Map<String, Object> map = new HashMap<>();
			List<DataSource> dss=sources.get(key);
			for(DataSource ds:dss){
				map.put(ETLResourceConsist.ID, ds.getBusinessCode());//数据源ID
				map.put(ETLResourceConsist.NAME, ds.getBusinessName());//数据源名称
				map.put(ETLResourceConsist.TYPE, ds.getTemplateCode());//db
				if(ds.getDatasourceType()!=null){
					map.put(ds.getDatasourceType().getParam(),ds.getParamValue());
				}
			}
			result.add(map);
		}
		return result;
	}

}
