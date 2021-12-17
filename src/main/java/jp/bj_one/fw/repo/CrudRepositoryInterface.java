package jp.bj_one.fw.repo;

import jp.bj_one.fw.entity.EntityInterface;

/**
 * データ操作用インターフェース.
 *
 * @author kaoru-shimizu
 *
 */
public interface CrudRepositoryInterface extends RepositoryInterface {

  /**
   * プライマリーキーでの検索処理。
   *
   * @param entity
   * @return
   */
  EntityInterface selectPrimaryKey(EntityInterface entity);

  /**
   * レコード登録処理。
   *
   * @param entity
   * @return
   */
  int insert(EntityInterface entity);

  /**
   * レコード更新処理。
   *
   * @param entity
   * @return
   */
  int update(EntityInterface entity);

  /**
   * レコード削除処理。
   *
   * @param entity
   * @return
   */
  int delete(EntityInterface entity);
}
