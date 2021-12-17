package jp.bj_one.fw.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author
 */
@Setter
@Getter
public class BjMSystemInfoEntity implements EntityInterface {

  /** bj_system_id。 */
  private String bjSystemId;

  /** bj_company。 */
  private String bjCompany;

  /** bj_remarks。 */
  private String bjRemarks;

  /** bj_db。 */
  private String bjDb;

  /** bj_appserver。 */
  private String bjAppserver;

  /** bj_java_version。 */
  private String bjJavaVersion;

  /** bj_etc。 */
  private String bjEtc;

  /** bj_repo。 */
  private String bjRepo;

  /** bj_yukokigen_month。 */
  private BigDecimal bjYukokigenMonth;

  /** bj_password_ch_day。 */
  private BigDecimal bjPasswordChDay;

  /** bj_number_generations。 */
  private BigDecimal bjNumberGenerations;

  /** bj_retry。 */
  private BigDecimal bjRetry;

  /** bj_log_flg。 */
  private String bjLogFlg;

  /** bj_lock_flg。 */
  private String bjLockFlg;

  /** bj_password_chk_str。 */
  private String bjPasswordChkStr;

  /** bj_create_date。 */
  private Timestamp bjCreateDate;

  /** bj_create_id。 */
  private String bjCreateId;

  /** bj_update_date。 */
  private Timestamp bjUpdateDate;

  /** bj_update_id。 */
  private String bjUpdateId;

  /** revision。 */
  private BigDecimal revision;

  /** bj_delete_flg。 */
  private String bjDeleteFlg;


}