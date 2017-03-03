package org.apel.hermes.core.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformUtils;

/**
 * 数据库类型探测器----单例
 * 重写了determineDatabaseType方法用于支撑dm数据库
 * 调用determineDatabaseType方法对db数据源进行探测
 * @author lijian
 *
 */
public class DBTypeDetector extends PlatformUtils{
	
	private static DBTypeDetector detector = new DBTypeDetector();
	
	private DBTypeDetector(){}
	
	public static DBTypeDetector getInstance(){
		return detector;
	}

	@Override
	public String determineDatabaseType(DataSource dataSource)
			throws DatabaseOperationException {
		if(StringUtils.isEmpty(super.determineDatabaseType(dataSource))){
			String type = null;
			try {
				Connection conn = dataSource.getConnection();
				type = conn.getMetaData().getDatabaseProductName();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return type;
		}else{
			return super.determineDatabaseType(dataSource);
		}
	}

	
	
}
