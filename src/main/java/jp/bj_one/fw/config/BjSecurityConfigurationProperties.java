package jp.bj_one.fw.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bj.security")
public class BjSecurityConfigurationProperties {

  private Boolean activate;

  /** 認証無効時のユーザー情報作成クラス。 */
  private String nonAuthUserInfoCreatorClass;

  /** ユーザー認証クラス。 */
  private String userDetailServiceClass;

  /** 認証成功ハンドラークラス。 */
  private String successHandlerClass;

  /** 認証失敗ハンドラークラス。 */
  private String failureHandlerClass;

  /** 認証画面。 */
  private String loginForm;

  /** 認証エラー画面。 */
  private String loginErrorForm;

  private String[] ignorePackage;

  private String[] allUserAccess;

  /**
   * @return activate
   */
  public Boolean isActivate() {
    if (activate == null) {
      return false;
    }
    return activate;
  }

  /**
   * @param activate セットする activate
   */
  public void setActivate(Boolean activate) {
    this.activate = activate;
  }

  /**
   * @return nonAuthUserInfoCreatorClass
   */
  public String getNonAuthUserInfoCreatorClass() {
    if (StringUtils.isBlank(nonAuthUserInfoCreatorClass)) {
      return "jp.bj_one.fw.common.BjUserInfoCreator";
    }
    return nonAuthUserInfoCreatorClass;
  }

  /**
   * @param nonAuthUserInfoCreatorClass セットする nonAuthUserInfoCreatorClass
   */
  public void setNonAuthUserInfoCreatorClass(String nonAuthUserInfoCreatorClass) {
    this.nonAuthUserInfoCreatorClass = nonAuthUserInfoCreatorClass;
  }

  /**
   * @return userDetailServiceClass
   */
  public String getUserDetailServiceClass() {
    if (StringUtils.isBlank(userDetailServiceClass)) {
      return "jp.bj_one.fw.svc.BjUserDetailsService";
    }
    return userDetailServiceClass;
  }

  /**
   * @param userDetailServiceClass セットする userDetailServiceClass
   */
  public void setUserDetailServiceClass(String userDetailServiceClass) {
    this.userDetailServiceClass = userDetailServiceClass;
  }

  /**
   * @return successHandlerClass
   */
  public String getSuccessHandlerClass() {
    if (StringUtils.isBlank(successHandlerClass)) {
      return "jp.bj_one.fw.svc.BjAuthenticationSuccessHandler";
    }
    return successHandlerClass;
  }

  /**
   * @param successHandlerClass セットする successHandlerClass
   */
  public void setSuccessHandlerClass(String successHandlerClass) {
    this.successHandlerClass = successHandlerClass;
  }

  /**
   * 認証エラーハンドリングクラスを呼び出し元に返します。
   * 
   * @return 認証エラーハンドリングクラス
   */
  public String getFailureHandlerClass() {
    if (StringUtils.isBlank(failureHandlerClass)) {
      return "jp.bj_one.fw.svc.BjAuthenticationFailureHandler";
    }
    return failureHandlerClass;
  }

  /**
   * 認証エラーハンドリングクラスを設定します。
   * 
   * @param failureHandlerClass 認証エラーハンドリングクラス
   */
  public void setFailureHandlerClass(String failureHandlerClass) {
    this.failureHandlerClass = failureHandlerClass;
  }

  /**
   * ログインフォームパスを呼び出し元に返します。
   * 
   * @return ログインフォームパス
   */
  public String getLoginForm() {
    if (StringUtils.isBlank(loginForm)) {
      return "/loginForm";
    }
    return loginForm;
  }

  /**
   * ログインフォームパスを設定します。
   * 
   * @param loginForm ログインフォームパス
   */
  public void setLoginForm(String loginForm) {
    this.loginForm = loginForm;
  }

  /**
   * ログインエラーフォームパスを呼び出し元に返します。
   * 
   * @return ログインエラーフォームパス
   */
  public String getLoginErrorForm() {
    if (StringUtils.isBlank(loginErrorForm)) {
      return "/loginError";
    }
    return loginErrorForm;
  }

  /**
   * ログインエラーフォームパスを設定します。
   * 
   * @param loginErrorForm ログインエラーフォームパス
   */
  public void setLoginErrorForm(String loginErrorForm) {
    this.loginErrorForm = loginErrorForm;
  }

  /**
   * @return ignorePackage
   */
  public String[] getIgnorePackage() {
    if (ignorePackage == null) {
      return new String[] {"/public/**"};
    }
    return ignorePackage;
  }

  /**
   * @param ignorePackage セットする ignorePackage
   */
  public void setIgnorePackage(String[] ignorePackage) {
    this.ignorePackage = ignorePackage;
  }

  /**
   * @return allUserAccess
   */
  public String[] getAllUserAccess() {
    if (allUserAccess == null) {
      return new String[] {"/", "/loginForm", "/loginError"};
    }
    return allUserAccess;
  }

  /**
   * @param allUserAccess セットする allUserAccess
   */
  public void setAllUserAccess(String[] allUserAccess) {
    this.allUserAccess = allUserAccess;
  }
}
