package org.apel.hermes.config.biz.service.impl;

import java.util.List;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hermes.config.biz.dao.DBConfigureRepository;
import org.apel.hermes.config.biz.domain.DBConfigure;
import org.apel.hermes.config.biz.service.DBConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DBConfigureServiceImpl extends AbstractBizCommonService<DBConfigure, String> implements DBConfigureService{
	
	@Autowired
	private DBConfigureRepository dBConfigureRepository;
	
	@Override
	public String modify(DBConfigure dBConfigure) {
		if(findByDbKeyExists(dBConfigure)){
			return "";
		};
		super.update(dBConfigure);
		return dBConfigure.getId();
	}

	@Override
	public boolean findByDbKeyExists(DBConfigure dBConfigure) {
		List<DBConfigure> dbconfigureEntityList =  dBConfigureRepository.findAllByDbKey(dBConfigure.getDbKey());
		if(dbconfigureEntityList!=null && dbconfigureEntityList.size()>0){
			return true;
		}
		return false;
	}
	
	@Override
	public String save(DBConfigure dBConfigure){
		if(findByDbKeyExists(dBConfigure)){
			return "";
		};
		
		super.save(dBConfigure);
		return dBConfigure.getId();
	}

	

}
