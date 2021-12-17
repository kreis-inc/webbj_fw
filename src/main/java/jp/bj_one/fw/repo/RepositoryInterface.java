package jp.bj_one.fw.repo;

import java.util.List;

import jp.bj_one.fw.entity.EntityInterface;
import jp.bj_one.fw.form.BjForm;

public interface RepositoryInterface {

  /**
   * 件数を取得。
   *
   * @param condition
   * @return
   */
  int count(BjForm form);

  /**
   * データを検索。
   *
   * @param condition
   * @return
   */
  List<EntityInterface> search(BjForm form);

  EntityInterface findOne(EntityInterface entity);

}
