package jp.bj_one.fw.entity;

public class BjLiteralParamEntity implements EntityInterface {

  private String codeColumn;

  private String literalGroup;

  private String nameColumn;

  private String orderColumn;

  private boolean useBjLiteral;

  /**
   * @return codeColumn
   */
  public String getCodeColumn() {
    return codeColumn;
  }

  /**
   * @param codeColumn セットする codeColumn
   */
  public void setCodeColumn(String codeColumn) {
    this.codeColumn = codeColumn;
  }

  /**
   * @return literalGroup
   */
  public String getLiteralGroup() {
    return literalGroup;
  }

  /**
   * @param literalGroup セットする literalGroup
   */
  public void setLiteralGroup(String literalGroup) {
    this.literalGroup = literalGroup;
  }

  /**
   * @return nameColumn
   */
  public String getNameColumn() {
    return nameColumn;
  }

  /**
   * @param nameColumn セットする nameColumn
   */
  public void setNameColumn(String nameColumn) {
    this.nameColumn = nameColumn;
  }

  /**
   * @return orderColumn
   */
  public String getOrderColumn() {
    return orderColumn;
  }

  /**
   * @param orderColumn セットする orderColumn
   */
  public void setOrderColumn(String orderColumn) {
    this.orderColumn = orderColumn;
  }

  /**
   * @return useBjLiteral
   */
  public boolean isUseBjLiteral() {
    return useBjLiteral;
  }

  /**
   * @param useBjLiteral セットする useBjLiteral
   */
  public void setUseBjLiteral(boolean useBjLiteral) {
    this.useBjLiteral = useBjLiteral;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("codeColumn:").append(codeColumn);
    sb.append("literalGroup:").append(literalGroup);
    sb.append("nameColumn:").append(nameColumn);
    sb.append("orderColumn:").append(orderColumn);
    sb.append("useBjLiteral:").append(useBjLiteral);
    return sb.toString();
  }
}
