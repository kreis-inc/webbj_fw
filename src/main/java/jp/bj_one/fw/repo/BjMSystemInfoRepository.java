package jp.bj_one.fw.repo;

import java.util.List;
import jp.bj_one.fw.repo.CrudRepositoryInterface;
import jp.bj_one.fw.entity.BjMSystemInfoEntity;

/**
 *
 *
 * @author
 */
public interface BjMSystemInfoRepository extends CrudRepositoryInterface {

  /**
   * プライマリーキーで検索します。
   *
   * @param entity
   * @return
   */
  BjMSystemInfoEntity selectPrimaryKey(BjMSystemInfoEntity entity);

  /**
   * レコード登録します。
   *
   * @param entity
   * @return
   */
  int insert(BjMSystemInfoEntity entity);

  /**
   * レコード更新します。
   *
   * @param entity
   * @return
   */
  int update(BjMSystemInfoEntity entity);

  /**
   * レコード削除します。
   *
   * @param entity
   * @return
   */
  int delete(BjMSystemInfoEntity entity);
}