package org.apel.hermes.config.biz.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "etl_job")
public class Job {

	@Id
	private String id;// 作业ID

	private Date createDate;
	
	// 输入源
	private String  input;
	// 作业名称
	private String  name;
	// 描述
	private String  description;
	// 执行时间
	private String  schedule;
	// 输出源
	private String  output;
	// 作业业务ID
	private String  jobBizId;

	

	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJobBizId() {
		return jobBizId;
	}

	public void setJobBizId(String jobBizId) {
		this.jobBizId = jobBizId;
	}

	
}
