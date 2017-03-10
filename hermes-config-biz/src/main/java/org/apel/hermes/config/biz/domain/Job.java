package org.apel.hermes.config.biz.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "etl_job")
public class Job implements Serializable{

	@Id
	private String id;
	private Date createDate;

	// 作业名称
	private String  jobName;
	// 描述
	private String  description;

	// 作业KEY
	private String  jobKey;
	// cron表达式
	private String  schedule;
	
	
	@Transient
	private List<String> inList;
	
	@Transient
	private List<String> outList;

	
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
	
	public String getJobKey() {
		return jobKey;
	}
	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getInList() {
		return inList;
	}

	public void setInList(List<String> inList) {
		this.inList = inList;
	}

	public List<String> getOutList() {
		return outList;
	}

	public void setOutList(List<String> outList) {
		this.outList = outList;
	}

	
	

}
