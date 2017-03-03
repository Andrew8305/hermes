package org.apel.hermes.core.schedule;

import org.apel.hermes.core.job.ETLJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 运行同步job的定时器
 * 内部定时逻辑实现为直接开启一个job
 * @author lijian
 *
 */
public class QuartzJob implements Job{
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ETLJob job = (ETLJob)context.getJobDetail().getJobDataMap().get("job");
		job.start();
	}

}
