package org.apel.hermes.config.biz.service;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hermes.config.biz.domain.DBConfigure;

public interface DBConfigureService extends BizCommonService<DBConfigure,String>{

	String modify(DBConfigure dBConfigure);
	
	
	boolean findByDbKeyExists(DBConfigure dBConfigure);

}
