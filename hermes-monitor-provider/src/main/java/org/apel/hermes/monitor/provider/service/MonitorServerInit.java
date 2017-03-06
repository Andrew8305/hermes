package org.apel.hermes.monitor.provider.service;

import org.apel.hermes.monitor.provider.server.MonitorServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 监控服务端初始化器
 * @author lijian
 *
 */
@Component
public class MonitorServerInit implements InitializingBean{

	@Value("${monitor.port}")
	private String port;
	
	@Autowired
	private ConnectionManager connectionManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(){
			@Override
			public void run() {
				MonitorServer monitorServer = new MonitorServer(Integer.parseInt(port), connectionManager);
				monitorServer.run();
			}
			
		}.start();
	}

	
	
}
