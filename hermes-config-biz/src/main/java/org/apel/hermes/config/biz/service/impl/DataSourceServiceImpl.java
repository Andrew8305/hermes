package org.apel.hermes.config.biz.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.gaia.util.BeanUtils;
import org.apel.hermes.config.biz.dao.DataSourceRepository;
import org.apel.hermes.config.biz.dao.JobRepository;
import org.apel.hermes.config.biz.dao.TaskRepository;
import org.apel.hermes.config.biz.domain.DataSource;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class DataSourceServiceImpl extends AbstractBizCommonService<DataSource, Integer> implements DataSourceService{

	@Autowired
	private DataSourceRepository dataSourceRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private EntityManager em;
	
	@Override
	protected CommonRepository<DataSource, Integer> getRepository() {
		return dataSourceRepository;
	}

	
	
	
	@Override
	protected String getPageQl() {
	
		String hql= "select o from DataSource  o where 1=1 " ;
		return hql;
	}

	/**
	 * 合并属性 key=businessCode,value=具有不同属性的DataSource集合
	 * @param list
	 * @return
	 */
	private static Map<String,List<DataSource>> getSingleDataSources(List<DataSource> list){
		Map<String,List<DataSource>> dataSources = new HashMap<>();
		if(list==null){
			return dataSources;
		}
		for(DataSource ds:list){
			if(dataSources.get(ds.getBusinessCode())!=null){
				dataSources.get(ds.getBusinessCode()).add(ds);
			}else{
				List<DataSource> newList= new ArrayList<>();
				newList.add(ds);
				dataSources.put(ds.getBusinessCode(), newList);
			}
		}
		return dataSources;
	}
	
	
	
	@Override
	public Map<String,List<DataSource>> getInputDataSource(String jobBizId,String taskBizId) {
		Task task = findTaskByJobBizIdAndTaskBizId(jobBizId, taskBizId);
		
		String input = task.getInput();
		List<DataSource> list = null;
		if(StringUtils.isNotBlank(input)){
			String[] str = input.split(",");
			List<String> inputs = Arrays.asList(str);
			list = dataSourceRepository.findByBusinessCodes(inputs);
		}
		return getSingleDataSources(list);
	}




	private Task findTaskByJobBizIdAndTaskBizId(String jobBizId, String taskBizId) {
		List<Job> jobs = jobRepository.findByJobBizId(jobBizId);
		String jobId="";
		if(CollectionUtils.isEmpty(jobs)){
			throw new RuntimeException("没有找到jobBizId["+jobBizId+"]对应的Job");
		}
		jobId=jobs.get(0).getId();
		List<Task> tasks = taskRepository.findByJobIdAndTaskBizId(jobId, taskBizId);
		if(CollectionUtils.isEmpty(tasks)){
			throw new RuntimeException("没有找到jobBizId["+jobBizId+"]和taskBizId["+taskBizId+"]对应的Task");
		}
		Task task = tasks.get(0);
		return task;
	}

	@Override
	public Map<String,List<DataSource>> getOutputDataSource(String jobBizId,String taskBizId) {
		Task task = findTaskByJobBizIdAndTaskBizId(jobBizId, taskBizId);
		String output = task.getOutput();
		List<DataSource> list = null;
		if(StringUtils.isNotBlank(output)){
			String[] str = output.split(",");
			List<String> outputs = Arrays.asList(str);
			list = dataSourceRepository.findByBusinessCodes(outputs);
		}
		return getSingleDataSources(list);
	}

	
	@Override
	public void saveOrUpdate(List<DataSource> dataSource) throws Exception {
		Integer max = dataSourceRepository.findMaxSeq()==null?1:dataSourceRepository.findMaxSeq()+1;
		
		if(dataSource.size()==0 || dataSource==null){
			throw new RuntimeException("请先添加模板信息!!!");
		}
		
//		String businessCode = dataSource.get(0).getBusinessCode();
//		List<DataSource> persistenceEntityList = dataSourceRepository.findAllByBusinessCode(businessCode);
//		for(DataSource persistenceEntity:persistenceEntityList){
//			dataSourceRepository.delete(persistenceEntity);
//		}
		if(null!= dataSource.get(0).getId() && !"".equals(dataSource.get(0).getId())){
			for(DataSource ds :dataSource){
				Integer id = ds.getId();
				DataSource persistentEntity =  this.findById(id);
				BeanUtils.copyNotNullProperties(ds, persistentEntity);
				this.update(persistentEntity);
			}
			
		}else{
			for(DataSource ds:dataSource){
				ds.setSeq(max);
				this.save(ds);
			}
		}
		
		
	}

	@Override
	public Map<String, String> checkRepeat(String businessCode) throws Exception {
		Map<String,String> result = new HashMap<String,String>();
		List<DataSource> list = dataSourceRepository.findAllByBusinessCode(businessCode);
		if(list==null || list.size()==0){
			result.put("isRepeat", "false");
		}else{
			result.put("isRepeat", "true");
		}
		
		return result;
	}




	@Override
	public List<DataSource> findAllByBusinessCode(Integer id) throws Exception {
		DataSource dataSourceEntity = this.findById(id);
		String businessCode = dataSourceEntity.getBusinessCode();
		List<DataSource> list = dataSourceRepository.findAllByBusinessCode(businessCode);
		return list;
	}

	@Override
	public void batchDelete(Integer[] ids) throws Exception {
		for(Integer id:ids){
			DataSource dataSourceEntity  = this.findById(id);
			String code = dataSourceEntity.getBusinessCode();
			String sql = "SELECT o.*  FROM etl_job o WHERE FIND_IN_SET('"+code+"',o.output) OR FIND_IN_SET('"+code+"',o.output)";
			List list = em.createNativeQuery(sql).getResultList();
			if(list!=null && list.size()>0){
				throw new RuntimeException("Job列表有关联引用，无法删除！！！");
			}
			dataSourceRepository.deleteAllByBusinessCode(code);
		}
		
	}




	@SuppressWarnings("unchecked")
	@Override
	public List<DataSource> getAllDataSourceByGroup() throws Exception {
		List<DataSource>  dataSource = dataSourceRepository.getAllDataSourceByGroup();
		if(dataSource.size()==0){
			dataSource = Collections.EMPTY_LIST;
		}
		return dataSource;
	}

}
