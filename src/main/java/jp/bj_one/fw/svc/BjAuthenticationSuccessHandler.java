package jp.bj_one.fw.svc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jp.bj_one.fw.common.BjUserInfo;

/**
 * 認証成功時ハンドラークラス。
 * 
 * @author kaoru.amagai
 */
@Component
public class BjAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  
  /**
   * 認証成功時のハンドリングメソッド。
   * 
   * @param request HTTPリクエスト
   * @param response HTTPレスポンス
   * @param authentication 認証オブジェクト
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    // 認証OK
    response.setStatus(HttpServletResponse.SC_OK);

    // 作成されたユーザー情報の取得
    BjUserInfo userInfo = (BjUserInfo) authentication.getPrincipal();

    // パラメータよりユーザーID、ユーザー名が取得できる場合は塗り替え
    String userId = request.getParameter("tp2UserId");
    String userName = request.getParameter("tp2UserName");
    if (userId != null && userName != null) {
      userInfo.getUser().setUserId(userId);
      userInfo.getUser().setUserName(userName);
    }
    
    // 初期画面表示
    response.sendRedirect("sc002/display");
  }
}
