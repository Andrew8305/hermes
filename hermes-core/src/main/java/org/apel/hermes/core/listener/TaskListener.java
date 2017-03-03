package org.apel.hermes.core.listener;

/**
 * ETL任务监听器
 * @author lijian
 *
 */
public interface TaskListener {
	
	void onStart();
	
	void onEnd();
	
}
