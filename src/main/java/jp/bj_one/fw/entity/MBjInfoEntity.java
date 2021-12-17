package jp.bj_one.fw.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * お知らせ情報テーブルエンティティクラス。
 *
 * @author kaoru.amagai
 */
@Getter
@Setter
public class MBjInfoEntity implements EntityInterface {
  /** SEQ。 */
  private String seq;
  /** お知らせ区分。 */
  private String kbn;
  /** 起票日。 */
  private Date date;
  /** タイトル。 */
  private String title;
  /** お知らせ情報。 */
  private String textInfo;
  /** 重要事項。 */
  private String important;
  /** 掲載期間From。 */
  private Date periodFrom;
  /** 掲載期間To。 */
  private Date periodTo;
}