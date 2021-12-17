package jp.bj_one.re;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReportInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/*****************************
	 * 帳票情報
	 *****************************/
	
	/**
	 * 帳票ID.<br>
	 * Report Engine 帳票サーバーで print メソッド実行時前に設定される.
	 * 帳票ファイル毎のユニーク ID.（サーバーエラーコード兼用）
	 * print メソッド内での変更は無効.
	 */
	ReportId id;
	
	/**
	 * 帳票名1.<br>
	 * 出力帳票一覧（帳票ステータス）に入る帳票名。省略時はクラス名が入る。<br>
	 * print メソッド内での変更は有効.
	 */
	String reportName;
	public String getReportName() {
		return reportName != null ? reportName : "";
	}
	
	/**
	 * 帳票名2.<br>
	 * 出力帳票一覧（帳票ステータス）に入る帳票名.<br>
	 * print メソッド内での変更は有効.
	 */
	String reportSubName;
	public String getReportSubName() {
		return reportSubName != null ? reportSubName : "";
	}
	
	/**
	 * 帳票毎メッセージ.<br>
	 * print メソッド内での変更は有効.
	 * 主にエラーメッセージ用だが、正常時にもステータスに反映する.
	 */
	String reportMessage;
	public String getReportMessage() {
		return reportMessage != null ? reportMessage : "";
	}
	
	/**
	 * 帳票投入単位毎 ID.<br>
	 * 投入した最初の帳票 ID になる.
	 * print メソッド内での変更は無効.
	 */
	Long postGroupId;
	
	/**
	 * 帳票投入単位毎 名前.
	 * 投入（）時は Report.postGroupName の方が有効になる.
	 * print メソッド内での変更は有効.同一帳票投入単位全ての名前を更新する.
	 */
	String postGroupName;
	public String getPostGroupName() {
		return postGroupName != null ? postGroupName : "";
	}
	
	/**
	 * 帳票投入単位毎 メッセージ.用途は特定していない.
	 * 投入（）時は Report.postGroupMessage の方が有効になる.
	 * print メソッド内での変更は有効.同一帳票投入単位全てのメッセージを更新する.
	 */
	String postGroupMessage;
	public String getPostGroupMessage() {
		return postGroupMessage != null ? postGroupMessage : "";
	}
	
	/**
	 * print メソッドが書き出したファイルをダウンロードするときに付与するファイル名.<br>
	 * print メソッド内での変更は有効.
	 * ファイルを書き出さなかった場合は setFilename しなくて OK.
	 */
	String filename;
	public String getFilename() {
		return filename != null ? filename : "";
	}
	
	/**
	 * 【MCPVC固有】内部番号.
	 * print メソッド内での変更は有効.
	 */
	Long internalNumber;
	
	/**
	 * 【MCPVC固有】受注番号.
	 * print メソッド内での変更は有効.
	 */
	String salesOrderNo;


	/*****************************
	 * print メソッド戻り値
	 *****************************/
	
	/**
	 * print メソッドが正常終了した場合は true.
	 */
	boolean success;


	/*****************************
	 * ExEnbed パラメタ 
	 *****************************/
	
	/**
	 * テンプレートファイル名.<br>
	 * null または空文字の場合は Report の "クラス名.xlsx"、" クラス名.xlsm" を検索する。
	 */
	String templateFilename = null;
	
	/**
	 * ページ行数.<br>
	 * 0 の場合はページ送り（シートの複製）を行わない.
	 */
	public int pageLines = 0;
}
