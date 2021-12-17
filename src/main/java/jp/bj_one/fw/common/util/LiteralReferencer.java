package jp.bj_one.fw.common.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.bj_one.fw.common.literal.BjLiteralKeyType;
import jp.bj_one.fw.entity.BjLiteralEntity;
import jp.bj_one.fw.entity.BjLiteralItemEntity;
import jp.bj_one.fw.svc.BjLiteralService;

/**
 * リテラルマスター参照ユーティリティ。
 * 
 * @author kaoru.amagai
 */
@Component
public class LiteralReferencer {
  /** リテラル参照サービス。 */
  private static BjLiteralService literalService;

  /** autowired 用リテラル参照サービス。 */
  @Autowired
  private BjLiteralService autowiredLiteralService;

  /** 初期処理。 */
  @PostConstruct
  private void init() {
    literalService = autowiredLiteralService;
  }

  /**
   * リテラルアイテムのリストを呼び出し元に返します。
   * 
   * <pre>
   * 指定されたグループIDに該当するリテラルアイテムのリストを呼び出し元に返します。
   * 該当するリテラルグループが無い場合、またはそのグループに紐づくアイテムが無い場合は null が返されます。
   * 
   * 取得したリテラルアイテムから各種リテラル値を参照することができます。
   * 詳細は BjLiteralItemEntity クラスを参照してください。
   * 
   * 例:
   *   List<BjLiteralItemEntity> itemList = LiteralReferencer.getLiteralItems( "BJ_INFO_CLASSIFICATION" );
   *   for( BjLiteralItemEntity item : itemList ) {
   *     String value = item.getStringValue( "VALUE_C1" );
   *   }
   * </pre>
   * 
   * @param literalGroupId リテラルグループID
   * @return リテラルアイテムのリスト
   * @see jp.bj_one.fw.entity.BjLiteralItemEntity
   */
  public static List<BjLiteralItemEntity> getLiteralItems(String literalGroupId) {
    // パラメータ
    BjLiteralItemEntity param = new BjLiteralItemEntity();
    param.setLiteralGroupId(literalGroupId);

    // リテラルアイテムの検索
    List<BjLiteralItemEntity> items = literalService.findLiteralItems(param);
    if (items == null) {
      return null;
    }
    List<BjLiteralItemEntity> returnList = new ArrayList<BjLiteralItemEntity>();
    for( BjLiteralItemEntity item : items ) {
      BjLiteralItemEntity returnItem = getLiteralItem(literalGroupId, item.getLiteralItemId());
      if (returnItem != null) {
        returnList.add(returnItem);
      }
    }
    
    // 呼び出し元に返す
    if( returnList.size() == 0 ) {
      return null;
    }
    return returnList;
  }
  
  /**
   * リテラルアイテムを呼び出し元に返します。
   * 
   * <pre>
   * 指定されたグループID、アイテムIDに該当するリテラルアイテムを呼び出し元に返します。
   * 該当するリテラルアイテムが無い場合、null が返されます。
   * 
   * 取得したリテラルアイテムから各種リテラル値を参照することができます。
   * 詳細は BjLiteralItemEntity クラスを参照してください。
   * 
   * 例:
   *   BjLiteralItemEntity item = LiteralReferencer.getLiteralItem( "BJ_INFO_CLASSIFICATION", "MAINTENACE" );
   *   String value = item.getStringValue( "VALUE_C1" );
   * 
   * </pre>
   * 
   * @param literalGroupId リテラルグループID
   * @param literalItemId リテラルアイテムID
   * @return リテラルアイテム
   * @see jp.bj_one.fw.entity.BjLiteralItemEntity
   */
  public static BjLiteralItemEntity getLiteralItem(String literalGroupId, String literalItemId) {
    // パラメータ
    BjLiteralItemEntity param = new BjLiteralItemEntity();
    param.setLiteralGroupId(literalGroupId);
    param.setLiteralItemId(literalItemId);

    // リテラルアイテムの検索
    List<BjLiteralItemEntity> items = literalService.findLiteralItems(param);
    if (items == null) {
      return null;
    }
    BjLiteralItemEntity item = items.get(0);

    // リテラル値の検索
    List<BjLiteralEntity> values = literalService.findItemValues(param);
    if (values == null || values.size() == 0) {
      return item;
    }
    
    // リテラル値をマップ
    Map<String, Object> valueMap = new TreeMap<String, Object>();
    for (BjLiteralEntity value : values) {
      // 型が取得できていない場合は文字列として処理する
      if (value.getLiteralValueType() == null) value.setLiteralValueType(BjLiteralKeyType.CHARACTER.toString());

      if (value.getLiteralStringValue() == null) {
        // 値が取得できない場合は null をマップ
        valueMap.put(value.getLiteralKey(), null);
        continue;
      }

      try {
        switch (value.getLiteralValueType()) {
          case "NUMBER": // 数値
            valueMap.put(value.getLiteralKey(), new BigDecimal(value.getLiteralStringValue()));
            break;

          case "DATE": // 日付
            SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
            valueMap.put(value.getLiteralKey(), f.parse(value.getLiteralStringValue()));
            break;

          default: // 文字列
            valueMap.put(value.getLiteralKey(), value.getLiteralStringValue());

        }

      } catch (Exception e) {
        // 例外時は null をマップ
        valueMap.put(value.getLiteralKey(), null);
      }
    }
    item.setValueMap(valueMap);
    
    // 呼び出し元に返す
    return item;
  }
}
