package jp.bj_one.fw.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * リテラルアイテムエンティティクラス。
 * 
 * @author kaoru.amagai
 */
@Getter
@Setter
public class BjLiteralItemEntity {
  /** リテラルグループID。 */
  private String literalGroupId;
  
  /** リテラルアイテムID。 */
  private String literalItemId;
  
  /** リテラルアイテム名称。 */
  private String literalItemName;
  
  /** リテラル値マップ。 */
  private Map<String, Object> valueMap;

  /**
   * リテラル値を呼び出し元に返します。
   * <pre>
   * このリテラルアイテムに対して、指定されたキーに紐づくリテラル値を呼び出し元に返します。
   * キーに紐づく値が存在しない場合、null が返されます。
   * 
   * 取得できる値はリテラルキータイプに応じて String、BigDecimal、Date の何れかとなります。
   * 必要に応じて該当クラスにキャストして利用してください。
   * </pre>
   * 
   * @param literalKey リテラルキー
   * @return リテラル値
   */
  public Object getValue( String literalKey ) {
    return this.valueMap.get( literalKey );
  }

  /**
   * 文字列リテラル値を呼び出し元に返します。
   * <pre>
   * このリテラルアイテムに対して、指定されたキーに紐づくリテラル値を呼び出し元に返します。
   * 指定するリテラルキーのタイプが文字列である場合、このメソッドを利用することができます。
   * </pre>
   * 
   * @param literalKey リテラルキー
   * @return リテラル値
   */
  public String getStringValue( String literalKey ) {
    return (String)this.valueMap.get( literalKey );
  }

  /**
   * 数値リテラル値を呼び出し元に返します。
   * <pre>
   * このリテラルアイテムに対して、指定されたキーに紐づくリテラル値を呼び出し元に返します。
   * 指定するリテラルキーのタイプが数値である場合、このメソッドを利用することができます。
   * </pre>
   * 
   * @param literalKey リテラルキー
   * @return リテラル値
   */
  public BigDecimal getNumberValue( String literalKey ) {
    return (BigDecimal)this.valueMap.get( literalKey );
  }

  /**
   * 日付リテラル値を呼び出し元に返します。
   * <pre>
   * このリテラルアイテムに対して、指定されたキーに紐づくリテラル値を呼び出し元に返します。
   * 指定するリテラルキーのタイプが日付である場合、このメソッドを利用することができます。
   * </pre>
   * 
   * @param literalKey リテラルキー
   * @return リテラル値
   */
  public Date getDateValue( String literalKey ) {
    return (Date)this.valueMap.get( literalKey );
  }
  
  /**
   * リテラルアイテムの文字列表現を呼び出し元に返します。
   * <pre>
   * このリテラルアイテムの各種フィールド、マップされているリテラル値を確認する場合、このメソッドを利用してください。
   * デバッグ要素の強いメソッドであるため、業務ロジックへの組み込みは推奨されません。
   * </pre>
   * 
   * @return リテラルアイテムの文字列表現値
   */
  @Override
  public String toString() {
    String ls = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    
    sb.append( "literalGroupId: " + literalGroupId + ls );
    sb.append( "literalItemId: " + literalItemId + ls );
    sb.append( "literalItemName: " + literalItemName + ls );
    if( valueMap == null ) {
      sb.append( "valueMap: null" );
    }else {
      sb.append( "valueMap: {" + ls );
      for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
        sb.append( "  " + entry.getKey() + ": " + entry.getValue() );
      }    
      sb.append( "}" + ls );
    }
    
    return sb.toString();
  }
}
