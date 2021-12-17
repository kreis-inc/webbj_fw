package jp.bj_one.fw.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.bj_one.fw.entity.BjMSystemInfoEntity;
import jp.bj_one.fw.repo.BjMSystemInfoRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class BjSystemInfoService {

	@Autowired
	BjMSystemInfoRepository repository;

	@Value("${app.name}")
	private String systemId;

	public BjMSystemInfoEntity getBjMSystemInfoEntity() {

		 BjMSystemInfoEntity entity = new BjMSystemInfoEntity();
		entity.setBjSystemId(this.systemId);
		entity = repository.selectPrimaryKey(entity);
		if(entity == null) {
			return new BjMSystemInfoEntity();
		}
		return entity;

	}

}
