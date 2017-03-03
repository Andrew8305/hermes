package org.apel.hermes.monitor.api.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ETLReport implements Serializable {

	private static final long serialVersionUID = -3885576424555144892L;

	private List<JobReport> jobReports = new ArrayList<>();

	public List<JobReport> getJobReports() {
		return jobReports;
	}

	public void setJobReports(List<JobReport> jobReports) {
		this.jobReports = jobReports;
	}

}
