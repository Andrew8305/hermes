package org.apel.hermes.config.provider.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 转换工具类
 * @author msk
 *
 */
public class ConvertUtil {
	
	public static List<Map<String,Object>> listObject2ListMap(List<?> objs){
		List<Map<String,Object>> result = new ArrayList<>();
		for(Object obj:objs){
			result.add(object2Map(obj));
		}
		return result;
	}
	
	public static Map<String, Object> object2Map(Object obj){
		Map<String, Object> map = new HashMap<>();
        // System.out.println(obj.getClass());  
        // 获取对象对应类中的所有属性域  
        Field[] fields = obj.getClass().getDeclaredFields();  
        for (int i = 0, len = fields.length; i < len; i++) {  
            String varName = fields[i].getName();  
            try {  
                // 获取原来的访问控制权限  
                boolean accessFlag = fields[i].isAccessible();  
                // 修改访问控制权限  
                fields[i].setAccessible(true);  
                // 获取在对象中属性fields[i]对应的对象中的变量  
                Object o = fields[i].get(obj);  
                if (o != null)  
                    map.put(varName, o);  
                // System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);  
                // 恢复访问控制权限  
                fields[i].setAccessible(accessFlag);  
            } catch (IllegalArgumentException ex) {  
                ex.printStackTrace();  
            } catch (IllegalAccessException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return map;   
	 }  
	
	public static String listString2String(List<String> list,String separator){
		StringBuffer sb = new StringBuffer();
		if(list==null){
			return null;
		}
		if(StringUtils.isEmpty(separator)){
			separator=",";
		}
		for(int i=0;i<list.size();i++){
			sb.append(list.get(i));
			if(i<list.size()-1){
				sb.append(separator);
			}
		}
		return sb.toString();
	}
	
}
