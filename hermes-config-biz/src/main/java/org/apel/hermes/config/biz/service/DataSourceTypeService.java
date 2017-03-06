package org.apel.hermes.config.biz.service;

import java.util.List;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.DataSourceType;

public interface DataSourceTypeService extends BizCommonService<DataSourceType,Integer>{

	void saveOrUpdate(List<DataSourceType> datasource)throws Exception;

	List<DataSourceType> findAllByType(Integer id)throws Exception;

	void deleteByTemplateType(Integer[] ids)throws Exception;

	List<DataSourceType> getTemplate(String code)throws Exception;

}
