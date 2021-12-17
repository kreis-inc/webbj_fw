package jp.bj_one.fw;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.bj_one.fw.dto.CrudParamDto;
import jp.bj_one.fw.form.Form;

@Component
public abstract class AbstractHelper {

  @Autowired
  protected Mapper mapper;

  /**
   * DOZER によるマッピング処理を行います. 汎用マッピング処理のため、特殊処理を伴わない場合に使用します。
   *
   * @param source マッピング元
   * @param <T> マッピング元のクラス
   * @param destinationClass マッピング先クラスの型
   * @return マッピング結果
   */
  public <T> T map(Object source, Class<T> destinationClass) {
    return this.mapper.map(source, destinationClass);
  }

  /**
   * DOZER によるマッピング処理を行います. 汎用マッピング処理のため、特殊処理を伴わない場合に使用します。
   *
   * @param source マッピング元
   * @param <T> マッピング元のクラス
   * @param destinationClass マッピング先クラスの型
   * @param key マッピングID
   * @return マッピング結果
   * @author
   */
  public <T> T map(Object source, Class<T> destinationClass, String key) {
    return this.mapper.map(source, destinationClass, key);
  }

  /**
   * DOZER によるマッピング処理を行います. 汎用マッピング処理のため、特殊処理を伴わない場合に使用します。
   *
   * @param source マッピング元
   * @param destination マッピング先のインスタンス
   */
  public void map(Object source, Object destination) {
    this.mapper.map(source, destination);
  }

  /**
   * DOZER によるマッピング処理を行います. 汎用マッピング処理のため、特殊処理を伴わない場合に使用します。
   *
   * @param source マッピング元
   * @param destination マッピング先のインスタンス
   * @param key マッピングID
   */
  public void map(Object source, Object destination, String key) {
    this.mapper.map(source, destination, key);
  }

  public CrudParamDto formToCrudDto(Form form, Class<? extends CrudParamDto> crudDtoClass) {
    CrudParamDto dto = this.map(form, crudDtoClass);
    this.map(form, dto.getViewEntity());
    return dto;
  }
}
