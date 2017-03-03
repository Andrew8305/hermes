package org.apel.hermes.core.util;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * 线程静态工具
 * @author lijian
 *
 */
public class ThreadUtil {
	
	private static Logger LOG = Logger.getLogger(ThreadUtil.class);
	
	/**
	 * 线程集合的join操作
	 */
	public static void join(List<Thread> stepThreads){
		for (Thread thread : stepThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOG.error("线程join错误:" + e.getMessage());
			}
		}
	}
	
}
