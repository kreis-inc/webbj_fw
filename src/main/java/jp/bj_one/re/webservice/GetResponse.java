package jp.bj_one.re.webservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class GetResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	ErrorCode errorCode = ErrorCode.NO_ERROR;
	String message = "";
	
	public GetResponse() {}
	
	public GetResponse(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}

	public GetResponse(ErrorCode errorCode, String additionalMessage) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage() + additionalMessage != null ? additionalMessage : "";
	}

	public enum ErrorCode {
		/** 非エラー. */
		NO_ERROR("エラーではありません。"),
		
		/** C/S 通信用 DTO 不正. */
		REQUEST_IS_NULL("不正な呼出です。"),
		
		/** report が エラーコード . */
		REPORTID_ERROR("印刷する帳票はエラーになっています。"),

		/** DB 上に無い ReportId. */
		REPORT_ENTITY_NOT_FOUND("帳票が見つかりません。"),
		
		/** print がエラーになっている. */
		REPORT_PRINT_ERROR("帳票出力に失敗しています。"),
		
		/** まだ print が終わっていない. */
		REPORT_PRINT_NOT_COMPLETE("帳票出力中です。"),
		
		/** ファイルが見つからない. */
		REPORT_FILE_NOT_FOUND("作成された帳票が見つかりません。"),
		
		/** ファイル名が無い. */
		REPORT_FILENAME_EMPTY("作成された帳票にファイル名の指定が有りません。"),
		
		/** 一時ディレクトリの作成が出来ない. */
		TEMP_DIRECTORY_ERROR("ファイルアーカイブ用のディレクトリが用意できません。"),
		
		/** 帳票ファイル名が無い. */
		EMPTY_FILENAME("帳票ファイル名が無いため、アーカイブファイルの作成が出来ません。"),
		
		/** zip 作成失敗. */
		CREATE_ZIPFILE_ERROR("アーカイブファイルの作成が出来ません。");
		
		private String message;
		
		ErrorCode(String message) {
			this.message = message;
		}
		
		/** エラーの簡単な説明 */
		public String getMessage() {
			return "Report Engine Web Servive GET error : " + this.message;
		}
	}
	
    @JsonIgnore
	public boolean isError() {
		return this.errorCode == null || this.errorCode != ErrorCode.NO_ERROR;
	}
}
