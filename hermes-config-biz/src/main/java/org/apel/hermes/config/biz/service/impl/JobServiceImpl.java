package org.apel.hermes.config.biz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.dao.JobRepository;
import org.apel.hermes.config.biz.dao.TaskRepository;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class JobServiceImpl extends AbstractBizCommonService<Job, String> implements JobService{

	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private TaskRepository taskRepository;

	
	@Override
	protected CommonRepository<Job, String> getRepository() {
		return jobRepository;
	}

	@Override
	protected String getPageQl() {
		return "select t from Job t where 1=1";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, String> checkRef(String code) {
		String sql = "SELECT o.*  FROM etl_task o WHERE FIND_IN_SET('"+code+"',o.output) OR FIND_IN_SET('"+code+"',o.output)";
		Query query = entityManager.createNativeQuery(sql);
		
		List list = query.getResultList();
		Map<String,String> map =  new HashMap<String,String>();
		map.put("isRef", "false");
		if(list!=null && list.size()>0){
			map.put("isRef", "true");
		}
		return map;
	}

	@Override
	public Map<String, String> checkIsRepeat(String jobBizId) throws Exception {
		List<Job> jobs = jobRepository.findByJobBizId(jobBizId);
		Map<String,String> map = new HashMap<String,String>();
		map.put("isRepeat", "false");
		if(jobs!=null && jobs.size()>0){
			map.put("isRepeat","true");
		}
		return map;
	}

	@Override
	public Job findByJobBizId(String jobBizId) {
		List<Job> jobs = jobRepository.findByJobBizId(jobBizId);
		return (CollectionUtils.isEmpty(jobs))?null:jobs.get(0);
	}

	@Override
	public void batchDelete(String[] ids) throws Exception {
		for(String id:ids){
			taskRepository.deleteByJobId(id);
			this.deleteById(id);
			
		}
		
		
	}

	
}
