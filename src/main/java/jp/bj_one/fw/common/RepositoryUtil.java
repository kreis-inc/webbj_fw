package jp.bj_one.fw.common;

import java.math.BigDecimal;

import jp.bj_one.fw.common.util.Escaper;

public class RepositoryUtil {

  /**
   * 検索時の開始行を取得。
   *
   * @param perPage 1ページの表示件数
   * @param nowPage 現在ページ
   * @return 開始行番号
   */
  public static Integer getStartLineNo(Integer perPage, Integer nowPage) {
    if (perPage == null || nowPage == null) {
      return 1;
    }
    return nowPage * perPage - perPage;
  }

  /**
   * 検索時の終了行を取得。
   *
   * @param perPage 1ページの表示件数
   * @param nowPage 現在ページ
   * @return 終了行番号
   */
  public static Integer getEndLineNo(Integer perPage, Integer nowPage) {
    if (perPage == null || nowPage == null) {
      return 10;
    }
    return nowPage * perPage;
  }

  /**
   * 検索時の検索行数を取得。
   *
   * @param perPage 1ページの表示件数
   * @return 終了行番号
   */
  public static Integer getNextLineNo(Integer perPage) {
    if (perPage == null) {
      return 10;
    }
    return perPage;
  }

  /**
   * 全ページ数の算出。
   *
   * @param perPage 1ページの表示件数
   * @param total 総データ件数
   * @return 全ページ数
   */
  public static Integer calculateTotalPage(Integer perPage, Integer total) {
    if (perPage == 0) {
      return 1;
    }
    if (perPage == null || total == null) {
      return 1;
    }

    BigDecimal totalPage = (new BigDecimal(total)).divide(new BigDecimal(perPage), BigDecimal.ROUND_UP );

    return totalPage.setScale(0, BigDecimal.ROUND_UP).intValue();
  }

  /**
   * 前方一致条件エスケープ文字列を作成します。
   *
   * @param condition Like条件
   * @return 前方一致条件文字列
   */
  public static String toForwardLike(String condition) {
    return Escaper.sqlForwardLike(condition);
  }

  /**
   * 後方一致条件エスケープ文字列を作成します。
   *
   * @param condition
   * @return
   */
  public static String toBackwardLike(String condition) {
    return Escaper.sqlBackwardLike(condition);
  }

  /**
   * 部分一致条件エスケープ文字列を作成します。
   *
   * @param condition
   * @return
   */
  public static String toPartialLike(String condition) {
    return Escaper.sqlPartialLike(condition);
  }
}
