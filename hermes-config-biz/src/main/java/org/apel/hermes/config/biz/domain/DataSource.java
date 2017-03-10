package org.apel.hermes.config.biz.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "etl_datasource")
public class DataSource implements Serializable {

	private static final long serialVersionUID = -8600806161612230055L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;// 数据源ID

	
	private Integer seq; 
	
	
	private String paramValue;
	
	private String businessCode;//业务数据源Id标识
	
	private String businessName;
	
	private String templateCode;//数据字典 TemplateEnum
	
	@ManyToOne
	@JoinColumn(name="datasourceTypeId")
	private DataSourceType datasourceType;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public DataSourceType getDatasourceType() {
		return datasourceType;
	}

	public void setDatasourceType(DataSourceType datasourceType) {
		this.datasourceType = datasourceType;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	
	
	
	

	
	

	

}
