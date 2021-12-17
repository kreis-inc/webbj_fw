package jp.bj_one.fw.repo;

import java.util.List;
import jp.bj_one.fw.repo.CrudRepositoryInterface;
import jp.bj_one.fw.entity.BjLogOperationEntity;

/**
 *
 *
 * @author
 */
public interface BjLogOperationRepository extends CrudRepositoryInterface {

  /**
   * プライマリーキーで検索します。
   *
   * @param entity
   * @return
   */
  BjLogOperationEntity selectPrimaryKey(BjLogOperationEntity entity);

  /**
   * レコード登録します。
   *
   * @param entity
   * @return
   */
  int insert(BjLogOperationEntity entity);

  /**
   * レコード更新します。
   *
   * @param entity
   * @return
   */
  int update(BjLogOperationEntity entity);

  /**
   * レコード削除します。
   *
   * @param entity
   * @return
   */
  int delete(BjLogOperationEntity entity);
}