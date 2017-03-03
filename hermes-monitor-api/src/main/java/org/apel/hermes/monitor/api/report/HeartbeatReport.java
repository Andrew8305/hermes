package org.apel.hermes.monitor.api.report;

import java.io.Serializable;

public class HeartbeatReport implements Serializable {

	private static final long serialVersionUID = 2937157518262838890L;

	private String id;

	private ETLReport etlReport;

	private MachineReport machineReport;

	public ETLReport getEtlReport() {
		return etlReport;
	}

	public void setEtlReport(ETLReport etlReport) {
		this.etlReport = etlReport;
	}

	public MachineReport getMachineReport() {
		return machineReport;
	}

	public void setMachineReport(MachineReport machineReport) {
		this.machineReport = machineReport;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
