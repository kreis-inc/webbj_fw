package jp.bj_one.fw.common.validate;

public class BjErrorCdConfig {
	
	/** 
	 * '@BjKey' アノテーション用エラーメッセージコード 
	 *  {0}が入力されていません。
	 * */
	public static final String BJ_KEY_ERROR = "LE_BJ03";
	
	/**
	 * '@BJDbCheck'アノテーション用エラーメッセージコード
	 *  LE_BJ02={0}は登録されていない値です
	 */
	public static final String BJ_DBCHECK_ERROR = "LE_BJ02"; 
	
	/**
	 * duplicateエラー用エラーメッセージ
	 * {0}は重複した入力値です。
	 */
	public static final String BJ_DUPLICATE_ERROR= "LE_BJ04";
	
	/**
	 * IDのフォーマットエラー用メッセージ
	 * LE_PVC02={0}は、大文字・半角・英数字とハイフン"-"、 アンダースコア "_"のみで入力して下さい。 
	 */
	public static final String PVC_IDFORMAT_ERROR = "LE_PVC02";
			
	
	

}
