package jp.bj_one.fw;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BjLoginController {

  @RequestMapping(value = "loginForm", method = RequestMethod.GET)
  public String loginForm() {
    return "loginForm.jsp";
  }

  @RequestMapping(value = "menu")
  public String menu() {
    return "menu.jsp";
  }

  @RequestMapping(value = "loginError")
  public String loginError() {
    return "loginError.jsp";
  }
}
