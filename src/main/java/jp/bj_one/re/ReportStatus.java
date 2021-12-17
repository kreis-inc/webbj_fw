package jp.bj_one.re;

import java.time.LocalDateTime;

import jp.bj_one.re.database.ManagementEntity;
import lombok.Data;

@Data
public class ReportStatus {
	/** 帳票ID. */
	ReportId id;
	
	/** 実行ステータス. */
	PrintStatus status;
	
	/** 帳票名1.("日時売上"等) */
	String reportName;
	
	/** 帳票名2.("2019/2/28"等) */
	String reportSubName;
	
	/** 帳票メッセージ.用途は特定していない. */
	String reportMessage;
	
	/** 帳票投入単位毎 ID. */
	Long postGroupId;
	
	/** 帳票投入単位毎 名前. */
	String postGroupName;

	/** 帳票投入単位毎 メッセージ. */
	String postGroupMessage;

	/** 帳票ダウンロード時のファイル名. */
	String filename;
	
	/** 帳票ファイルサイズ. */
	Long filesize;
	
	/** 削除フラグ. */
	Boolean deleteFlg;
	
	/** 【MCPVC固有】内部番号. */
	Long internalNumber;
	
	/** 【MCPVC固有】受注番号. */
	String salesOrderNo;
	
	/** 帳票投入日時. */
	LocalDateTime postDateTime;
	
	/** 帳票作成開始日時. */
	LocalDateTime printDateTime;
	
	/** 帳票作成完了（エラー終了含む）日時. */
	LocalDateTime endedDateTime;
	
	String userId;
	String bjSystemId;
	String loginUserName;
	
	public ReportStatus() {}
	
	public ReportStatus(ManagementEntity dbEntity) {
		this.id = new ReportId(dbEntity.getId());
		this.status = dbEntity.getStatus();
		this.reportName = dbEntity.getReportName();
		this.reportSubName = dbEntity.getReportSubName();
		this.reportMessage = dbEntity.getReportMessage();
		this.postGroupId = dbEntity.getPostGroupId();
		this.postGroupName = dbEntity.getPostGroupName();
		this.postGroupMessage = dbEntity.getPostGroupMessage();
		this.filename = dbEntity.getFilename();
		this.filesize = dbEntity.getFilesize();
		this.deleteFlg = dbEntity.getDeleteFlg();
		this.internalNumber = dbEntity.getInternalNumber();
		this.salesOrderNo = dbEntity.getSalesOrderNo();
		this.postDateTime = dbEntity.getPostDateTime();
		this.printDateTime = dbEntity.getPrintDateTime();
		this.endedDateTime = dbEntity.getEndedDateTime();
		this.userId = dbEntity.getUserId();
		this.bjSystemId = dbEntity.getBjSystemId();
		this.loginUserName = dbEntity.getLoginUserName();
	}
	
	
	/**
	 * 実行中のステータスかどうか.
	 * @return true:実行中 (not 実行完了)
	 */
	public boolean isRunning() {
		return status.isRunning();
	}
	
	public static boolean isRunning(ReportStatus[] statusList) {
		for (ReportStatus reportStatus : statusList) {
			if (reportStatus.isRunning())
				return true;
		}
		return false;
	}
	
	/**
	 * 実行完了したステータスかどうか.
	 * @return true:実行完了.（正常終了・エラー終了両方を含む）
	 */
	public boolean isEnded() {
		return status.isEnded();
	}
	
	public static boolean isEnded(ReportStatus[] statusList) {
		return !isRunning(statusList);
	}
	
	/**
	 * 正常終了したステータスかどうか.
	 * @return true:正常終了している.
	 */
	public boolean isComplete() {
		return status.isComplete();
	}
	
	public static boolean isComplete(ReportStatus[] statusList) {
		return !isError(statusList);
	}
	
	/**
	 * エラー終了したステータスかどうか.
	 * @return true:エラー終了している
	 */
	public boolean isError() {
		return status.isError();
	}
	
	public static boolean isError(ReportStatus[] statusList) {
		for (ReportStatus reportStatus : statusList) {
			if (reportStatus.isError())
				return true;
		}
		return false;
	}
}
