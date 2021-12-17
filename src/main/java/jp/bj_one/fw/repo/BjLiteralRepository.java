package jp.bj_one.fw.repo;

import java.util.List;

import jp.bj_one.fw.entity.BjLiteralEntity;
import jp.bj_one.fw.entity.BjLiteralItemEntity;
import jp.bj_one.fw.entity.BjLiteralParamEntity;

public interface BjLiteralRepository {

  List<BjLiteralEntity> selectLiteralList(BjLiteralParamEntity entity);

  List<BjLiteralEntity> selectMasterList(BjLiteralParamEntity entity);

  List<BjLiteralItemEntity> selectLiteralItems(BjLiteralItemEntity entity);

  List<BjLiteralEntity> selectItemValues(BjLiteralItemEntity entity);
}
