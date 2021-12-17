package jp.bj_one.fw.svc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.bj_one.fw.common.MessageData;
import jp.bj_one.fw.common.RepositoryUtil;
import jp.bj_one.fw.dto.ResponseDto;
import jp.bj_one.fw.entity.EntityInterface;
import jp.bj_one.fw.exception.ApplicationException;
import jp.bj_one.fw.exception.BjSystemException;
import jp.bj_one.fw.form.BjForm;
import jp.bj_one.fw.repo.CrudRepositoryInterface;
import jp.bj_one.fw.repo.RepositoryInterface;

@Service
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractService {

  @Autowired
  protected Mapper mapper;

  @Autowired
  protected MessageSource messageSource;

  /**
   * 入力チェック用メソッド
   * 継承先で実装.
   * @param dto
   */
  protected abstract void validateItem(ResponseDto dto);

  protected abstract void validateCorrelation(ResponseDto dto);

  /**
   * 検索を行う際のリポジトリー。
   * 継承先で実装.
   *
   * @return 検索用リポジトリー
   */
  protected abstract RepositoryInterface getSearchRepository();

  /**
   * 検索結果をFormへ格納
   * 継承先で実装
   * @param bjForm
   * @param results
   */
  protected abstract void setSearchResult(BjForm bjForm, List<EntityInterface> results);

  /**
   * 登録・更新・削除を行う際のリポジトリー。
   * 継承先で実装.
   * @return 登録・更新・削除用リポジトリー
   */
  protected abstract CrudRepositoryInterface getCrudRepository();

  /**
   * 登録・更新・削除を行うテーブルのエンティティクラス。
   *
   * @return
   */
  protected abstract Class<? extends EntityInterface> getCrudEntityClass();

  public ResponseDto execute(ResponseDto dto) {
    if (dto.getForm().getTriggerCtrlId() == null)
      return dto;

    switch (dto.getForm().getTriggerCtrlId()) {
    case "display":
      executeDisplay(dto);
      break;
    case "search":
      executeSearch(dto);
      break;
    case "register":
      executeRegister(dto);
      break;
    case "modify":
      executeModify(dto);
      break;
    case "unregister":
      executeUnregister(dto);
      break;

    case "back":
      // 何もしない
      break;
    default:
    }
    return dto;
  }

  /**
   * 画面表示処理
   *
   * @param dto
   */
  protected void executeDisplay(ResponseDto dto) {
    BjForm bjForm = dto.getForm();
    
    if (!isCrudRepository()) {
      return;
    }

    // データ取得
    try {
      EntityInterface crudEntity = this.getCrudEntityClass().newInstance();
      mapper.map(bjForm, crudEntity);
      crudEntity = this.getCrudRepository().selectPrimaryKey(crudEntity);

      if (crudEntity == null) {
        bjForm.getMessageData().add("LE00009", "");
        bjForm.getMessageData().getMessageStr(this.messageSource, Locale.JAPAN);
        throw new ApplicationException(bjForm.getMessageData());
      }

      //排他チェック
      //楽観排他制御及びシグネチャーのSET
      if (bjForm.getRevision() != null && bjForm.getLockFlg() != null) {
        if (!"".equals((bjForm.getRevision()).toString()) && "1".equals(bjForm.getLockFlg())) {
          BjForm lockBjForm = new BjForm();
          mapper.map(crudEntity, lockBjForm);

          if (lockBjForm.getRevision() != null && !"".equals((lockBjForm.getRevision()).toString())) {

            int result = bjForm.getRevision().compareTo(lockBjForm.getRevision());
            if (result != 0) {
              //排他エラー
              bjForm.getMessageData().add("LE00012", "");
              bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
              throw new ApplicationException(bjForm.getMessageData());
            }
          }
        }
      }

      // 取得結果をFormに詰め替え
      mapper.map(crudEntity, bjForm);
      dto.setForm(bjForm);

    } catch (IllegalAccessException | InstantiationException e) {
      throw new BjSystemException(e);
    }

  }

  /**
   * データ検索処理。
   *
   * @param dto
   * @return
   */
  protected void executeSearch(ResponseDto dto) {
    BjForm bjForm = dto.getForm();

    this.validate(bjForm, dto);

    // 件数を取得
    int total = this.getSearchRepository().count(bjForm);

    if (total == 0) {
      // データ取得できない場合、エラーを返す
      bjForm.getMessageData().add("LI00004", "");
      bjForm.getMessageData().getMessageStr(this.messageSource, Locale.JAPAN);
    } else {

      // 取得結果を設定
      bjForm.setTotalPage(RepositoryUtil.calculateTotalPage(bjForm.getPerPage(), total));
      bjForm.setTotalRecord(total);
      
      // データを検索
      List<EntityInterface> records = this.getSearchRepository().search(bjForm);
      
      // データが見つからない場合は 1 ページに移動して再検索
      // （ページ移動した際に、削除等でデータ件数が減った場合に発生する）
      if( records.size() == 0 ) {
        bjForm.setNowPage(1);
        records = this.getSearchRepository().search(bjForm);
      }
      
      this.setSearchResult(bjForm, records);
    }
  }

  /**
   * データ登録処理。
   *
   * @param crudList
   */
  protected void executeRegister(ResponseDto dto) {
    BjForm bjForm = dto.getForm();
    
    this.validate(bjForm, dto);

    if (!isCrudRepository()) {
      return;
    }

    try {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      bjForm.setBjCreateUserId(bjForm.getUserInfo().getUser().getUserId());
      bjForm.setBjCreateDate(timestamp);
      bjForm.setBjUpdateId(null);
      bjForm.setBjUpdateDate(null);
      bjForm.setBjDeleteFlg("0");
      bjForm.setRevision(new BigDecimal(1));

      EntityInterface crudEntity = this.getCrudEntityClass().newInstance();
      mapper.map(bjForm, crudEntity);
      this.preRegister(crudEntity, dto);
      this.getCrudRepository().insert(crudEntity);
      bjForm.getMessageData().add("LI00001", "");
      bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
      dto.setForm(bjForm);
    } catch (IllegalAccessException | InstantiationException e) {
      throw new BjSystemException(e);

    } catch (Exception e) {
      // Duplicate entry
      if (e instanceof DuplicateKeyException) {
        bjForm.getMessageData().add("LE00010", "");
        bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
        throw new ApplicationException(bjForm.getMessageData());
      }
      e.printStackTrace();
      bjForm.getMessageData().add("LE00011", "");
      bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
      throw new ApplicationException(bjForm.getMessageData());
    }
  }

  /**
   * 登録処理SQL実行直前処理.
   * @param entity
   * @param dto
   */
  protected abstract void preRegister(EntityInterface entity, ResponseDto dto);

  /**
   * データ更新処理。
   *
   * @param dto
   */
  protected void executeModify(ResponseDto dto) {
    BjForm bjForm = dto.getForm();
    
    this.validate(bjForm, dto);

    if (!isCrudRepository()) {
      return;
    }

    try {
      EntityInterface crudEntity = this.getCrudEntityClass().newInstance();
      mapper.map(bjForm, crudEntity);

      //楽観排他制御及びシグネチャーのSET
      if (bjForm.getRevision() != null && bjForm.getLockFlg() != null) {
        if ("1".equals(bjForm.getLockFlg()) && bjForm.getRevision() != null
            && !"".equals((bjForm.getRevision()).toString())) {
          //排他制御実施
          crudEntity = this.getCrudRepository().selectPrimaryKey(crudEntity);
          BjForm lockBjForm = new BjForm();
          mapper.map(crudEntity, lockBjForm);
          if (!"".equals((lockBjForm.getRevision()).toString()) || lockBjForm.getRevision() != null) {
            int result = bjForm.getRevision().compareTo(lockBjForm.getRevision());
            if (result != 0) {
              //排他エラー
              bjForm.getMessageData().add("LE00012", "");
              bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
              throw new ApplicationException(bjForm.getMessageData());
            }
          }
          Timestamp timestamp = new Timestamp(System.currentTimeMillis());
          //シグネチャーのSET
          bjForm.setBjCreateUserId(lockBjForm.getBjCreateUserId());
          bjForm.setBjCreateDate(lockBjForm.getBjCreateDate());
          bjForm.setBjUpdateId(bjForm.getUserInfo().getUser().getUserId());
          bjForm.setBjUpdateDate(timestamp);
          bjForm.setBjDeleteFlg("0");
          //revision インクリメント
          if( bjForm.getRevision() == null) {
        	  bjForm.setRevision(new BigDecimal(1));
          } else {
        	  bjForm.getRevision().add(new BigDecimal(1));
          }
          mapper.map(bjForm, crudEntity);
        }
      }
      if (this.getCrudRepository().update(crudEntity) == 1) {
        bjForm.getMessageData().add("LI00002", "");
        bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
      } else {
        bjForm.getMessageData().add("LE00013", "");
        bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
        throw new ApplicationException(bjForm.getMessageData());
      }
    } catch (IllegalAccessException | InstantiationException e) {
      throw new BjSystemException(e);
    }
  }

  /**
   * データ削除処理。
   *
   * @param crudList
   */
  protected void executeUnregister(ResponseDto dto) {
    BjForm bjForm = dto.getForm();

    if (!isCrudRepository()) {
      return;
    }

    try {
      EntityInterface crudEntity = this.getCrudEntityClass().newInstance();
      mapper.map(bjForm, crudEntity);
      int i = this.getCrudRepository().delete(crudEntity);
      if (i == 1) {
        bjForm.getMessageData().add("LI00003", "");
        bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
      } else {
        bjForm.getMessageData().add("LE00014", "");
        bjForm.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
        throw new ApplicationException(bjForm.getMessageData());
      }
    } catch (IllegalAccessException | InstantiationException e) {
      throw new BjSystemException(e);
    }
  }

  /**
   * 入力チェック処理。
   *
   * @param dto
   */
  protected void validate(BjForm bjForm, ResponseDto dto) {

    this.validateItem(dto);

    isError(bjForm.getMessageData());

    this.validateCorrelation(dto);

    isError(bjForm.getMessageData());

  }

  /**
   * エラー判定処理。
   *
   * @param messageData メッセージオブジェクト
   */
  public void isError(MessageData messageData) {
    if (messageData == null)
      return;
    if (messageData.isError()) {
      messageData.getMessageStr(messageSource, Locale.JAPAN);
      throw new ApplicationException(messageData);
    }
  }

  /**
   * 更新用リポジトリがテーブルリポジトリか判定。
   *
   * @return
   */
  private boolean isCrudRepository() {
    if (this.getCrudRepository() instanceof CrudRepositoryInterface) {
      return true;
    }
    return false;
  }
}
