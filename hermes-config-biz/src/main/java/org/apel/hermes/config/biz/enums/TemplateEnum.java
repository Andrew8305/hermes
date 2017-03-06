package org.apel.hermes.config.biz.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TemplateEnum{
	
	DB("数据库"),FILE("文件");
	
	
	private static List<Map<String,Object>> list = new ArrayList<>();
	
	static{
		TemplateEnum [] enums = TemplateEnum.values();
		for(TemplateEnum template:enums){
			Map<String,Object> map = new HashMap<String,Object>();	
			map.put("code", template.name());
			map.put("name", template.getType());
			list.add(map);
		}
	}
	
	
	private String type;
	
	private TemplateEnum(String type){
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static List<Map<String,Object>>  getAllType(){
		return list;
	}
	
	@Override
	public String toString(){
		return this.type;
	}
	
	public static TemplateEnum getTemplateEnumByCode(String code){
		TemplateEnum [] enums = TemplateEnum.values();
		for(TemplateEnum templateEnum :enums){
			if(templateEnum.name().equals(code)){
				return templateEnum;
			}
		}
		
		return null;
	}
	
	
}
