package org.apel.hermes.core.listener;

/**
 *  ETL作业监听器
 * @author lijian
 *
 */
public interface JobListener {
	
	void onStart();
	
	void onEnd();
	
}
