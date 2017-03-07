package org.apel.hermes.core.thread;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apel.hermes.config.api.domain.JobLogCollector;
import org.apel.hermes.config.api.domain.StepLogCollector;
import org.apel.hermes.config.api.domain.TaskLogCollector;
import org.apel.hermes.core.listener.TaskListener;
import org.apel.hermes.core.resource.ETLResource;
import org.apel.hermes.core.step.ETLStep;
import org.apel.hermes.core.util.ETLListenerUtil;
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

	public SingleResourceTaskThread(String taskId, ETLResource inputETLResource, ETLResource outputETLResource, 
			List<ETLStep> allOrderedSteps, TaskListener taskListener, JobLogCollector jobLogCollector) {
		this.taskId = taskId;
		this.taskListener = taskListener;
		this.inputETLResource = inputETLResource;
		this.outputETLResource = outputETLResource;
		this.allOrderedSteps = allOrderedSteps;
		this.jobLogCollector = jobLogCollector;
	}
	
	@Override
	public void run() {
		//创建任务日志收集器
		TaskLogCollector taskLogCollector = TaskLogCollector.create();
		jobLogCollector.addTaskLogCollector(taskLogCollector);
		taskLogCollector.setStartTime(new Date());
		taskLogCollector.setId(this.taskId);
		Long startTime = System.currentTimeMillis();
		try {
			LOG.info("ETL任务，id为[" + taskId + "]开始运行");
			//监听器回调点
			ETLListenerUtil.taskOnStart(taskListener);
			for (ETLStep etlStep : allOrderedSteps) {
				//创建step日志收集器
				StepLogCollector stepLogCollector = StepLogCollector.create();
				taskLogCollector.addStepLogCollector(stepLogCollector);
				stepLogCollector.setOrder(etlStep.order());
				stepLogCollector.setStartTime(new Date());
				Long stepStartTime = System.currentTimeMillis();
				try {
					etlStep.doStep(inputETLResource, outputETLResource, stepLogCollector);
				} catch (Exception e) {
					e.printStackTrace();
					String msg = "ETL步骤，步骤数为[" + etlStep.order() + "]，数据源标识[" + inputETLResource.id() + "]运行失败：" + e.getMessage();
					LOG.error(msg);
					stepLogCollector.setResultDesc(msg);
					stepLogCollector.setResult(false);
				}finally{
					Long stepEndTime = System.currentTimeMillis();
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
			e.printStackTrace();
			String msg = "ETL任务，id为[" + taskId + "]运行失败：" + e.getMessage();
			LOG.error(msg);
			taskLogCollector.setResultDesc(msg);
			taskLogCollector.setResult(false);
		}finally{
			Long endTime = System.currentTimeMillis();
			LOG.info(TimeComputer.computeDuration("ETL任务，id为[" + taskId + "]结束运行，执行时间", startTime, endTime));
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
		}
	}

}
