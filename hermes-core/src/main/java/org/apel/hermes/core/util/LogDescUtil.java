package org.apel.hermes.core.util;

/**
 * @author Administrator
 *
 */
public class LogDescUtil {

	/**
	 * 单个任务子线程的日志id
	 * @param taskId 当前任务的标识
	 * @param inputId 输入源的标识
	 * @param inputName 输入源的名称
	 * @param outputId 输出源的标识
	 * @param outputName 输出源的名称
	 */
	public static String taskThreadId(String taskId, 
			String inputId, String inputName, 
			String outputId, String outputName){
		String desc = "[从#inName#(#inId#)到#outName#(#outId#)]";
		desc = desc.replaceAll("#inName#", inputName)
				.replaceAll("#inId#", inputId)
				.replaceAll("#outId#", outputId)
				.replaceAll("#outName#", outputName);
		return taskId + desc;
	}

	/**
	 * 公用任务子线程日志描述
	 * @param taskId 当前任务的标识
	 * @param inputId 输入源的标识
	 * @param inputName 输入源的名称
	 * @param outputId 输出源的标识
	 * @param outputName 输出源的名称
	 * @param suffixDesc 后置字符串描述	
	 */
	public static String commonTaskDesc(String taskId, 
			String inputId, String inputName, 
			String outputId, String outputName, String suffixDesc){
		return ("ETL任务，任务标识为[#taskId#]的子线程---"
				+ "(从(id:#inputId#,name:#inputName#)到"
				+ "(id:#outputId#,name:#outputName#))"
				+ suffixDesc)
				.replace("#taskId#", taskId)
				.replace("#inputId#", inputId)
				.replace("#inputName#", inputName)
				.replace("#outputId#", outputId)
				.replace("#outputName#", outputName);
	}
	
	/**
	 * 公用任务子线程日志描述
	 * @param taskId 当前任务的标识
	 * @param inputId 输入源的标识
	 * @param inputName 输入源的名称
	 * @param outputId 输出源的标识
	 * @param outputName 输出源的名称
	 * @param suffixDesc 后置字符串描述	
	 */
	public static String commonStepDesc(int stepOrder, 
			String inputId, String inputName, 
			String outputId, String outputName, String sql, String suffixDesc){
		return ("ETL步骤，步骤数为[#stepOrder#], 同步语句为：#sql#,"
				+ "位于子线程---"
				+ "(从(id:#inputId#,name:#inputName#)到"
				+ "(id:#outputId#,name:#outputName#))的步骤"
				+ suffixDesc)
				.replace("#stepOrder#", String.valueOf(stepOrder))
				.replace("#sql#", sql)
				.replace("#inputId#", inputId)
				.replace("#inputName#", inputName)
				.replace("#outputId#", outputId)
				.replace("#outputName#", outputName);
	}
	
}
