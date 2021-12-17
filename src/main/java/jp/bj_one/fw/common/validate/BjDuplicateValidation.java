package jp.bj_one.fw.common.validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.bj_one.fw.common.BjReflectUtil;
import jp.bj_one.fw.common.MessageData;
import jp.bj_one.fw.common.annotation.BjAnnotationUtil;
import jp.bj_one.fw.entity.MessageEntity;
import jp.bj_one.fw.form.BjForm;

/**
 * 入力duplicateCheck
 * @author kreis
 *
 */
public class BjDuplicateValidation {

	/**
	 * List<BjFrom>フィールドの要素内でduplicateチェッカー
	 * @return
	 */
	public static MessageData duplicateCheck(BjForm form, MessageData message) {

	
		List<List<? extends Object>> list = BjReflectUtil.getChildList(form);
		if (list != null) {
			for (List<? extends Object> lo : (List<List<? extends Object>>) list) {
				if (lo != null) {
					int seq = 1;
					Map<String,Map<String,String>> containsKeyMap = new HashMap<String,Map<String,String>>();
					
					for (Object o : (List<? extends Object>) lo) {
						if (o instanceof BjForm) {
							Map<String,Map<String,String[]>> map = BjAnnotationUtil.getBjDuplicateChk(o);
							
							Map<String,String[]> keyMap = null;
							//grupKeyNoでチェック連結文字列作成
							Iterator<String> ite = map.keySet().iterator();
							while (ite.hasNext()) {
								String key = (String) ite.next();
								if(! "@bjOne".equals(key)) {
									keyMap = BjDuplicateValidation.createKeyVal(map.get(key));
								} else {
									keyMap = map.get(key);
								}
								Iterator<String>iter = keyMap.keySet().iterator();
								while (iter.hasNext()) {
										String _key = (String)iter.next();
										String val[] = keyMap.get(_key);
										if(containsKeyMap.containsKey(_key)) {
									
												Map<String,String> containsMap = containsKeyMap.get(_key);
												if (containsMap.containsKey(val[0])) {
													//すでに登録されているのでエラー表示
													String param = seq + "行目の "  +  val[1];
													MessageEntity msgEntity = new MessageEntity(BjErrorCdConfig.BJ_DUPLICATE_ERROR, param);
													ArrayList<String> errorIdList = new ArrayList<String>();
													errorIdList.add(key);
													msgEntity.setItemIdList(errorIdList);
													message.add(msgEntity);
												} else {
													containsMap.put(val[0], val[1]);
												}
													
										} else {
											//登録されていないので要素put
											Map<String, String> containsNewMap = new HashMap<String, String>();
											 containsNewMap.put(val[0], val[1]);
											 containsKeyMap.put(_key, containsNewMap);
										}
								}
							}
							seq++;
						}
					}
				}
			}
		}
		return message;
	}
	
	/**
	 * グループNo毎のListから文字連結した結果を返します。
	 * @param Map<String,String[]> 項目ID、「項目の値、項目名称]
	 * @return
	 */
	private static Map<String,String[]> createKeyVal(Map<String, String[]> map) {

		StringBuffer idbuf = new StringBuffer("");
		StringBuffer valsbuf = new StringBuffer("");
		StringBuffer namesbuf = new StringBuffer("");
		//keyを作る際順番を保証する為昇順に。 
		
		List<String> list = new ArrayList<> (map.keySet());
		Collections.sort(list);
		Iterator<String> ite = list.iterator();
		int i = 0;
		while (ite.hasNext()) {
			String key = (String) ite.next();
			String[] val = map.get(key);
			if( val[0] == null ) {
				valsbuf.append("");
			}else {
				valsbuf.append(val[0]);
			}
			if(i > 0 ) {
				namesbuf.append(",");
			}
			i++;
			namesbuf.append(val[1]);
			idbuf.append(key);
		}
		String items[] = {valsbuf.toString(),namesbuf.toString()};
		Map<String,String[]>resultMap = new HashMap<String,String[]>();
		resultMap.put(idbuf.toString(),items );
		return resultMap;
	}
}
