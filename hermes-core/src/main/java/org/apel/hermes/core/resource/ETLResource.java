package org.apel.hermes.core.resource;

/**
 * ETL同步源接口，对所有不同类型的源进行了抽象，如文件，数据库，webservice等
 * @author lijian
 *
 */
public interface ETLResource {

	/**
	 * 源id
	 */
	String id();
	
	/**
	 * 设置源id
	 */
	void id(String id);
	
	/**
	 * 源名称
	 */
	String name();
	
	/**
	 * 设置源名称
	 */
	void name(String name);
	
}
