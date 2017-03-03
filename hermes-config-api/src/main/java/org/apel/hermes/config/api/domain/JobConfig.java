package org.apel.hermes.config.api.domain;

import java.io.Serializable;

/**
 * ETL中心远程传输job对象
 * @author lijian
 *
 */
public class JobConfig implements Serializable{

	private static final long serialVersionUID = -2182310770775707183L;

	private String id;

	private String name;

	private String desc;

	private String schedule;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

}
