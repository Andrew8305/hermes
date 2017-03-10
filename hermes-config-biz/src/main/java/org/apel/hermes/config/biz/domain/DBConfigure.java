package org.apel.hermes.config.biz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "etl_db_configure")
public class DBConfigure implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6130624578239579001L;

	@Id
	private String id;

	private Date createDate;
	
	// dbName
	private String  dbName;
	// username
	private String  username;
	// dbKey
	private String  dbKey;
	// url
	private String  url;
	// password
	private String  password;

	
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
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDbKey() {
		return dbKey;
	}
	public void setDbKey(String dbKey) {
		this.dbKey = dbKey;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
