package jp.bj_one.fw.common.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jp.bj_one.fw.common.BjReflectUtil;
import jp.bj_one.fw.common.MessageData;
import jp.bj_one.fw.common.annotation.BjAnnotationUtil;
import jp.bj_one.fw.entity.MessageEntity;
import jp.bj_one.fw.form.BjForm;

/**
 * アノテーションの'@BjKey'の汎用チェッククラス
 * @author kreis
 *
 */
public class BjkeyVaridation {

	/**
	 * Object内から '@BjKey’のフィールドがNULLか判断行いMessageDataを作成するメソッド
	 * Form内フィールド内のcollection無いフィールドに関しては
	 * java.util.Listのみ対応。
	 * 
	 * @param checkObject BjForm チェックを行うBjForm
	 * @param msg MessageData
	 * @param count グリットやダイナパネルでのListチェックの際のカウントナンバー
	 * @return MessageData
	 * 
	 */
	public static MessageData bjKeyCheker(BjForm form,MessageData message) {
		return BjkeyVaridation.bjKeyCheker((BjForm)form, message,0);
	}
	
	/**
	 * Object内から '@BjKey’のフィールドがNULLか判断行いMessageDataを作成するメソッド
	 * Form内フィールドのjava.util.Listにも対応。
	 * 
	 * @param checkObject BjForm チェックを行うBjForm
	 * @param msg MessageData
	 * @param count グリットやダイナパネルでのListチェックの際のカウントナンバー
	 * @return MessageData
	 * 
	 */
	private static MessageData bjKeyCheker(BjForm form,MessageData message,int count) {
		
		HashMap<String, String[]> map = BjAnnotationUtil.getBjKeyMap(form);
		Iterator<String> ite = map.keySet().iterator();
		while (ite.hasNext()) {
			String key = (String) ite.next();
			String[] val = map.get(key);
			if (val[0] == null || "".equals(val[0])) {
				//エラー
				String param;
				if( count == 0) {
					param = val[1] ;
				}else {
					param = val[1] + "-" + count;
				}
				 
			    MessageEntity msgEntity = new MessageEntity(BjErrorCdConfig.BJ_KEY_ERROR,param);
				ArrayList<String> list = new ArrayList<String>();
				list.add(key);
				msgEntity.setItemIdList(list);
				message.add(msgEntity);
			}
		}
		//Listの検索
		//Objectの中にListのfiledがあるかチェック。
		List<List<? extends Object>> list = BjReflectUtil.getChildList(form);
		if(list != null) {
			int seq = 1;
			for(List<? extends Object> lo : (List<List<? extends Object>>)list){
				if(lo!=null) {
					for(Object o : (List<? extends Object>)lo) {
					 if(o instanceof BjForm) {
						 BjkeyVaridation.bjKeyCheker((BjForm)o, message,seq);
					 }
					 seq++;
					}
				}
				
			 }
		}
		return message;
	}

	/**
	 * BjForm内の@BjKeyアノテーションが付与されている項目のNULLチェックの判断を行うメソッド
	 * Form内フィールド内のcollection無いフィールドに関しては
	 * java.util.Listのみ対応。
	 * 
	 * @param o
	 * @return true Key項目NULL
	 */
	public static boolean isNullKey(BjForm fo) {

		HashMap<String,String[]> map = BjAnnotationUtil.getBjKeyMap(fo);
		for(Iterator<String[]> iterator = map.values().iterator(); iterator.hasNext();) {
			String[] value = iterator.next();
			if(value[0] == null || "".equals(value[0])) {
				return true;
			}
		}
		//Listの検索
				//Objectの中にListのfiledがあるかチェック。
				List<List<? extends Object>> list = BjReflectUtil.getChildList(fo);
				if(list!=null) {
					
					for(List<? extends Object> lo : (List<List<? extends Object>>)list){
						if(lo!=null) {
							for(Object o : (List<? extends Object>)lo) {
							 if(o instanceof BjForm) {
								 BjkeyVaridation.isNullKey((BjForm)fo);
							 }
							
							}
						}
						
					 }
		
				}
		return false;
	
				
	}
}
