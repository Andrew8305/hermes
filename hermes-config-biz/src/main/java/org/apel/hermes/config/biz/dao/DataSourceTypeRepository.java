package org.apel.hermes.config.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.DataSourceType;
import org.apel.hermes.config.biz.enums.TemplateEnum;
import org.springframework.data.jpa.repository.Query;

public interface DataSourceTypeRepository extends CommonRepository<DataSourceType, Integer>{
	
	List<DataSourceType> findAllByTemplateType(TemplateEnum type);

	@Query("SELECT max(seq) FROM DataSourceType ")
	Integer findMaxSeq();

}
