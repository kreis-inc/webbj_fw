package jp.bj_one.fw.common.util;

/**
 * エスケープユーティリティクラス。
 *
 * @author kaoru.amagai
 */
public class Escaper {
  /**
   * SQL LIKE 前方一致条件エスケープ文字列を作成します。
   *
   * <pre>
   * 詳細は sqlLike メソッドを参照してください。
   * </pre>
   *
   * @param target エスケープ対象文字列
   *    * @return エスケープされた文字列
   */
  public static String sqlForwardLike(String target) {
    if (target == null || target.length() == 0) {
      return null;
    }
    return sqlLike(target) + "%";
  }

  /**
   * SQL LIKE 後方一致条件エスケープ文字列を作成します。
   *
   * <pre>
   * 詳細は sqlLike メソッドを参照してください。
   * </pre>
   *
   * @param target エスケープ対象文字列
   * @return エスケープされた文字列
   */
  public static String sqlBackwardLike(String target) {
    if (target == null || target.length() == 0) {
      return null;
    }

    return "%" + sqlLike(target);
  }

  /**
   * SQL LIKE 部分一致条件エスケープ文字列を作成します。
   *
   * <pre>
   * 詳細は sqlLike メソッドを参照してください。
   * </pre>
   *
   * @param target エスケープ対象文字列
   * @return エスケープされた文字列
   */
  public static String sqlPartialLike(String target) {
    if (target == null || target.length() == 0) {
      return null;
    }

    return "%" + sqlLike(target) + "%";
  }

  /**
   * SQL LIKE 条件エスケープ文字列を作成します。
   *
   * <pre>
   * エスケープは~（チルダ）を用いて行います。
   * SQL 文において LIKE 条件のエスケープを~（チルダ）で行うよう定義してください。
   * </pre>
   *
   * @param target エスケープ対象文字列
   * @return エスケープされた文字列
   */
  private static String sqlLike(String target) {


    // エスケープ
    //target = target.replaceAll("~", "~~").replaceAll("%", "~%").replaceAll("_","~_");
	//target = target.replaceAll("~", "\\~").replaceAll("%", "\\%").replaceAll("_", "\\_");
    target = target.replaceAll("~", "\\\\~").replaceAll("%", "\\\\%").replaceAll("_","\\\\_");
    return target;
  }

  /**
   * SQL LIKE 条件エスケープ文字列を元に戻します。
   *
   * @param target エスケープ後文字列
   * @return エスケープ前の文字列
   */
  public static String unescapeSqlLike(String target) {
    if (target == null) {
      return null;
    }

    // エスケープ解除
    target = target.replaceAll("^%", "").replaceAll("([^~])%$", "$1").replaceAll("~%", "%").replaceAll("~_", "_")
        .replaceAll("~~", "~");

    return target;
  }
}
