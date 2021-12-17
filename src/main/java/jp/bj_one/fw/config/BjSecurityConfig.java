package jp.bj_one.fw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.bj_one.fw.svc.BjTimeoutEntryPoint;

@Configuration
public class BjSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AutowireCapableBeanFactory factory;

  @Autowired
  BjSecurityConfigurationProperties configurationProperties;

  @Override
  public void configure(WebSecurity webSecurity) throws Exception {

    // 設定を無視するリクエスト設定
    if (configurationProperties.isActivate()) {
      // 認証機能が有効
      webSecurity.ignoring().antMatchers(
          configurationProperties.getIgnorePackage());
    } else {
      webSecurity.ignoring().antMatchers(
          "/**");
    }
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {

    // 成功時ハンドリングクラス
    @SuppressWarnings("rawtypes")
    Class successHandlerClazz = Class.forName(configurationProperties.getSuccessHandlerClass());
    @SuppressWarnings("unchecked")
    AuthenticationSuccessHandler successHandler = (AuthenticationSuccessHandler) factory.createBean(successHandlerClazz);
    
    // エラー時ハンドリングクラス
    @SuppressWarnings("rawtypes")
    Class failureHandlerClazz = Class.forName(configurationProperties.getFailureHandlerClass());
    @SuppressWarnings("unchecked")
    AuthenticationFailureHandler failureHandler = (AuthenticationFailureHandler) factory.createBean(failureHandlerClazz);


    httpSecurity.authorizeRequests()
        .antMatchers(configurationProperties.getAllUserAccess()).permitAll()
        .anyRequest().authenticated().and()
        .headers().frameOptions().disable();

    httpSecurity.csrf().disable()
        .formLogin()
        .successHandler(successHandler)
        .loginProcessingUrl("/login")
        .loginPage(configurationProperties.getLoginForm())
        .failureUrl(configurationProperties.getLoginErrorForm())
        .usernameParameter("userId").passwordParameter("password")
        .failureHandler(failureHandler).and();

    httpSecurity.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
        .logoutSuccessUrl(configurationProperties.getLoginForm());
    
    // タイムアウト処理
    httpSecurity.exceptionHandling()
        .authenticationEntryPoint(new BjTimeoutEntryPoint(configurationProperties.getLoginForm()));
  }

  @Configuration
  protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private AutowireCapableBeanFactory factory;

    @Autowired
    BjSecurityConfigurationProperties configurationProperties;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
      // 認証するユーザーを設定する
      @SuppressWarnings("rawtypes")
      Class userDetailServiceClazz = Class.forName(configurationProperties.getUserDetailServiceClass());
      @SuppressWarnings("unchecked")
      UserDetailsService userDetailService = (UserDetailsService) factory.createBean(userDetailServiceClazz);
      auth.userDetailsService(userDetailService)
          // 入力値をbcryptでハッシュ化した値でパスワード認証を行う
          .passwordEncoder(new Md5PasswordEncoder());
    }
  }

}
