package jp.bj_one.fw.repo;

import java.util.List;

import jp.bj_one.fw.entity.MLoginEntity;

/**
 *
 *
 * @author
 */
public interface MLoginRepository extends CrudRepositoryInterface {

  /**
   * プライマリーキーで検索します。
   *
   * @param entity
   * @return
   */
  MLoginEntity selectPrimaryKey(MLoginEntity entity);

  /**
   * レコード登録します。
   *
   * @param entity
   * @return
   */
  int insert(MLoginEntity entity);

  /**
   * レコード更新します。
   *
   * @param entity
   * @return
   */
  int update(MLoginEntity entity);

  /**
   * レコード削除します。
   *
   * @param entity
   * @return
   */
  int delete(MLoginEntity entity);

  /**
   * 一覧件数を取得します。
   *
   * @param entity
   * @return
   */
  int count(MLoginEntity entity);

  
  /**
   * 一覧データを取得します。
   *
   * @param entity
   * @return
   */
  List<MLoginEntity> search(MLoginEntity entity);

  /**
   * 認証情報取得用。
   *
   * @param entity
   * @return
   */
  MLoginEntity findOne(MLoginEntity entity);
}