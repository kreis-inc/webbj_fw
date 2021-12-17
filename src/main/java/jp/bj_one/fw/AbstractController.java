package jp.bj_one.fw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.util.JavaScriptUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.bj_one.fw.dto.ResponseDto;
import jp.bj_one.fw.exception.BjSystemException;
import jp.bj_one.fw.svc.AbstractService;

@Controller
public abstract class AbstractController {

  @Autowired
  MessageSource messageSource;

  /**
   * ヘルパークラスの取得処理.
   *
   * @return ヘルパークラス.
   */
  protected abstract AbstractHelper getHelper();

  /**
   * サービスクラスの取得処理.
   *
   * @return サービスクラス.
   */
  protected abstract AbstractService getService();

  /**
   * JSPパスを取得.
   *
   * @return
   */
  protected abstract String getJspPath();


  /**
   * 処理結果をModelに設定します。
   *
   * @param model モデルオブジェクト
   * @param dto 処理結果
   */
  protected void setModelData(Model model, ResponseDto dto) {
    try {
      model.addAttribute("responseDto", JavaScriptUtils.javaScriptEscape(new ObjectMapper().writeValueAsString(dto)).replaceAll("&", "&amp;"));
    } catch (JsonProcessingException e) {
      throw new BjSystemException(e);
    }
  }
}
