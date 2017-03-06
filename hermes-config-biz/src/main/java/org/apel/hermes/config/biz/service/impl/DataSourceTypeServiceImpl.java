package org.apel.hermes.config.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.gaia.util.BeanUtils;
import org.apel.hermes.config.biz.dao.DataSourceTypeRepository;
import org.apel.hermes.config.biz.domain.DataSourceType;
import org.apel.hermes.config.biz.enums.TemplateEnum;
import org.apel.hermes.config.biz.service.DataSourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataSourceTypeServiceImpl extends AbstractBizCommonService<DataSourceType, Integer> implements DataSourceTypeService{

	@Autowired
	private DataSourceTypeRepository dataSourceTypeRepository;
	
	@Override
	protected CommonRepository<DataSourceType, Integer> getRepository() {
		return dataSourceTypeRepository;
	}

	@Override
	protected String getPageQl() {
		return "select t from DataSourceType t where 1=1";
	}

	@Override
	public void saveOrUpdate(List<DataSourceType> datasource) throws Exception {
		DataSourceType dataSource = datasource.get(0);
		if(dataSource.getId()!=null && !"".equals(dataSource.getId())){
			DataSourceType result = dataSourceTypeRepository.findOne(datasource.get(0).getId());
			for(DataSourceType ds:datasource){
				if(null==ds.getId() || "".equals(ds.getId())){
					ds.setSeq(result.getSeq());
					dataSourceTypeRepository.store(ds);
				}else{
					DataSourceType persistentEntity = dataSourceTypeRepository.findOne(ds.getId());
					BeanUtils.copyNotNullProperties(ds, persistentEntity);
					this.update(persistentEntity);
				}
			}
		}else{
			//查询最大seq
			Integer max = dataSourceTypeRepository.findMaxSeq()==null?1:dataSourceTypeRepository.findMaxSeq()+1;
			for(DataSourceType dataSourceType:datasource){
				dataSourceType.setSeq(max);
				dataSourceTypeRepository.store(dataSourceType);
			}
		}
		
		
		
	}

	@Override
	public List<DataSourceType> findAllByType(Integer id) throws Exception {
		DataSourceType datasourceType = this.findById(id);
		List<DataSourceType> result = dataSourceTypeRepository.findAllByTemplateType(datasourceType.getTemplateType());
		return result;
	}

	@Override
	public void deleteByTemplateType(Integer[] ids) throws Exception {
		
		List<TemplateEnum>  types = new ArrayList<TemplateEnum>();
		for(Integer id:ids){
			DataSourceType dataSource = this.findById(id);
			types.add(dataSource.getTemplateType());
		}
		for(TemplateEnum template: types){
			List<DataSourceType> result = dataSourceTypeRepository.findAllByTemplateType(template);
			for(DataSourceType dataSourceEntity:result){
				this.deleteById(dataSourceEntity.getId());
			}

		}
		
	}

	@Override
	public List<DataSourceType> getTemplate(String code) throws Exception {
		TemplateEnum templateEnum = TemplateEnum.getTemplateEnumByCode(code);
		List<DataSourceType> result = dataSourceTypeRepository.findAllByTemplateType(templateEnum);
		return result;
	}

}
