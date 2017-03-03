package org.apel.hermes.core.job;

import java.util.Map;

import org.apel.hermes.core.context.ETLContextSetting;
import org.apel.hermes.core.task.ETLTask;
import org.quartz.Scheduler;

/**
 * 同步作业的顶层接口，所有的job必须实现该接口
 * @author lijian
 *
 */
/**
 * @author Administrator
 *
 */
public interface ETLJob extends ETLContextSetting{
	
	final static String SCHEDULE_NONE = "N/A";

	/**
	 * 作业id
	 */
	String id();
	
	/**
	 * 开启作业
	 */
	void start();
	
	/**
	 * 获取作业的定时cron
	 */
	String getSchedule();
	
	/**
	 * 设置作业的定时cron
	 */
	void setSchedule(String schedule);
	
	/**
	 * 往作业中添加任务
	 */
	void addTask(ETLTask task);
	
	/**
	 * 在作业中移除任务
	 */
	void removeTask(String taskId);
	
	/**
	 * 定时运行作业
	 */
	void schedule();
	
	/**
	 * 设置作业名称
	 */
	void setName(String jobName);
	
	/**
	 * 获取作业名称
	 */
	String getName();
	
	/**
	 * 获取作业当中的quartz定时器
	 */
	Scheduler scheduler();
	
	/**
	 * 获取作业当中所有的任务
	 */
	Map<String, ETLTask> getAllTasks();
	
}
