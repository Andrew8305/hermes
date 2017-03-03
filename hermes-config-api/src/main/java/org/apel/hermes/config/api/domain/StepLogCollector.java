package org.apel.hermes.config.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * step日志收集器
 * 
 * @author lijian
 *
 */
public class StepLogCollector implements Serializable {

	private static final long serialVersionUID = -1122290787773685758L;

	private int order;

	private Date startTime;

	private Date endTime;

	private String duration;

	private String resultDesc;

	private boolean result;

	private StepLogCollector() {
	}

	private List<String> logs = new ArrayList<>();

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
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

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public void addLog(String log) {
		logs.add(log);
	}

	public void addLogs(List<String> logs) {
		this.logs.addAll(logs);
	}

	public static StepLogCollector create() {
		return new StepLogCollector();
	}

	public List<String> getLogs() {
		return logs;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
