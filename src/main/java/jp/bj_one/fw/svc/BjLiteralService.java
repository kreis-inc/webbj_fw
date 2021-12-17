package jp.bj_one.fw.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.bj_one.fw.entity.BjLiteralEntity;
import jp.bj_one.fw.entity.BjLiteralItemEntity;
import jp.bj_one.fw.entity.BjLiteralParamEntity;
import jp.bj_one.fw.repo.BjLiteralRepository;

@Service
public class BjLiteralService {

  @Autowired
  BjLiteralRepository bjLiteralRepository;

  public List<BjLiteralEntity> findList(BjLiteralParamEntity condition) {
    return bjLiteralRepository.selectLiteralList(condition);
  }

  public List<BjLiteralEntity> findMasterList(BjLiteralParamEntity condition) {
    return bjLiteralRepository.selectMasterList(condition);
  }

  public List<BjLiteralItemEntity> findLiteralItems(BjLiteralItemEntity condition) {
    return bjLiteralRepository.selectLiteralItems(condition);
  }

  public List<BjLiteralEntity> findItemValues(BjLiteralItemEntity condition) {
    return bjLiteralRepository.selectItemValues(condition);
  }
}
