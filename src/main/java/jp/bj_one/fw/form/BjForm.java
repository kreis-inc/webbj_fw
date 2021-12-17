package jp.bj_one.fw.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jp.bj_one.fw.common.BjUserInfo;
import jp.bj_one.fw.common.MessageData;
import jp.bj_one.fw.common.literal.BjDisplayMode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString(exclude = {"messageData"})
public class BjForm implements Serializable{

	private String dbtype;

	/** 排他制御用フラグ application.propatiesに定義 */
	private String lockFlg;

	//2019/01/18追加
	/* ------ BJ用DBsignature -------- */
	 /** bj_create_date 新規作成日*/
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss", timezone = "Asia/Tokyo")
	  private Timestamp bjCreateDate;
	  /** bj_create_user_id。新規作成者 */
	  private String bjCreateUserId;
	  /** bj_update_date。更新日 */
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss", timezone = "Asia/Tokyo")
	  private Timestamp bjUpdateDate;
	  /** bj_update_id。更新日時 */
	  private String bjUpdateId;
	  /** bj_delete_flg。 削除フラグ*/
	  private String bjDeleteFlg;
	  /** revision。楽観排他用Revision */
	  private BigDecimal revision;



	private MessageData messageData = new MessageData();
	private BjUserInfo userInfo;
	private String event;

	/** 表示モード。 */
	private BjDisplayMode bjDisplayMode;


  /* ------ 画面ID、名称変更用の項目 -------- */
	/** 変更画面ID。 */
  private String changeScreenId;
  /** 変更画面名称。 */
  private String changeScreenName;

	/* ------ 画面イベント情報用の項目 -------- */
  /** イベント発火元画面ID。 */
	private String triggerScreenId;
	/** イベント発火元画面名称。 */
	private String triggerScreenName;
	/** イベント発火元イベントID。 */
  private String triggerCtrlId;
  /** イベント発火元コントロールID。 */
  private String triggerEventId;
  /** イベント発火元項目名称。 */
  private String triggerItemName;
  /** イベント発火元項目名称。 */
  private String triggerItemId;



	/* ------ 検索結果ページング処理用の項目 -------- */
	/** 1ページあたりの表示件数。 */
	private int perPage;
	/** 現在のページ位置。 */
	private int nowPage;
  /** 全ページ数。 */
  private int totalPage;
  /** 全レコード数。 */
  private int totalRecord;


  private String chengeJsp;



}
