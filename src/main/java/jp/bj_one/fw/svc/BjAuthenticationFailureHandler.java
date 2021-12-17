package jp.bj_one.fw.svc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 認証失敗時ハンドラークラス。
 * 
 * @author kaoru.amagai
 */
@Component
public class BjAuthenticationFailureHandler implements AuthenticationFailureHandler {
  /**
   * 認証失敗時のハンドリングメソッド。
   * 
   * @param request HTTPリクエスト
   * @param response HTTPレスポンス
   * @param e 認証例外オブジェクト
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException e) throws IOException, ServletException {
    // 認証失敗
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
