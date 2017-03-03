package org.apel.hermes.core.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apel.hermes.config.api.domain.JobLogCollector;
import org.apel.hermes.core.context.ETLContext;
import org.apel.hermes.core.listener.JobListener;
import org.apel.hermes.core.remoting.RemotingService;
import org.apel.hermes.core.schedule.QuartzJob;
import org.apel.hermes.core.task.ETLTask;
import org.apel.hermes.core.util.ETLListenerUtil;
import org.apel.hermes.core.util.SchedulerUtil;
import org.apel.hermes.core.util.ThreadUtil;
import org.apel.hermes.core.util.TimeComputer;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 同步作业的抽象类，实现了顶层job接口，作为一各适配器存在，
 * 自己的job实际上在真正使用过程当中必须继承该类
 * @author lijian
 *
 */
public abstract class GenericETLJob implements ETLJob {
	
	@Autowired
	private RemotingService remotingService;

	private static Logger LOG = Logger.getLogger(GenericETLJob.class);
	
	private Scheduler scheduler = SchedulerUtil.createScheduler();
	
	protected ETLContext context;
	
	protected String jobName;
	
	protected String schedule;
	
	protected Map<String, ETLTask> allTasks = new HashMap<>();
	
	private JobListener jobListener;
	
	public GenericETLJob() {
		if(this instanceof JobListener){
			this.jobListener = (JobListener)this;
		}
	}

	@Override
	public void start() {
		//创建日志收集器，用于对ETL逻辑中产生关键信息进行记录，并翻译成普通文本信息并通过RPC调用远端进行记录
		final JobLogCollector jobLogCollector = JobLogCollector.create();
		jobLogCollector.setJobId(id());
		jobLogCollector.setJobName(getName());
		jobLogCollector.setStartTime(new Date());
		LOG.info("ETL作业[" + id() + "]" + "开始运行");
		Long startTime = System.currentTimeMillis();
		//监听器回调点
		ETLListenerUtil.jobOnStart(this.jobListener);
		if(allTasks.size() == 1){//只有一个元素不需要为任务开启多线程
			for (ETLTask etlTask : allTasks.values()) {
				etlTask.start(jobLogCollector);
			}
		}else{
			List<Thread> threads = new ArrayList<>();
			for (final ETLTask etlTask : allTasks.values()) {
				Thread t = new Thread(){
					@Override
					public void run() {
						etlTask.start(jobLogCollector);
					}
				};
				threads.add(t);
				t.start();
			}
			ThreadUtil.join(threads);
		}
		//监听器回调点
		ETLListenerUtil.jobOnEnd(this.jobListener);
		Long endTime = System.currentTimeMillis();
		LOG.info(TimeComputer.computeDuration("ETL作业[" + id() + "]" + "结束运行-----运行时间为", startTime, endTime));
		jobLogCollector.setDuration(TimeComputer.computeDuration("", startTime, endTime));
		jobLogCollector.setEndTime(new Date());
		//调用配置中心接口存储日志
		remotingService.saveLog(jobLogCollector);
	}

	@Override
	public void schedule() {
		//cron = */5 * * * * ?
		//初始化quartz定时器，并运行
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(this.id());
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(getSchedule());
			CronTrigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerKey).withSchedule(scheduleBuilder)
					.build();
			JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
					.withIdentity(trigger.getJobKey()).build();
			jobDetail.getJobDataMap().put("job", this);
			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("执行job定时器任务失败：" + e.getMessage());
		}
	}

	@Override
	public void setName(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public String getName() {
		return this.jobName;
	}

	@Override
	public String getSchedule() {
		return this.schedule;
	}

	@Override
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	@Override
	public void addTask(ETLTask task) {
		task.setContext(context);
		allTasks.put(task.id(), task);
	}

	@Override
	public void removeTask(String taskId) {
		allTasks.remove(taskId);
	}

	@Override
	public void setContext(ETLContext context) {
		this.context = context;
	}

	@Override
	public ETLContext getContext() {
		return this.context;
	}

	@Override
	public Scheduler scheduler() {
		return this.scheduler;
	}

	@Override
	public Map<String, ETLTask> getAllTasks() {
		return this.allTasks;
	}
	
	
}
