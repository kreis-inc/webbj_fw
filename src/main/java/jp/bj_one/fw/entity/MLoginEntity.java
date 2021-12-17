package jp.bj_one.fw.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 *
 * @author
 */
public class MLoginEntity implements EntityInterface , Serializable {

  /** COMPANY_CD。 */
  private String companyCd;

  /** USER_ID。 */
  private String userId;

  /** USER_NAME。 */
  private String userName;

  /** ADMIN_FLG。 */
  private String adminFlg;

  /** PASSWORD。 */
  private String password;

  /** STOP_FLG。 */
  private String stopFlg;

  /** REMARKS。 */
  private String remarks;

  /** CREATE_DATE。 */
  private Timestamp createDate;

  /** CREATE_USER_ID。 */
  private String createUserId;

  /** CREATE_USER_NAME。 */
  private String createUserName;

  /** UPDATE_DATE。 */
  private Timestamp updateDate;

  /** UPDATE_USER_ID。 */
  private String updateUserId;

  /** UPDATE_USER_NAME。 */
  private String updateUserName;

  /** REVISION。 */
  private BigDecimal revision;

  /**
   * COMPANY_CDを呼び出し元に返します。
   *
   * @return COMPANY_CD
   */
  public String getCompanyCd() {
    return this.companyCd;
  }

  /**
   * COMPANY_CDを設定します。
   *
   * @param companyCd COMPANY_CD
   */
  public void setCompanyCd(String companyCd) {
    this.companyCd = companyCd;
  }

  /**
   * USER_IDを呼び出し元に返します。
   *
   * @return USER_ID
   */
  public String getUserId() {
    return this.userId;
  }

  /**
   * USER_IDを設定します。
   *
   * @param userId USER_ID
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * USER_NAMEを呼び出し元に返します。
   *
   * @return USER_NAME
   */
  public String getUserName() {
    return this.userName;
  }

  /**
   * USER_NAMEを設定します。
   *
   * @param userName USER_NAME
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * ADMIN_FLGを呼び出し元に返します。
   *
   * @return ADMIN_FLG
   */
  public String getAdminFlg() {
    return this.adminFlg;
  }

  /**
   * ADMIN_FLGを設定します。
   *
   * @param adminFlg ADMIN_FLG
   */
  public void setAdminFlg(String adminFlg) {
    this.adminFlg = adminFlg;
  }

  /**
   * PASSWORDを呼び出し元に返します。
   *
   * @return PASSWORD
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * PASSWORDを設定します。
   *
   * @param password PASSWORD
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * STOP_FLGを呼び出し元に返します。
   *
   * @return STOP_FLG
   */
  public String getStopFlg() {
    return this.stopFlg;
  }

  /**
   * STOP_FLGを設定します。
   *
   * @param stopFlg STOP_FLG
   */
  public void setStopFlg(String stopFlg) {
    this.stopFlg = stopFlg;
  }

  /**
   * REMARKSを呼び出し元に返します。
   *
   * @return REMARKS
   */
  public String getRemarks() {
    return this.remarks;
  }

  /**
   * REMARKSを設定します。
   *
   * @param remarks REMARKS
   */
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  /**
   * CREATE_DATEを呼び出し元に返します。
   *
   * @return CREATE_DATE
   */
  public Timestamp getCreateDate() {
    return this.createDate;
  }

  /**
   * CREATE_DATEを設定します。
   *
   * @param createDate CREATE_DATE
   */
  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  /**
   * CREATE_USER_IDを呼び出し元に返します。
   *
   * @return CREATE_USER_ID
   */
  public String getCreateUserId() {
    return this.createUserId;
  }

  /**
   * CREATE_USER_IDを設定します。
   *
   * @param createUserId CREATE_USER_ID
   */
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }

  /**
   * CREATE_USER_NAMEを呼び出し元に返します。
   *
   * @return CREATE_USER_NAME
   */
  public String getCreateUserName() {
    return this.createUserName;
  }

  /**
   * CREATE_USER_NAMEを設定します。
   *
   * @param createUserName CREATE_USER_NAME
   */
  public void setCreateUserName(String createUserName) {
    this.createUserName = createUserName;
  }

  /**
   * UPDATE_DATEを呼び出し元に返します。
   *
   * @return UPDATE_DATE
   */
  public Timestamp getUpdateDate() {
    return this.updateDate;
  }

  /**
   * UPDATE_DATEを設定します。
   *
   * @param updateDate UPDATE_DATE
   */
  public void setUpdateDate(Timestamp updateDate) {
    this.updateDate = updateDate;
  }

  /**
   * UPDATE_USER_IDを呼び出し元に返します。
   *
   * @return UPDATE_USER_ID
   */
  public String getUpdateUserId() {
    return this.updateUserId;
  }

  /**
   * UPDATE_USER_IDを設定します。
   *
   * @param updateUserId UPDATE_USER_ID
   */
  public void setUpdateUserId(String updateUserId) {
    this.updateUserId = updateUserId;
  }

  /**
   * UPDATE_USER_NAMEを呼び出し元に返します。
   *
   * @return UPDATE_USER_NAME
   */
  public String getUpdateUserName() {
    return this.updateUserName;
  }

  /**
   * UPDATE_USER_NAMEを設定します。
   *
   * @param updateUserName UPDATE_USER_NAME
   */
  public void setUpdateUserName(String updateUserName) {
    this.updateUserName = updateUserName;
  }

  /**
   * REVISIONを呼び出し元に返します。
   *
   * @return REVISION
   */
  public BigDecimal getRevision() {
    return this.revision;
  }

  /**
   * REVISIONを設定します。
   *
   * @param revision REVISION
   */
  public void setRevision(BigDecimal revision) {
    this.revision = revision;
  }
}