package org.apel.hermes.config.biz.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "etl_job_log")
public class JobLog {

	@Id
	private String id;

	private Date createDate;
	
	// 作业名称
	private String  jobName;
	// 结束时间
	private Date  endTime;
	// 作业BizID
	private String  jobBizId;
	// 开始时间
	private Date  startTime;
	// 持续时间
	private String  duration;

	@Transient
	private List<TaskLog> taskLogs=new ArrayList<>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public List<TaskLog> getTaskLogs() {
		return taskLogs;
	}

	public void setTaskLogs(List<TaskLog> taskLogs) {
		this.taskLogs = taskLogs;
	}

	public String getJobBizId() {
		return jobBizId;
	}

	public void setJobBizId(String jobBizId) {
		this.jobBizId = jobBizId;
	}

	
	

}
