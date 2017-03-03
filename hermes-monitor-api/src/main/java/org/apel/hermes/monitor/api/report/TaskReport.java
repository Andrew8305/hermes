package org.apel.hermes.monitor.api.report;

import java.io.Serializable;

public class TaskReport implements Serializable {

	private static final long serialVersionUID = 6497893766593603037L;

	private String taskId;

	private int stepNum;

	public int getStepNum() {
		return stepNum;
	}

	public void setStepNum(int stepNum) {
		this.stepNum = stepNum;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
