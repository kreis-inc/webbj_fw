package jp.bj_one.fw.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dozer.DozerConverter;

/**
 * String から 日付への dozer コンバータークラス。
 * 
 * @author kaoru.amagai
 */
public class DozerStringToDateConverter extends DozerConverter<String, Date> {

  /**
   * コンストラクタ
   */
  public DozerStringToDateConverter() {
    super(String.class, Date.class);
  }

  /**
   * DateからStringへの変換処理
   * 
   * @param arg0 日付パラメータ
   * @param arg1 文字列パラメータ
   */
  @Override
  public String convertFrom(Date arg0, String arg1) {
    if( arg0 == null ) {
      return null;
    }
    return new SimpleDateFormat("yyyy/MM/dd").format(arg0);
  }

  /**
   * String から Date への変換処理
   * 
   * @param arg0 文字列パラメータ
   * @param arg1 日付パラメータ
   */
  @Override
  public Date convertTo(String arg0, Date arg1) {
    try {
      if( arg0 == null ) {
        return null;
      }
      if( !arg0.matches("\\d{4}/\\d{2}/\\d{2}") ) {
        return null;
      }
      return new Date(new SimpleDateFormat("yyyy/MM/dd").parse(arg0).getTime());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
