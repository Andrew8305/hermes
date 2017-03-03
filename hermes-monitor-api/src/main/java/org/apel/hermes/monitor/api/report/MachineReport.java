package org.apel.hermes.monitor.api.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MachineReport implements Serializable {

	private static final long serialVersionUID = 4603134759797655968L;

	private String ip;

	private List<String> jvmStatus = new ArrayList<>();

	private List<String> osStatus = new ArrayList<>();

	private List<String> memoryStatus = new ArrayList<>();

	private List<CpuStatus> cpuStatus = new ArrayList<>();

	public List<String> getJvmStatus() {
		return jvmStatus;
	}

	public void setJvmStatus(List<String> jvmStatus) {
		this.jvmStatus = jvmStatus;
	}

	public List<String> getOsStatus() {
		return osStatus;
	}

	public void setOsStatus(List<String> osStatus) {
		this.osStatus = osStatus;
	}

	public List<String> getMemoryStatus() {
		return memoryStatus;
	}

	public void setMemoryStatus(List<String> memoryStatus) {
		this.memoryStatus = memoryStatus;
	}

	public List<CpuStatus> getCpuStatus() {
		return cpuStatus;
	}

	public void setCpuStatus(List<CpuStatus> cpuStatus) {
		this.cpuStatus = cpuStatus;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
