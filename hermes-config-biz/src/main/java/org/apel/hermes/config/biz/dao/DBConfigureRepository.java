package org.apel.hermes.config.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.DBConfigure;
import org.springframework.data.jpa.repository.Query;

public interface DBConfigureRepository extends CommonRepository<DBConfigure, String>{
	
	@Query
	List<DBConfigure> findAllByDbKey(String dbKey);

}
