package org.apel.hermes.config.biz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "etl_task")
public class Task implements Serializable{

	private static final long serialVersionUID = -5945832917476088451L;

	@Id
	private String id;

	private Date createDate;
	// 任务KEY
	private String  taskKey;
	
	@ManyToOne
	@JoinColumn(name="job")
	private Job job;
	
	// 输入源
	@ManyToOne
	@JoinColumn(name="db_in")
	private DBConfigure  dbInput;
	// 输出源
	@ManyToOne
	@JoinColumn(name="db_out")
	private DBConfigure  dbOutput;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTaskKey() {
		return taskKey;
	}
	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public DBConfigure getDbInput() {
		return dbInput;
	}

	public void setDbInput(DBConfigure dbInput) {
		this.dbInput = dbInput;
	}

	public DBConfigure getDbOutput() {
		return dbOutput;
	}

	public void setDbOutput(DBConfigure dbOutput) {
		this.dbOutput = dbOutput;
	}
	
	

}
