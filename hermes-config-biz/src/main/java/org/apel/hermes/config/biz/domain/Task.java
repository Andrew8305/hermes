package org.apel.hermes.config.biz.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


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
	
	@Transient
	private List<DBConfigure> inList;
	
	@Transient
	private List<DBConfigure> outList;

	
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

	public List<DBConfigure> getInList() {
		return inList;
	}

	public void setInList(List<DBConfigure> inList) {
		this.inList = inList;
	}

	public List<DBConfigure> getOutList() {
		return outList;
	}

	public void setOutList(List<DBConfigure> outList) {
		this.outList = outList;
	}

	

}
