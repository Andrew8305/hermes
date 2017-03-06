package org.apel.hermes.config.biz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apel.hermes.config.biz.enums.TemplateEnum;


@Entity
@Table(name = "etl_datasource_type")
public class DataSourceType implements Serializable{
	private static final long serialVersionUID = -2872369982561547174L;
	public DataSourceType() {
	}

	public DataSourceType(Integer id, Date createDate, String param, TemplateEnum templateType) {
		this.id = id;
		this.param = param;
		this.templateType = templateType;
	}
	
	
	public DataSourceType(Integer id, String param) {
		this.id = id;
		this.param = param;
	}
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private Integer seq;
	// 类型字段
	private String  param;
	@Enumerated(EnumType.STRING)
    @Column(name = "templateType")
	private TemplateEnum templateType;

	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
	public TemplateEnum getTemplateType() {
		return this.templateType;
	}
	public void setTemplateType(TemplateEnum templateType) {
		this.templateType = templateType;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	
	
}
