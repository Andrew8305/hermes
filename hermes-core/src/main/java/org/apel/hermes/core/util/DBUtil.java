package org.apel.hermes.core.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作工具类
 * @author lijian
 *
 */
public class DBUtil {

	public static List<String> getColumnNames(Connection conn, String tableName){
		List<String> columnNames = new ArrayList<>();
		try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM " + tableName);
				ResultSet rs = pst.executeQuery();){
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				columnNames.add(rsmd.getColumnLabel(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return columnNames;
	}
	
	
	
}
