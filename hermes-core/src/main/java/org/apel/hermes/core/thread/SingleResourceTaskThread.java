package org.apel.hermes.core.thread;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apel.hermes.config.api.domain.JobLogCollector;
import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.config.api.domain.TaskLogCollector;
import org.apel.hermes.core.listener.TaskListener;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.step.ETLStep;
import org.apel.hermes.core.step.db.SmartDBETLStep;
import org.apel.hermes.core.util.ETLListenerUtil;
import org.apel.hermes.core.util.LogDescUtil;
import org.apel.hermes.core.util.TimeComputer;

/**
 * task线程类，每一个输入源和输出源对应的task会成为一个线程在整个上下文环境中进行执行
 * @author lijian
 *
 */
public class SingleResourceTaskThread implements Runnable{
	
	private static Logger LOG = Logger.getLogger(SingleResourceTaskThread.class);
	
	private ETLResource inputETLResource;
	private ETLResource outputETLResource;
	private List<ETLStep> allOrderedSteps;
	private TaskListener taskListener;
	private JobLogCollector jobLogCollector;
	private String taskId;
	private Semaphore semaphore;
	private String runtimeVersionId;

	public SingleResourceTaskThread(String taskId, ETLResource inputETLResource, ETLResource outputETLResource, 
			List<ETLStep> allOrderedSteps, TaskListener taskListener, JobLogCollector jobLogCollector, 
			Semaphore semaphore, String runtimeVersionId) {
		this.taskId = taskId;
		this.taskListener = taskListener;
		this.inputETLResource = inputETLResource;
		this.outputETLResource = outputETLResource;
		this.allOrderedSteps = allOrderedSteps;
		this.jobLogCollector = jobLogCollector;
		this.semaphore = semaphore;
		this.runtimeVersionId = runtimeVersionId;
	}
	
	@Override
	public void run() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//创建任务日志收集器
		TaskLogCollector taskLogCollector = TaskLogCollector.create();
		jobLogCollector.addTaskLogCollector(taskLogCollector);
		taskLogCollector.setStartTime(new Date());
		taskLogCollector.setId(LogDescUtil.taskThreadId(this.taskId, inputETLResource.id(), inputETLResource.name(), outputETLResource.id(), outputETLResource.name()));
		Long startTime = System.currentTimeMillis();
		try {
			LOG.info(LogDescUtil.commonTaskDesc(this.taskId, inputETLResource.id(), inputETLResource.name(), outputETLResource.id(), outputETLResource.name(), "开始运行"));
			//监听器回调点
			ETLListenerUtil.taskOnStart(taskListener);
			for (ETLStep etlStep : allOrderedSteps) {
				//创建step日志收集器
				StepLogCollector stepLogCollector = StepLogCollector.create();
				taskLogCollector.addStepLogCollector(stepLogCollector);
				stepLogCollector.setOrder(etlStep.order());
				stepLogCollector.setStartTime(new Date());
				Long stepStartTime = System.currentTimeMillis();
				String sql = null;
				if (etlStep instanceof SmartDBETLStep){
					sql = ((SmartDBETLStep)etlStep).getMainSql();
				}
				sql = sql == null ? "" : sql;
				try {
					//控制打印
					String stepStartDesc = LogDescUtil.commonStepDesc(etlStep.order(), inputETLResource.id(), 
							inputETLResource.name(), outputETLResource.id(), outputETLResource.name(), sql, "开始执行");
					LOG.info(stepStartDesc);
					
					//具体的step执行
					etlStep.doStep(inputETLResource, outputETLResource, stepLogCollector, runtimeVersionId);
					
				} catch (Exception e) {
					
					//控制打印
					e.printStackTrace();
					String stepErrorDesc = LogDescUtil.commonStepDesc(etlStep.order(), inputETLResource.id(), 
							inputETLResource.name(), outputETLResource.id(), outputETLResource.name(), sql, "出现异常");
					LOG.error(stepErrorDesc + "：" + e.getMessage());
					
					//写入配置中心
					String errorResultMsg = "ETL步骤，步骤数为[" + etlStep.order() + "]运行失败：" + e.getMessage();
					stepLogCollector.setResultDesc(errorResultMsg);
					stepLogCollector.setResult(false);
				}finally{
					
					//控制打印
					Long stepEndTime = System.currentTimeMillis();
					String stepEndDesc = LogDescUtil.commonStepDesc(etlStep.order(), inputETLResource.id(), 
							inputETLResource.name(), outputETLResource.id(), outputETLResource.name(), sql, "结束执行，执行时间为：");
					LOG.info(TimeComputer.computeDuration(stepEndDesc, stepStartTime, stepEndTime));
					
					//写入配置中心
					stepLogCollector.setEndTime(new Date());
					stepLogCollector.setDuration(TimeComputer.computeDuration("", stepStartTime, stepEndTime));
					if(StringUtils.isEmpty(stepLogCollector.getResultDesc())){
						stepLogCollector.setResultDesc("成功结束");
						stepLogCollector.setResult(true);
					}
				}
			}
			//监听器回调点
			ETLListenerUtil.taskOnEnd(taskListener);
		} catch (Exception e) {
			
			//控制打印
			e.printStackTrace();
			String taskErrorDesc = LogDescUtil.commonTaskDesc(this.taskId, inputETLResource.id(), inputETLResource.name(), outputETLResource.id(), outputETLResource.name(), "出现异常");
			taskErrorDesc = taskErrorDesc + ":" + e.getMessage();
			LOG.error(taskErrorDesc);
			
			//写入配置中心
			String taskResultDesc = "ETL任务，任务id为[" + this.taskId + "]出现异常：" + e.getMessage();
			taskLogCollector.setResultDesc(taskResultDesc);
			taskLogCollector.setResult(false);
		}finally{
			
			//控制打印
			Long endTime = System.currentTimeMillis();
			String taskEndDesc = LogDescUtil.commonTaskDesc(this.taskId, inputETLResource.id(), inputETLResource.name(), outputETLResource.id(), outputETLResource.name(), "结束运行，执行时间为");
			LOG.info(TimeComputer.computeDuration(taskEndDesc, startTime, endTime));
			
			//写入配置中心
			taskLogCollector.setEndTime(new Date());
			taskLogCollector.setDuration(TimeComputer.computeDuration("", startTime, endTime));
			//判断所有步骤是否都执行成功
			List<StepLogCollector> stepLogCollectors = taskLogCollector.getStepLogCollectors();
			boolean allStepSuccess = true;
			for (StepLogCollector slc : stepLogCollectors) {
				if(!slc.isResult()){
					allStepSuccess = false;
				}
			}
			if(StringUtils.isEmpty(taskLogCollector.getResultDesc()) && allStepSuccess){//所有步骤都成功，并且步骤循环逻辑没有异常
				taskLogCollector.setResultDesc("成功结束");
				taskLogCollector.setResult(true);
			}else{
				taskLogCollector.setResultDesc("运行失败");
				taskLogCollector.setResult(false);
			}
			semaphore.release();
		}
	}

}
