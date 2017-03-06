package org.apel.hermes.config.biz.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "etl_task_log")
public class TaskLog {

	@Id
	private String id;

	private Date createDate;
	// 任务BizID
	private String taskBizId;
	// 作业日志ID
	private String  jobLogId;
	// 开始时间
	private Date  startTime;
	// 持续时间
	private String  duration;
	// 结果描述
	@Column(length=4000)
	private String  resultDesc;
	// 结束时间
	private Date  endTime;
	// 结果
	private Boolean  result;
	
	@Transient
	private List<StepLog> stepLogs=new ArrayList<>();

	
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
	
	
	public String getJobLogId() {
		return jobLogId;
	}

	public void setJobLogId(String jobLogId) {
		this.jobLogId = jobLogId;
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
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}

	public List<StepLog> getStepLogs() {
		return stepLogs;
	}

	public void setStepLogs(List<StepLog> stepLogs) {
		this.stepLogs = stepLogs;
	}

	public String getTaskBizId() {
		return taskBizId;
	}

	public void setTaskBizId(String taskBizId) {
		this.taskBizId = taskBizId;
	}

}
