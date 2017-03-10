package org.apel.hermes.config.biz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "etl_job_dbconfigure")
public class JobDBConfigure implements Serializable{
	private static final long serialVersionUID = -1601670658496922709L;
	@Id
	private String id;
	
	@ManyToOne
	@JoinColumn(name="job")
	private Job job;
	
	@ManyToOne
	@JoinColumn(name="in_dbconfigure")
	private DBConfigure inDBconfigure;
	
	@ManyToOne
	@JoinColumn(name="out_dbconfigure")
	private DBConfigure outDBconfigure;
	
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}


	public DBConfigure getInDBconfigure() {
		return inDBconfigure;
	}

	public void setInDBconfigure(DBConfigure inDBconfigure) {
		this.inDBconfigure = inDBconfigure;
	}

	public DBConfigure getOutDBconfigure() {
		return outDBconfigure;
	}

	public void setOutDBconfigure(DBConfigure outDBconfigure) {
		this.outDBconfigure = outDBconfigure;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
