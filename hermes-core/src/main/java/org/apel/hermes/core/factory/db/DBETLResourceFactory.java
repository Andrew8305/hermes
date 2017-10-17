package org.apel.hermes.core.factory.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apel.hermes.common.consist.DBParam;
import org.apel.hermes.common.consist.ETLResourceConsist;
import org.apel.hermes.core.cache.DataBaseCacher;
import org.apel.hermes.core.factory.ETLResourceFactory;
import org.apel.hermes.core.resource.db.DBETLResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据库数据源创建泛型工厂，将在初始化器当中使用工厂创建对应的源对象
 * 源泛型工厂可以在环境当中水平扩展，只要实现了ETLResourceFactory，则在ETL初始化器当中都可会被找到用于创建源对象
 * @author lijian
 *
 */
@Component
public class DBETLResourceFactory implements ETLResourceFactory<DBETLResource>{

	@Value("${minIdle:0}")
	private int minIdle;
	@Value("${maxActive:200}")
	private int maxActive;
	
	private static Logger LOG = Logger.getLogger(DBETLResourceFactory.class); 
	
	public static final String DB_TYPE = "DB";
	
	@Override
	public DBETLResource createResource(Map<String, Object> params) {
		if(checkParams(params)){
			DBETLResource resource = new DBETLResource();
			resource.id(params.get(ETLResourceConsist.ID).toString());
			resource.name(params.get(ETLResourceConsist.NAME).toString());
			//首先从数据库缓存当中去获取数据源，如果拿到了直接使用，无需再进行构建，如果没有拿到，再进行创建
			DataSource ds = DataBaseCacher.getInstance().get(params.get(ETLResourceConsist.ID).toString());
			if(ds == null){//缓存当中没有数据源，创建数据源
				ds = createDataSource(params);
				try (Connection conn = ds.getConnection();){
					conn.createStatement().executeQuery("select 'x' from dual");
				} catch (SQLException e) {
					LOG.warn("(标识-" +  params.get(ETLResourceConsist.ID) + ", 名称-" + params.get(ETLResourceConsist.NAME) + ")的数据源连接失败，装配作业失败");
				}
				DataBaseCacher.getInstance().addDataSource(params.get(ETLResourceConsist.ID).toString(), ds);
			}
			JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
			resource.setDs(ds);
			resource.setJdbcTemplate(jdbcTemplate);
			return resource;
		}else{
			return null;
		}
	}

	/**
	 * 创建c3p0数据库连接池
	 */
	private DataSource createDataSource(
			Map<String, Object> params) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(params.get(DBParam.JDBC_URL).toString());
		dataSource.setUsername(params.get(DBParam.JDBC_USER_NAME).toString());
		dataSource.setPassword(params.get(DBParam.JDBC_PASSWORD).toString());
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		return dataSource;
	}

	@Override
	public String type() {
		return DB_TYPE;
	}

	@Override
	public boolean checkParams(Map<String, Object> params) {
		Object id = params.get(ETLResourceConsist.ID);
		Object name = params.get(ETLResourceConsist.NAME);
		Object url = params.get(DBParam.JDBC_URL);
		Object userName = params.get(DBParam.JDBC_USER_NAME);
		if(id == null || name == null || url == null || userName == null){
			LOG.error("配置中心源信息配置缺少关键信息，创建数据库源失败，请检查!");
			return false;
		}
		return true;
	}

}
