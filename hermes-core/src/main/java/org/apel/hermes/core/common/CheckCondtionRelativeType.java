package org.apel.hermes.core.common;

/**
 * 目标数据库数据源的检查相对条件
 * @author lijian
 *
 */
public enum CheckCondtionRelativeType {
	
	AND("and"),OR("or");
	
	private String type;
	
	private CheckCondtionRelativeType(String type){
		this.type = type;
	}
	
	public String toString(){
		return this.type;
	}
	
}
