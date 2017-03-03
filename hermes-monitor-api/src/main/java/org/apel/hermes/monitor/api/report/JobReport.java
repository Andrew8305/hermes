package org.apel.hermes.monitor.api.report;

import java.io.Serializable;
import java.util.List;

public class JobReport implements Serializable {

	private static final long serialVersionUID = -3989727128512714833L;

	private String jobId;

	private List<TaskReport> taskReports;

	public List<TaskReport> getTaskReports() {
		return taskReports;
	}

	public void setTaskReports(List<TaskReport> taskReports) {
		this.taskReports = taskReports;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
