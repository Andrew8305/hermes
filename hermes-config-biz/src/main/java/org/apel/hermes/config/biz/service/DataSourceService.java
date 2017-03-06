package org.apel.hermes.config.biz.service;

import java.util.List;
import java.util.Map;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.DataSource;

public interface DataSourceService extends BizCommonService<DataSource,Integer>{

	/**
	 * 查询输入源
	 * @return
	 */
	public Map<String,List<DataSource>> getInputDataSource(String jobBizId,String taskBizId);

	/**
	 * 查询输出源
	 * @return
	 */
	public Map<String,List<DataSource>> getOutputDataSource(String jobBizId,String taskBizId);
	


	public void saveOrUpdate(List<DataSource> dataSource)throws Exception;

	public Map<String, String> checkRepeat(String businessCode)throws Exception;

	public List<DataSource> findAllByBusinessCode(Integer id)throws Exception;

	public void batchDelete(Integer[] ids)throws Exception;

	public List<DataSource> getAllDataSourceByGroup()throws Exception;

	
}
