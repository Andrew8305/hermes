package org.apel.hermes.config.api;

import org.apel.hermes.config.api.domain.JobLogCollector;

/**
 * 配置中心业务调用接口
 * @author msk
 *
 */
public interface RemotingBusinessService {

	/**
	 * 保存日志
	 * @param jobLogCollector
	 */
	public void saveLog(JobLogCollector jobLogCollector);
}
