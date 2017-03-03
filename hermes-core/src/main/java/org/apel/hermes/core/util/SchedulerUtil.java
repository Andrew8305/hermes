package org.apel.hermes.core.util;

import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时器创建静态工具
 * @author lijian
 *
 */
public class SchedulerUtil {

	public static Scheduler createScheduler(){
		SchedulerFactoryBean schedulerFactoryBean;
		try {
			schedulerFactoryBean = new SchedulerFactoryBean();
			schedulerFactoryBean.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return schedulerFactoryBean.getObject();
	}
	
}
