package org.apel.hermes.config.biz.service.impl;

import java.util.List;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hermes.config.biz.dao.JobDBConfigureRepository;
import org.apel.hermes.config.biz.domain.DBConfigure;
import org.apel.hermes.config.biz.domain.JobDBConfigure;
import org.apel.hermes.config.biz.service.JobDBConfigureService;
import org.apel.hermes.config.biz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobDBConfigureServiceImpl extends AbstractBizCommonService<JobDBConfigure, String> 
	implements JobDBConfigureService{
	
	@Autowired
	private JobDBConfigureRepository jobDBConfigureRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Override
	public List<JobDBConfigure> findAllByJobId(String jobId) {
		
		List<JobDBConfigure> result = jobDBConfigureRepository.findAllByJobId(jobId);
		return result;
	}
	
	
	@Override
	public void deleteById(String... id){
		JobDBConfigure jobDBConfigure = this.findById(id[0]);
		if(jobDBConfigure!=null){
			DBConfigure inDB = jobDBConfigure.getInDBconfigure();
			DBConfigure outDB = jobDBConfigure.getOutDBconfigure();
			super.deleteById(id);
		}
		
	}


	@Override
	public JobDBConfigure findByJobIdAndInDBconfigureId(String jobId, String inId) {
		return jobDBConfigureRepository.findByJobIdAndInDBconfigureId(jobId,inId);
	}


	@Override
	public JobDBConfigure findByJobIdAndOutDBconfigureId(String jobId, String outId) {
		return jobDBConfigureRepository.findByJobIdAndOutDBconfigureId(jobId,outId);
	}


	@Override
	public void deleteByJobId(String jobId) {
		jobDBConfigureRepository.deleteByJobId(jobId);
		
		
	}

	

}
