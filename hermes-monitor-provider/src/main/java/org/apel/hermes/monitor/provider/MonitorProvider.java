package org.apel.hermes.monitor.provider;

import java.util.ArrayList;
import java.util.List;

import org.apel.dubbo.starter.config.SpringService;
import org.apel.hermes.monitor.api.MonitorService;
import org.apel.hermes.monitor.api.report.HeartbeatReport;
import org.apel.hermes.monitor.provider.service.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service
@SpringService
public class MonitorProvider implements MonitorService{

	@Autowired
	private ConnectionManager connectionManager;
	
	@Override
	public List<HeartbeatReport> getMonitorStatus() {
		return new ArrayList<>(connectionManager.getAllChannels().values());
	}

}
