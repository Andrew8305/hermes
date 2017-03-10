package org.apel.hermes.config.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apel.hermes.config.web.exception.NoneMonitorProviderException;
import org.apel.hermes.monitor.api.MonitorService;
import org.apel.hermes.monitor.api.report.ETLReport;
import org.apel.hermes.monitor.api.report.HeartbeatReport;
import org.apel.hermes.monitor.api.report.JobReport;
import org.apel.hermes.monitor.api.report.MachineReport;
import org.apel.hermes.monitor.api.report.TaskReport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;


/**
 * 监控中心控制器
 * @author msk
 *
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController {
	private final static String INDEX_URL = "monitor/monitor_index";
	
	@Reference
	private MonitorService monitorService;
	
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(){
		return INDEX_URL;
	}
	
	@RequestMapping
	public @ResponseBody List<Map<String,Object>> list(){
		if(monitorService==null){
			throw new NoneMonitorProviderException("MonitorService not found.");
		}
		List<HeartbeatReport> heartbeatReports = monitorService.getMonitorStatus();
		List<Map<String,Object>> result = new ArrayList<>();
		//解析并统计需要的数据
		for(HeartbeatReport hr:heartbeatReports){
			ETLReport etlr=hr.getEtlReport();
			MachineReport mar=hr.getMachineReport();
			List<JobReport> jobreports = etlr.getJobReports();
			int jobNum=0,taskNum=0,stepNum=0;
			for(JobReport job:jobreports){
				jobNum+=1;
				//job.getJobId();
				List<TaskReport> taskReports = job.getTaskReports();
				for(TaskReport tr:taskReports){
					taskNum+=1;
					stepNum+=tr.getStepNum();
					//tr.getStepNum();
					//tr.getTaskId();
				}
			}
			Map<String,Object> result_map = new HashMap<>();
			result_map.put("ip", mar.getIp());
			result_map.put("id", hr.getId());
			result_map.put("jobNum", jobNum);
			result_map.put("taskNum", taskNum);
			result_map.put("stepNum", stepNum);
			result.add(result_map);
		}
		return result;
	}
	
	@RequestMapping(value="getOne",method=RequestMethod.GET)
	public @ResponseBody HeartbeatReport getOne(String id){
		if(monitorService==null){
			throw new NoneMonitorProviderException("MonitorService not found.");
		}
		HeartbeatReport heartbeatReport = null;
		if(StringUtils.isBlank(id)){
			return heartbeatReport;
		}
		List<HeartbeatReport> heartbeatReports = monitorService.getMonitorStatus();
		for(HeartbeatReport hr:heartbeatReports){
			if(id.equals(hr.getId())){
				heartbeatReport=hr;
				break;
			}
		}
		return heartbeatReport;
	}
	
	
}
