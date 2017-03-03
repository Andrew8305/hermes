package org.apel.hermes.core.resource.db;

import javax.sql.DataSource;

import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.util.DBTypeDetector;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库源，作为数据库同步程序的输入源和输出源
 * @author lijian
 *
 */
public class DBETLResource implements ETLResource {

	protected String id;

	protected String name;
	
	protected DataSource ds;

	protected JdbcTemplate JdbcTemplate;

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public void id(String id) {
		this.id = id;
	}

	@Override
	public void name(String name) {
		this.name = name;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public JdbcTemplate getJdbcTemplate() {
		return JdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		JdbcTemplate = jdbcTemplate;
	}

	/**
	 * 通过探测器探测数据库数据源的类型，其中4种会作为ETL框架分页使用(mysql sqlserver oracle dm)
	 */
	public String type() {
		return DBTypeDetector.getInstance().determineDatabaseType(this.ds);
	}

}
