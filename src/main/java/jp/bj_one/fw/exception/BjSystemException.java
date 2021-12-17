package jp.bj_one.fw.exception;

public class BjSystemException extends RuntimeException {

  /** シリアルバージョンUID。 */
  private static final long serialVersionUID = 1L;

  /**
   * 例外オブジェクトを引数とするコンストラクタ。
   *
   * @param e 例外オブジェクト
   */
  public BjSystemException(Exception e) {
    super(e);
  }
}
