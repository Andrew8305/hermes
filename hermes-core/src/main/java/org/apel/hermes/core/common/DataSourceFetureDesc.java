package org.apel.hermes.core.common;

public class DataSourceFetureDesc {

	private String id;

	private String name;

	private DataSourceFeture dataSourceFeture;
	
	public DataSourceFetureDesc(){
		
	}

	public DataSourceFetureDesc(String id, String name,
			DataSourceFeture dataSourceFeture) {
		this.id = id;
		this.name = name;
		this.dataSourceFeture = dataSourceFeture;
	}

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

	public DataSourceFeture getDataSourceFeture() {
		return dataSourceFeture;
	}

	public void setDataSourceFeture(DataSourceFeture dataSourceFeture) {
		this.dataSourceFeture = dataSourceFeture;
	}

}
