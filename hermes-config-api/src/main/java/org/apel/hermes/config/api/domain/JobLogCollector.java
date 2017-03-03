package org.apel.hermes.config.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作业日志收集器
 * @author lijian
 *
 */
public class JobLogCollector implements Serializable{
	
	private static final long serialVersionUID = -1068593455539447546L;

	private JobLogCollector(){}
	
	private String jobId;
	
	private String jobName;
	
	private Date startTime;
	
	private Date endTime;
	
	private String duration;
	
	private List<TaskLogCollector> taskLogCollectors = new ArrayList<>();
	
	public void addTaskLogCollector(TaskLogCollector taskLogCollector){
		taskLogCollectors.add(taskLogCollector);
	}
	
	public void addTaskLogCollectors(List<TaskLogCollector> taskLogCollectors){
		this.taskLogCollectors.addAll(taskLogCollectors);
	}
	
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public List<TaskLogCollector> getTaskLogCollectors() {
		return taskLogCollectors;
	}

	public void setTaskLogCollectors(List<TaskLogCollector> taskLogCollectors) {
		this.taskLogCollectors = taskLogCollectors;
	}
	
	public static JobLogCollector create(){
		return new JobLogCollector();
	}
	
}
