package jp.bj_one.fw.repo;

import jp.bj_one.fw.entity.MBjInfoEntity;

/**
 *
 *
 * @author
 */
public interface MBjInfoRepository extends CrudRepositoryInterface {

  /**
   * プライマリーキーで検索します。
   *
   * @param entity
   * @return
   */
  MBjInfoEntity selectPrimaryKey(MBjInfoEntity entity);

  /**
   * レコード登録します。
   *
   * @param entity
   * @return
   */
  int insert(MBjInfoEntity entity);

  /**
   * レコード更新します。
   *
   * @param entity
   * @return
   */
  int update(MBjInfoEntity entity);

  /**
   * レコード削除します。
   *
   * @param entity
   * @return
   */
  int delete(MBjInfoEntity entity);
}