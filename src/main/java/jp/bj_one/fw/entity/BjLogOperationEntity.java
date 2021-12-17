package jp.bj_one.fw.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jp.bj_one.fw.entity.EntityInterface;

/**
 *
 *
 * @author
 */
public class BjLogOperationEntity implements EntityInterface {

  /** seq。 */
  private String seq;

  /** object。 */
  private String object;

  /** status。 */
  private String status;

  /** date。 */
  private Timestamp date;

  /** ip。 */
  private String ip;

  /** system_id。 */
  private String systemId;

  /** user_id。 */
  private String userId;

  /** user_name。 */
  private String userName;

  /** service_id。 */
  private String serviceId;

  /** service_name。 */
  private String serviceName;

  /** action_id。 */
  private String actionId;

  /** action_name。 */
  private String actionName;

  /** trigger_ctrl_id。 */
  private String triggerCtrlId;

  /** trigger_event_id。 */
  private String triggerEventId;

  /** change_screen_id。 */
  private String changeScreenId;

  /** change_screen_name。 */
  private String changeScreenName;

  /** error_cd。 */
  private String errorCd;

  /** error。 */
  private String error;

  /** input。 */
  private String input;

  /** actiontime。 */
  private BigDecimal actiontime;

  /**
   * seqを呼び出し元に返します。
   *
   * @return seq
   */
  public String getSeq() {
    return this.seq;
  }

  /**
   * seqを設定します。
   *
   * @param seq seq
   */
  public void setSeq(String seq) {
    this.seq = seq;
  }

  /**
   * objectを呼び出し元に返します。
   *
   * @return object
   */
  public String getObject() {
    return this.object;
  }

  /**
   * objectを設定します。
   *
   * @param object object
   */
  public void setObject(String object) {
    this.object = object;
  }

  /**
   * statusを呼び出し元に返します。
   *
   * @return status
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * statusを設定します。
   *
   * @param status status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * dateを呼び出し元に返します。
   *
   * @return date
   */
  public Timestamp getDate() {
    return this.date;
  }

  /**
   * dateを設定します。
   *
   * @param date date
   */
  public void setDate(Timestamp date) {
    this.date = date;
  }

  /**
   * ipを呼び出し元に返します。
   *
   * @return ip
   */
  public String getIp() {
    return this.ip;
  }

  /**
   * ipを設定します。
   *
   * @param ip ip
   */
  public void setIp(String ip) {
    this.ip = ip;
  }

  /**
   * system_idを呼び出し元に返します。
   *
   * @return system_id
   */
  public String getSystemId() {
    return this.systemId;
  }

  /**
   * system_idを設定します。
   *
   * @param systemId system_id
   */
  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  /**
   * user_idを呼び出し元に返します。
   *
   * @return user_id
   */
  public String getUserId() {
    return this.userId;
  }

  /**
   * user_idを設定します。
   *
   * @param userId user_id
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * user_nameを呼び出し元に返します。
   *
   * @return user_name
   */
  public String getUserName() {
    return this.userName;
  }

  /**
   * user_nameを設定します。
   *
   * @param userName user_name
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * service_idを呼び出し元に返します。
   *
   * @return service_id
   */
  public String getServiceId() {
    return this.serviceId;
  }

  /**
   * service_idを設定します。
   *
   * @param serviceId service_id
   */
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  /**
   * service_nameを呼び出し元に返します。
   *
   * @return service_name
   */
  public String getServiceName() {
    return this.serviceName;
  }

  /**
   * service_nameを設定します。
   *
   * @param serviceName service_name
   */
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  /**
   * action_idを呼び出し元に返します。
   *
   * @return action_id
   */
  public String getActionId() {
    return this.actionId;
  }

  /**
   * action_idを設定します。
   *
   * @param actionId action_id
   */
  public void setActionId(String actionId) {
    this.actionId = actionId;
  }

  /**
   * action_nameを呼び出し元に返します。
   *
   * @return action_name
   */
  public String getActionName() {
    return this.actionName;
  }

  /**
   * action_nameを設定します。
   *
   * @param actionName action_name
   */
  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  /**
   * trigger_ctrl_idを呼び出し元に返します。
   *
   * @return trigger_ctrl_id
   */
  public String getTriggerCtrlId() {
    return this.triggerCtrlId;
  }

  /**
   * trigger_ctrl_idを設定します。
   *
   * @param triggerCtrlId trigger_ctrl_id
   */
  public void setTriggerCtrlId(String triggerCtrlId) {
    this.triggerCtrlId = triggerCtrlId;
  }

  /**
   * trigger_event_idを呼び出し元に返します。
   *
   * @return trigger_event_id
   */
  public String getTriggerEventId() {
    return this.triggerEventId;
  }

  /**
   * trigger_event_idを設定します。
   *
   * @param triggerEventId trigger_event_id
   */
  public void setTriggerEventId(String triggerEventId) {
    this.triggerEventId = triggerEventId;
  }

  /**
   * change_screen_idを呼び出し元に返します。
   *
   * @return change_screen_id
   */
  public String getChangeScreenId() {
    return this.changeScreenId;
  }

  /**
   * change_screen_idを設定します。
   *
   * @param changeScreenId change_screen_id
   */
  public void setChangeScreenId(String changeScreenId) {
    this.changeScreenId = changeScreenId;
  }

  /**
   * change_screen_nameを呼び出し元に返します。
   *
   * @return change_screen_name
   */
  public String getChangeScreenName() {
    return this.changeScreenName;
  }

  /**
   * change_screen_nameを設定します。
   *
   * @param changeScreenName change_screen_name
   */
  public void setChangeScreenName(String changeScreenName) {
    this.changeScreenName = changeScreenName;
  }

  /**
   * error_cdを呼び出し元に返します。
   *
   * @return error_cd
   */
  public String getErrorCd() {
    return this.errorCd;
  }

  /**
   * error_cdを設定します。
   *
   * @param errorCd error_cd
   */
  public void setErrorCd(String errorCd) {
    this.errorCd = errorCd;
  }

  /**
   * errorを呼び出し元に返します。
   *
   * @return error
   */
  public String getError() {
    return this.error;
  }

  /**
   * errorを設定します。
   *
   * @param error error
   */
  public void setError(String error) {
    this.error = error;
  }

  /**
   * inputを呼び出し元に返します。
   *
   * @return input
   */
  public String getInput() {
    return this.input;
  }

  /**
   * inputを設定します。
   *
   * @param input input
   */
  public void setInput(String input) {
    this.input = input;
  }

  /**
   * actiontimeを呼び出し元に返します。
   *
   * @return actiontime
   */
  public BigDecimal getActiontime() {
    return this.actiontime;
  }

  /**
   * actiontimeを設定します。
   *
   * @param actiontime actiontime
   */
  public void setActiontime(BigDecimal actiontime) {
    this.actiontime = actiontime;
  }
}