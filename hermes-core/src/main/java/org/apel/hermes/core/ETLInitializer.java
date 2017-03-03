package org.apel.hermes.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apel.hermes.common.consist.ETLResourceConsist;
import org.apel.hermes.config.api.domain.JobConfig;
import org.apel.hermes.core.context.ETLContext;
import org.apel.hermes.core.factory.ETLResourceFactory;
import org.apel.hermes.core.job.ETLJob;
import org.apel.hermes.core.monitor.MonitorClient;
import org.apel.hermes.core.remoting.RemotingService;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.step.ETLStep;
import org.apel.hermes.core.task.ETLTask;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ETL程序初始化器(同步程序主入口)
 * <ul>
 * 	<li>1 加载spring容器当中已经存在的数据源工厂(泛型工厂)</li>
 * 	<li>2 加载spring容器当中已经存在的自定义step</li>
 * 	<li>3 加载spring容器当中已经存在的task</li>
 * 	<li>4 加载spring容器当中已经存在的job</li>
 * 	<li>5 根据ETL管理中心加载对应的配置，
 * 		使得当前存在于spring容器当中的所有job能够完成自动装配(根据id进行对应，自动装配内部的task以及task内部的step)，
 * 		在装配的同时能够完成配置中心上对应数据源的初始化工作	
 * 	</li>
 * 	<li>
 * 		6 启动job的定时器
 * 	</li>
 * </ul>
 * @author lijian
 *
 */
@Component
public class ETLInitializer implements ApplicationContextAware, InitializingBean{
	
	private static Logger LOG = Logger.getLogger(ETLInitializer.class);
	
	//引用远程调用组件，可与ETL配置中心进行通讯，按配置中心的配置来装配当前环境的作业
	@Autowired
	private RemotingService remotingService;
	
	@Value("${monitor.server.ip}")
	private String monitorServerIp;
	@Value("${monitor.server.port}")
	private String monitorServerPort;
	
	//装配当前的上下文环境(空context)
	@Autowired
	private ETLContext context;
	
	private ApplicationContext applicationContext;
	
