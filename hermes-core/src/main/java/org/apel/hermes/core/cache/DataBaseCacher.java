package org.apel.hermes.core.cache;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 数据源缓存对象，缓存上下文当中所有的数据库连接源
 * 单例
 * @author lijian
 *
 */
public class DataBaseCacher{
	
	private static DataBaseCacher cacher = new DataBaseCacher();
	
	private Map<String, DataSource> dataSources = new HashMap<>();
	
	private DataBaseCacher(){}
	
	public static DataBaseCacher getInstance(){
		return cacher;
	}
	
	public void addDataSource(String id, DataSource dataSource){
		dataSources.put(id, dataSource);
	}
	
	public void removeDataSource(String id){
		dataSources.remove(id);
	}
	
	public DataSource get(String id){
		return dataSources.get(id);
	}
	
	public void clear(){
		dataSources.clear();
	}
	
}
