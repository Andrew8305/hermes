package org.apel.hermes.config.biz.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "etl_task")
public class Task {

	@Id
	private String id;

	private Date createDate;
	
	// 输出源
	private String  output;
	// 输入源
	private String  input;
	// 作业
	private String  jobId;
	// 任务业务ID
	private String  taskBizId;

	

	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskBizId() {
		return taskBizId;
	}

	public void setTaskBizId(String taskBizId) {
		this.taskBizId = taskBizId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
