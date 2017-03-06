package org.apel.hermes.monitor.provider.service;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

import org.apel.hermes.monitor.api.report.HeartbeatReport;
import org.springframework.stereotype.Component;

@Component
public class ConnectionManager {

	private Map<Channel, HeartbeatReport> channels = new HashMap<>();
	
	public void addChannel(Channel channel, HeartbeatReport heartbeatReport){
		this.channels.put(channel, heartbeatReport);
	}
	
	public Map<Channel, HeartbeatReport> getAllChannels(){
		return this.channels;
	}
	
	public void remove(Channel channel){
		this.channels.remove(channel);
	}
}
