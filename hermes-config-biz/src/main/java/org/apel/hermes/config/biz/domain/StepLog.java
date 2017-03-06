package org.apel.hermes.config.biz.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "etl_step_log")
public class StepLog {

	@Id
	private String id;

	private Date createDate;
	
	// 顺序
	private Integer  seq;
	// 详细日志记录
	@Column(length=4000)
	private String  logDetails;
	// 开始时间
	private Date  startTime;
	// 持续时间
	private String  duration;
	// 结束时间
	private Date  endTime;
	// 任务日志ID
	private String  taskLogID;
	// 结果描述
	@Column(length=4000)
	private String  resultDesc;
	// 结果
	private Boolean  result;

	
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
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getTaskLogID() {
		return taskLogID;
	}
	public void setTaskLogID(String taskLogID) {
		this.taskLogID = taskLogID;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getLogDetails() {
		return logDetails;
	}

	public void setLogDetails(String logDetails) {
		this.logDetails = logDetails;
	}

}
