package org.apel.hermes.config.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hermes.config.biz.domain.DataSource;
import org.springframework.data.jpa.repository.Query;

public interface DataSourceRepository extends CommonRepository<DataSource, Integer>{

	List<DataSource> findAllByBusinessCode(String businessCode)throws Exception;

	@Query("SELECT MAX(o.seq) FROM DataSource o ")
	Integer findMaxSeq()throws Exception;

	void deleteAllByBusinessCode(String businessEntity);

	@Query("select ds from DataSource ds where 1=1 and businessCode in(?1)")
	public List<DataSource> findByBusinessCodes(List<String> inputs);
	
	@Query("SELECT o FROM DataSource o group by  o.businessCode,o.businessName ")
	List<DataSource> getAllDataSourceByGroup();

}
