package jp.bj_one.re;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CreateReport Error (Report Engine Web Service API POST).
 * 負数の ReportId が帰ってきた時はエラー.
 * システム、application.properties、Report クラスの実装に問題が有る.
 */
public final class ReportId {
	final long value;
	
	/**
	 * プリミティブ（long）な数値で Report IDを設定する.
	 * @param value
	 */
	@JsonCreator
	public ReportId(@JsonProperty("value") long value) {
		this.value = value;
	}
	
	/**
	 * エラーコードで Report ID を設定する.
	 * @param code エラーコード
	 */
	public ReportId(ErrorCode code) {
		if (code == ErrorCode.NO_ERROR)
			code = ErrorCode.UNKNOWN_ERROR;
		value = code.getValue();
	}
	
	/**
	 * Report ID のプリミティブな数値を取得する.
	 * @return Report ID。
	 * <blockquote>エラーの値かもしれない。エラーの値かどうかは<code>isError()</code>や<code>getErrorCode()</code>で判定する</blockquote>
	 */
	public long getValue() {
		return value;
	}
	
	/**
	 * エラーを表す ReportID からエラーコードを取得する.
	 * エラーではない場合 noError を返す.
	 * @return エラーコード
	 */
	public ErrorCode getErrorCode() {
		if (this.isError()) {
			for (ErrorCode err : ErrorCode.values()) {
				if (err.getValue() == value)
					return err;
			}
			return ErrorCode.UNKNOWN_ERROR;
		}
		return ErrorCode.NO_ERROR;
	}
	
	/**
	 * エラーを表す ReportID かどうか判定する.
	 * @return エラーの場合は true
	 */
	public boolean isError() {
		return value < 0;
	}
	
	public static boolean isError(ReportId[] idList) {
		for (ReportId reportId : idList) {
			if (reportId.isError())
				return true;
		}
		return false;
	}
	
	/**
	 * toString() メソッドのオーバーライド.
	 * @return Report ID の場合は値（文字列化）、エラーの場合はエラーの enum name
	 */
	@Override
	public String toString() {
		if (this.isError())
			return this.getErrorCode().name();
		else
			return String.valueOf(getValue());
	}
	
	/**
	 * エラーコード.
	 * <blockquote>負数の ReportId はエラー</blockquote>
	 * <code>getMessage()</code>でデバッグ用の説明を取得可能
	 */
	public enum ErrorCode {
		/** 非エラー. */
		NO_ERROR(0, "エラーではありません。"),
		
		/** 未定義エラー. */
		UNKNOWN_ERROR(-1, "定義外のエラーです。"),
		
		/** Client : Report データ無し. */
		CLIENT_EMPTY_REPORT(-100, "クライアント：帳票データが有りません。"),
		
		/** Client : Report データ異常. */
		CLIENT_REPORT_MAPPING_ERROR(-110, "クライアント：帳票データが異常です。"),
		
		/** Client : URISyntaxException. */
		CLIENT_URI_ERROR(-120, "クライアント：帳票サーバー接続先設定が異常です。"),

		/** Client : サーバーからの戻り値がおかしい. */
		CLIENT_RESPONSE_ERROR(-130, "クライアント：帳票サーバーが異常です。"),

		
		/** Server : C/S 通信用 DTO 不正. */
		SERVER_REQUEST_IS_NULL(-200, "サーバー：不正な呼出です。"),
		
		/** Server : report が null または 0 件 . */
		SERVER_EMPTY_REPORT(-201, "サーバー：印刷する帳票指定が有りませんでした。"),

		/** Server : print 出力先ディレクトリが作成できない. */
		SERVER_REPORT_DIR_ERROR(-210, "サーバー：帳票の出力先ディレクトリの作成に失敗しました。"),
		
		/** Server : print 開始前キャンセル.. */
		SERVER_PRINT_DOES_NOT_START(-211, "サーバー：他の帳票でエラーが発生したため印刷できませんでした。"),
		
		/** Server : reportData.className が無い. */
		SERVER_EMPTY_CLASS_NAME(-220, "サーバー：不正な呼出です (Empty Class)。"),
		
		/** Server : reportData.dataJson が無い. */
		SERVER_ENPTY_DATA_JSON(-221, "サーバー：不正な呼出です (Empty data)。"),
		
		/** Server : reportData.className のクラスが見つからない. */
		SERVER_REPORT_CLASS_NOT_FOUND(-223, "サーバー：指定の帳票情報が見つかりません(1)。"),
		
		/** Server : reportData.className のクラスが null で取得された. */
		SERVER_REPORT_CLASS_IS_NULL(-224, "サーバー：指定の帳票情報が見つかりません(2)。"),
		
		/** Server : reportData.dataJson がマッピングできない. */
		SERVER_JSON_DATA_DECODE_ERROR(-225, "サーバー：帳票の情報が不正です(1)。"),
		
		/** Server : report が復元できない. */
		SERVER_REPORT_MAPPING_ERROR(-226, "サーバー：帳票の情報が不正です(2)。"),
		
		/** Server : report が復元できない. */
		SERVER_DATABASE_ERROR(-227, "サーバー：帳票データベースへのアクセスに失敗しました。"),
		
		/** Server : print() が Exception. */
		SERVER_PRINT_ERROR(-230, "サーバー：帳票作成の投入に失敗しました。");
		
		private long errorCode;
		private String message;
		
		ErrorCode(int errorCode, String message) {
			this.errorCode = errorCode;
			this.message = message;
		}
		
		/** エラー時に API から返すエラーのプリミティブな数値. */
		public Long getValue() {
			return this.errorCode;
		}
		
		/** エラーの簡単な説明 */
		public String getMessage() {
			return "Report Engine Web Servive POST error : " + this.message;
		}
	}
}
