package org.apel.hermes.monitor.api;

import java.util.List;

import org.apel.hermes.monitor.api.report.HeartbeatReport;

/**
 * 监控中心RPC
 * @author lijian
 *
 */
public interface MonitorService {

	List<HeartbeatReport> getMonitorStatus(); 
	
}
