package org.apel.hermes.config.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务日志收集器
 * 
 * @author lijian
 *
 */
public class TaskLogCollector implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private Date startTime;

	private Date endTime;

	private String duration;

	private String resultDesc;

	private boolean result;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public List<StepLogCollector> getStepLogCollectors() {
		return stepLogCollectors;
	}

	public void setStepLogCollectors(List<StepLogCollector> stepLogCollectors) {
		this.stepLogCollectors = stepLogCollectors;
	}

	private TaskLogCollector() {
	}

	private List<StepLogCollector> stepLogCollectors = new ArrayList<>();

	public void addStepLogCollector(StepLogCollector stepLogCollector) {
		stepLogCollectors.add(stepLogCollector);
	}

	public void addTaskLogCollectors(List<StepLogCollector> stepLogCollectors) {
		this.stepLogCollectors.addAll(stepLogCollectors);
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public static TaskLogCollector create() {
		return new TaskLogCollector();
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
