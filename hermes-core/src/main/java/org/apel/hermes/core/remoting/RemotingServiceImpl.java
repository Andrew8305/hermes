package org.apel.hermes.core.remoting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apel.hermes.config.api.RemotingBusinessService;
import org.apel.hermes.config.api.RemotingGenericService;
import org.apel.hermes.config.api.domain.JobConfig;
import org.apel.hermes.config.api.domain.JobLogCollector;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

@Component
public class RemotingServiceImpl implements RemotingService{
	
	private static Logger LOG = Logger.getLogger(RemotingServiceImpl.class);
	
	@Reference
	private RemotingBusinessService remotingBusinessService;
	@Reference
	private RemotingGenericService remotingGenericService;

	@Override
	public List<String> getTaskIds(String jobId) {
		List<String> taskIds = new ArrayList<>();
		try {
			taskIds = remotingGenericService.getTaskIds(jobId);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("RPC调用失败(getTaskIds)：" + e.getMessage());
		}
		return taskIds;
	}

	@Override
	public List<Map<String, Object>> getInputResources(String jobBizId,String taskBizId) {
//		List<Map<String, Object>> result = new ArrayList<>();
//		Map<String, Object> params = new HashMap<>();
//		
//		params.put(ETLResourceConsist.ID, "1");
//		params.put(ETLResourceConsist.NAME, "锦江监狱");
//		params.put(ETLResourceConsist.TYPE, DBETLResourceFactory.DB_TYPE);
//		params.put(DBParam.JDBC_URL, "jdbc:mysql://192.168.0.94:3306/platform?useUnicode=true&characterEncoding=utf8");
//		params.put(DBParam.JDBC_USER_NAME, "root");
//		params.put(DBParam.JDBC_PASSWORD, "");
		
//		params.put(ETLResourceConsist.ID, "1");
//		params.put(ETLResourceConsist.NAME, "锦江监狱");
//		params.put(ETLResourceConsist.TYPE, DBETLResourceFactory.DB_TYPE);
//		params.put(DBParam.JDBC_URL, "jdbc:oracle:thin:@192.168.0.49:1521:orcl");
//		params.put(DBParam.JDBC_USER_NAME, "dangkasync_ql");
//		params.put(DBParam.JDBC_PASSWORD, "123456789");
		
//		params.put(ETLResourceConsist.ID, "1");
//		params.put(ETLResourceConsist.NAME, "锦江监狱");
//		params.put(ETLResourceConsist.TYPE, DBETLResourceFactory.DB_TYPE);
//		params.put(DBParam.JDBC_URL, "jdbc:sqlserver://192.168.0.122:1433;databaseName=JGXT");
//		params.put(DBParam.JDBC_USER_NAME, "sa");
//		params.put(DBParam.JDBC_PASSWORD, "sa");
		
//		result.add(params);
		
		List<Map<String, Object>> inputResources = new ArrayList<>();
		try {
			inputResources = remotingGenericService.getInputResources(jobBizId, taskBizId);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("RPC调用失败(getInputResources)：" + e.getMessage());
		}
		
		return inputResources;
	}

	@Override
	public List<Map<String, Object>> getOutputResources(String jobBizId,String taskBizId) {
//		List<Map<String, Object>> result = new ArrayList<>();
//		Map<String, Object> params = new HashMap<>();
//		params.put(ETLResourceConsist.ID, "2");
//		params.put(ETLResourceConsist.NAME, "金堂监狱");
//		params.put(ETLResourceConsist.TYPE, DBETLResourceFactory.DB_TYPE);
//		params.put(DBParam.JDBC_URL, "jdbc:mysql://192.168.0.94:3306/etl1?useUnicode=true&characterEncoding=utf8");
//		params.put(DBParam.JDBC_USER_NAME, "root");
//		params.put(DBParam.JDBC_PASSWORD, "");
		
//		params.put(ETLResourceConsist.ID, "2");
//		params.put(ETLResourceConsist.NAME, "金堂监狱");
//		params.put(ETLResourceConsist.TYPE, DBETLResourceFactory.DB_TYPE);
//		params.put(DBParam.JDBC_URL, "jdbc:oracle:thin:@192.168.0.49:1521:orcl");
//		params.put(DBParam.JDBC_USER_NAME, "dangkasync_ql");
//		params.put(DBParam.JDBC_PASSWORD, "123456789");
		
//		params.put(ETLResourceConsist.ID, "2");
//		params.put(ETLResourceConsist.NAME, "金堂监狱");
//		params.put(ETLResourceConsist.TYPE, DBETLResourceFactory.DB_TYPE);
//		params.put(DBParam.JDBC_URL, "jdbc:sqlserver://192.168.0.122:1433;databaseName=JGXT");
//		params.put(DBParam.JDBC_USER_NAME, "sa");
//		params.put(DBParam.JDBC_PASSWORD, "sa");
		
//		result.add(params);
		
		List<Map<String, Object>> outputResources = new ArrayList<>();
		try {
			outputResources = remotingGenericService.getOutputResources(jobBizId, taskBizId);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("RPC调用失败(getOutputResources)：" + e.getMessage());
		}
		return outputResources;
	}

	@Override
	public JobConfig getJobConfig(String jobId) {
//		JobConfig jobConfig = new JobConfig();
//		jobConfig.setId("1");
//		jobConfig.setDesc("这是一个非常棒的作业");
//		jobConfig.setName("作业A");
//		jobConfig.setSchedule("*/40 * * * * ?");
//		jobConfig.setSchedule("0 */1 * * * ?");
		
		JobConfig jobConfig = null;
		try {
			jobConfig = remotingGenericService.getJobConfig(jobId);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("RPC调用失败(saveLog)：" + e.getMessage());
		}
		return jobConfig;
	}

	@Override
	public void saveLog(JobLogCollector jobLogCollector) {
		try {
			remotingBusinessService.saveLog(jobLogCollector);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("RPC调用失败(saveLog)：" + e.getMessage());
		}
	}

	
	
}
