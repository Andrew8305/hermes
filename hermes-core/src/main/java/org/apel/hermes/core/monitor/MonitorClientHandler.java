package org.apel.hermes.core.monitor;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apel.hermes.core.context.ETLContext;
import org.apel.hermes.core.job.ETLJob;
import org.apel.hermes.core.util.SigarUtil;
import org.apel.hermes.monitor.api.report.ETLReport;
import org.apel.hermes.monitor.api.report.HeartbeatReport;
import org.apel.hermes.monitor.api.report.JobReport;
import org.apel.hermes.monitor.api.report.MachineReport;
import org.apel.hermes.monitor.api.report.TaskReport;

public class MonitorClientHandler extends ChannelHandlerAdapter {

	private static Logger LOG = Logger.getLogger(MonitorClientHandler.class);
	
	private ETLContext etlContext;
	
	public MonitorClientHandler(ETLContext etlContext){
		this.etlContext = etlContext;
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.WRITER_IDLE)) {
				LOG.info("监控客户端发送心跳检测包");
				HeartbeatReport heartbeatReport = assembleReport();
				ctx.writeAndFlush(heartbeatReport);
			}
		}
	}

	/**
	 * 装配心跳包
	 */
	private HeartbeatReport assembleReport() {
		Map<String, ETLJob> allJobs = etlContext.getAllJobs();
		ETLReport etlReport = new ETLReport();
		List<JobReport> jobReports = new ArrayList<>();
		for (String jobId : allJobs.keySet()) {
			JobReport jobReport = new JobReport();
			jobReport.setJobId(jobId);
			ETLJob etlJob = allJobs.get(jobId);
			List<TaskReport> taskReports = new ArrayList<>();
			for (String taskId : etlJob.getAllTasks().keySet()) {
				TaskReport taskReport = new TaskReport();
				int stepNum = etlJob.getAllTasks().get(taskId).getAllSteps().size();
				taskReport.setStepNum(stepNum);
				taskReport.setTaskId(taskId);
				taskReports.add(taskReport);
			}
			jobReport.setTaskReports(taskReports);
			jobReports.add(jobReport);
		}
		etlReport.setJobReports(jobReports);
		HeartbeatReport heartbeatReport = new HeartbeatReport();
		MachineReport machineReport = new MachineReport();
		machineReport.setJvmStatus(SigarUtil.jvm());
		machineReport.setOsStatus(SigarUtil.os());
		machineReport.setCpuStatus(SigarUtil.cpu());
		machineReport.setMemoryStatus(SigarUtil.memory());
		machineReport.setIp(SigarUtil.ip());
		heartbeatReport.setMachineReport(machineReport);
		heartbeatReport.setEtlReport(etlReport);
		
		return heartbeatReport;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOG.error(cause.getMessage());
		cause.printStackTrace();
	}

}
