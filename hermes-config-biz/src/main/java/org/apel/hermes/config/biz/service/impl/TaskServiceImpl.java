package org.apel.hermes.config.biz.service.impl;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.dao.JobRepository;
import org.apel.hermes.config.biz.dao.TaskRepository;
import org.apel.hermes.config.biz.domain.Job;
import org.apel.hermes.config.biz.domain.Task;
import org.apel.hermes.config.biz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TaskServiceImpl extends AbstractBizCommonService<Task, String> implements TaskService{

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private JobRepository jobRepository;
	
	@Override
	protected CommonRepository<Task, String> getRepository() {
		return taskRepository;
	}

	@Override
	protected String getPageQl() {
		return "select t from Task t where 1=1";
	}

	@Override
	public List<Task> findByJobId(String jobId) {
		return taskRepository.findByJobId(jobId);
	}

	@Override
	public List<String> findTaskIdsByJobId(String jobBizId) {
		List<Job> jobs = jobRepository.findByJobBizId(jobBizId);
		String jobId="";
		if(CollectionUtils.isEmpty(jobs)){
			throw new RuntimeException("没有找到jobBizId["+jobBizId+"]对应的Job");
		}
		jobId=jobs.get(0).getId();
		
		List<Task> tasks = taskRepository.findByJobId(jobId);
		List<String> ids=new ArrayList<>();
		for(Task task:tasks){
			ids.add(task.getTaskBizId());
		}
		return ids;
	}

	@Override
	public Map<String, String> checkIsRepeat(String jobId,String taskBizId) throws Exception {
		List<Task> tasks = taskRepository.findByJobIdAndTaskBizId(jobId,taskBizId);
		Map<String,String> map = new HashMap<String,String>();
		map.put("isRepeat", "false");
		if(tasks!=null && tasks.size()>0){
			map.put("isRepeat", "true");
		}
		return map;
	}
	
	public static void main(String[] args) throws Exception {
		 String url = "jdbc:mysql://192.168.0.94:3306/my_test?"
	                + "useUnicode=true&characterEncoding=UTF8";
		 String userName= "root";
		 String password = "";
		 Connection connection = DriverManager.getConnection(url,userName,password);
		 String sql = "select * from cas_app_client where enabled = b'1'";
		 PreparedStatement prepareStatement = connection.prepareStatement(sql);
		 ResultSet rs = prepareStatement.executeQuery();
		 List<String> urls = new ArrayList<>();
		 while(rs.next()){
			 String url1 = rs.getString("url");
			 URL urlComponent = new URL(url1);
			 URLConnection openConnection = urlComponent.openConnection();
			 openConnection.setConnectTimeout(800);
			 try {
				openConnection.connect();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			urls.add(url1);
		 }
		 for (String str : urls) {
			System.out.println(str);
		}
	}



}
