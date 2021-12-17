package jp.bj_one.fw.common.literal;

/**
 * WBJのリテラルキータイプを持つ列挙型です。
 * 
 * @author kaoru.amagai
 */
public enum BjLiteralKeyType {
  /** 文字列。 */
  CHARACTER("CHARACTER"),
  /** 数値。 */
  NUMBER("NUMBER"),
  /** 日付。 */
  DATE("DATE");

  /** リテラルキータイプ文字列。 */
  private String literalKeyType;

  /**
   * リテラルキータイプ文字列を引数とするコンストラクタ。
   * 
   * <pre>
   * 列挙型のコンストラクタであるため、外部参照不可で定義しています。
   * </pre>
   * 
   * @param literalKeyType リテラルキータイプ文字列
   */
  private BjLiteralKeyType(String literalKeyType) {
    this.literalKeyType = literalKeyType;
  }

  /**
   * リテラルキータイプを文字列表現した値を呼び出し元に返します。
   * 
   * @return リテラルキータイプの文字列表現値
   */
  @Override
  public String toString() {
    return this.literalKeyType;
  }
}
