package org.apel.hermes.monitor.api.report;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CpuStatus implements Serializable{
	private static final long serialVersionUID = 806117072038329273L;
	
	List<String> cpuInfo = new ArrayList<>();

	public List<String> getCpuInfo() {
		return cpuInfo;
	}

	public void setCpuInfo(List<String> cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

}
