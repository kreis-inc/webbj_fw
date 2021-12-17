package jp.bj_one.fw.svc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * タイムアウト処理のエントリーポイントクラス。
 * 
 * @author kaoru.amagai
 */
public class BjTimeoutEntryPoint extends LoginUrlAuthenticationEntryPoint {
  /**
   * ログインフォームURLを引数とするコンストラクタ。
   * 
   * @param loginFormUrl ログインフォームURL
   */
  public BjTimeoutEntryPoint(String loginFormUrl) {
    super(loginFormUrl);
  }

  /**
   * 例外ハンドリング開始処理。
   * 
   * @param request HTTPリクエスト
   * @param reponse HTTPレスポンス
   * @param authException 認証例外オブジェクト
   * @throws IOException 入出力例外が発生した場合
   * @throws ServletException サーブレット例外が発生した場合
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {

    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    super.commence(request, response, authException);
  }
}
