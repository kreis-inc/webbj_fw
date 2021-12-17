package jp.bj_one.fw.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BjLiteralEntity implements EntityInterface {

  /** リテラルグループID。 */
  private String literalGroupId;

  /** リテラルアイテムID。 */
  private String literalItemId;

  /** リテラルアイテム名称。 */
  private String literalItemName;

  /** リテラルキー. */
  private String literalKey;
  
  /** リテラル値タイプ. */
  private String literalValueType;

  /** リテラル値（文字列）。 */
  private String literalStringValue;

  /** リテラル値（日付）。 */
  private Date literalDateValue;

  /** リテラル値（数値）。 */
  private BigDecimal literalNumValue;

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("literalGroupId:").append(literalGroupId);
    sb.append("literalItemId:").append(literalItemId);
    sb.append("literalItemName:").append(literalItemName);
    return sb.toString();
  }
}
