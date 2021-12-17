package jp.bj_one.fw.common.literal;

import java.io.Serializable;

/**
 * WBJの表示モードを持つ列挙型です。
 * 
 * @author kaoru.amagai
 */
public enum BjDisplayMode  implements Serializable{
  /** モード1。 */
  VIEW1("view1"),
  /** モード2。 */
  VIEW2("view2"),
  /** モード3。 */
  VIEW3("view3"),
  /** モード4。 */
  VIEW4("view4"),
  /** モード5。 */
  VIEW5("view5");

  /** 表示モード文字列 */
  private String mode;

  /**
   * 表示モード文字列を引数とするコンストラクタ。
   * 
   * <pre>
   * 列挙型のコンストラクタであるため、外部参照不可で定義しています。
   * </pre>
   * 
   * @param mode 表示モード文字列
   */
  private BjDisplayMode(String mode) {
    this.mode = mode;
  }

  /**
   * 表示モードを文字列表現した値を呼び出し元に返します。
   * 
   * @return 表示モードの文字列表現値
   */
  @Override
  public String toString() {
    return this.mode;
  }
}
