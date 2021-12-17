package jp.bj_one.fw.common.literal;

/**
 * WBJのファイル属性を持つ列挙型です。
 * 
 * @author kaoru.amagai
 */
public enum BjFileAttribute {
  /** ユニークファイル名。 */
  UNIQUE_NAME("uniqueName"),
  /** オリジナルファイル名。 */
  ORIGINAL_NAME("originalName");
  
  /** ファイル属性文字列 */
  private String attr;

  /**
   * ファイル属性文字列を引数とするコンストラクタ。
   * 
   * <pre>
   * 列挙型のコンストラクタであるため、外部参照不可で定義しています。
   * </pre>
   * 
   * @param mode ファイル属性文字列
   */
  private BjFileAttribute(String attr) {
    this.attr = attr;
  }

  /**
   * ファイル属性を文字列表現した値を呼び出し元に返します。
   * 
   * @return ファイル属性の文字列表現値
   */
  @Override
  public String toString() {
    return this.attr;
  }
}