	/**
	 * 在初始化容器后，进行当前上下文环境的检测，然后对ETL作业进行装配
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void afterPropertiesSet() throws Exception {
		//获取当前环境中所有的作业
		Map<String, ETLJob> jobs = this.applicationContext.getBeansOfType(ETLJob.class);
		//获取当前环境中所有的任务
		Map<String, ETLTask> tasks = this.applicationContext.getBeansOfType(ETLTask.class);
		//获取当前环境中所有的step
		Map<String, ETLStep> steps = this.applicationContext.getBeansOfType(ETLStep.class);
		//获取当前环境中所有的泛型源工厂
		Map<String, ETLResourceFactory> resourceFactories = this.applicationContext.getBeansOfType(ETLResourceFactory.class);
		//装配ETL作业
		assembleJob(jobs, tasks, steps, resourceFactories);
	}

	@SuppressWarnings("rawtypes")
	private void assembleJob(Map<String, ETLJob> jobs,
			Map<String, ETLTask> tasks, Map<String, ETLStep> steps,
			Map<String, ETLResourceFactory> resourceFactories) {
		//装配ETL作业,如果装配一个作业成功，这个作业会被加入当context上下文当中，在后期的心跳检测中会通知到ETL配置中心，让配置中心上的作业变为激活状态
		//激活状态下的作业可以实现关闭和开启操作
		f1:for (ETLJob job : jobs.values()) {
			//通过RPC获取到配置中心的job配置信息
			JobConfig jobConfig = remotingService.getJobConfig(job.id());
			//如果配置中心没有配置信息，则当前环境中的job不会加入到上下文当中，当为作废作业
			if(jobConfig == null){
				LOG.error("在配置中心没有找到job ID为[" + job.id() + "]的作业，同步作业程序不能正常初始化");
				continue f1;
			}
			job.setSchedule(jobConfig.getSchedule());
			job.setName(jobConfig.getName());
			//通过RPC获取到配置中心指定作业下的所有任务id
			List<String> taskIds = remotingService.getTaskIds(job.id());
			//装配任务
			for (String taskId : taskIds) {//循环配置中心所配置的任务
				//配置中心配置任务与当前环境中所配置的任务进行对比，如果当前环境没有对应的任务，则装配失败
				ETLTask assemblingTask = null;
				f3:for (ETLTask task : tasks.values()) {
					if(task.id().equals(taskId) && task.jobId().equals(job.id())){
						assemblingTask = task;
						break f3;
					}
				}
				if(assemblingTask == null){
					LOG.warn("配置中心配置为task ID[" + taskId + "]的任务在本地spring环境当中没有发现，同步作业程序初始化警告");
					continue f1;
				}
				//通过RPC获取配置中心指定任务下的所有源信息
				//获取输入源配置
				List<Map<String, Object>> inputs = remotingService.getInputResources(job.id(), assemblingTask.id());
				//获取输出源配置
				List<Map<String, Object>> outputs = remotingService.getOutputResources(job.id(), assemblingTask.id());
				List<ETLResource> inputResources = new ArrayList<>();
				List<ETLResource> outputResources = new ArrayList<>();
				//装配输入源和输出源
				boolean inputAssemableFlag = assembleResource(inputs, resourceFactories, inputResources);
				boolean outputAssemableFlag = assembleResource(outputs, resourceFactories, outputResources);
				if(!inputAssemableFlag){//装配失败的话导致整个作业的装配失败
					continue f1;
				}
				if(!outputAssemableFlag){//装配失败的话导致整个作业的装配失败
					continue f1;
				}
				assemblingTask.setInputETLResources(inputResources);
				assemblingTask.setoutputETLResources(outputResources);
				//装配step
				for (ETLStep step : steps.values()) {
					if(step.taskId().equals(assemblingTask.id())){
						assemblingTask.addScanCustomStep(step);
					}
				}
				//排序step
				assemblingTask.sortSteps();
				//检查是否是一个有效的step
				for (ETLStep step : assemblingTask.getAllSteps()) {
					try {
						step.check();
					} catch (Exception e) {//检查step失败的话会导致整个作业的装配失败
						e.printStackTrace();
						continue f1;
					}
				}
				//往当前job加入task
				job.addTask(assemblingTask);
				//向上下文当中添加作业
				context.addJob(job.id(), job);
			}
		}
		//运行上下文的定时任务
		context.schedule();
		
		//连接监控中心
		connectMonitorServer();
	}
	
	private void connectMonitorServer() {
		try {
			MonitorClient monitorClient = new MonitorClient(monitorServerIp, Integer.parseInt(monitorServerPort), context);
			monitorClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}

	/**
	 * 根据源配置装配源对象
	 * @param configParams 源配置
	 * @param resourceFactories 泛型工厂集合（spring环境当中的）
	 * @param resources 源对象集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean assembleResource(List<Map<String, Object>> configParams, Map<String, ETLResourceFactory> resourceFactories, List<ETLResource> resources){
		boolean flag = true;
		for (Map<String, Object> params : configParams) {
			//当前获取到的配置中必须指定type，如果没有指定type，则源对象创建失败，装配失败
			String type = params.get(ETLResourceConsist.TYPE).toString();
			if(StringUtils.isEmpty(type)){
				//没有找到指定类型的源工厂，不能初始化job
				LOG.warn("没有指定源信息配置的type属性，装配作业失败");
				flag = false;
			}
			ETLResourceFactory<ETLResource> factory = findFactory(type, resourceFactories);
			if(factory != null){
				ETLResource resource = factory.createResource(params);
				resources.add(resource);
			}else{
				LOG.warn("当前环境没有对应type[" + type + "]的源工厂，装配作业失败");
				flag = false;
			}
		}
		return flag;
	}
	
	
	/**
	 * @param type 根据数据源的type来查找当前上下文环境当中的源工厂(可能是db，可能是file也可能是其他工厂，可无限扩展)
	 * 指定的源工厂创建出来的就是指定的数据源(可能是db源也可能是文件源等等)
	 * @param resourceFactories
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ETLResourceFactory<ETLResource> findFactory(String type, Map<String, ETLResourceFactory> resourceFactories){
		for (ETLResourceFactory<ETLResource> factory : resourceFactories.values()) {
			if(factory.type().equals(type)){
				return factory;
			}
		}
		return null;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
