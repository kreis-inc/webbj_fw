package jp.bj_one.re.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.bj_one.re.Report;
import jp.bj_one.re.ReportId;
import jp.bj_one.re.ReportStatus;
import jp.bj_one.re.webservice.data.PostUserInfo;

@Service
public class ReportEngine {
	@Autowired
	private CreateReport createReport;
	
	@Autowired
	private GetStatus getStatus;
	
	@Autowired
	private GetReport getReport;

	@Autowired
	private ListReport listReport;
	
	@Autowired
	private PostGroupId postGroupId;
	
	
	/*****************************
	 * 作成投入 非同期
	 *****************************/
	
	/**
	 * 帳票作成投入（帳票非同期作成）.
	 * @param report 投入する帳票
	 * @return 投入された帳票の帳票ID
	 */
	
	/**
	 * 帳票作成投入（帳票非同期作成）.
	 * @param report 投入する帳票のリスト
	 * @return 投入された帳票の帳票IDの配列
	 */
	public ReportId createReportASync(List<Report> reportList, PostUserInfo userInfo, String postGroupName, String postGroupMessage) {
		if (reportList == null || reportList.size() < 1) {
			return null;
		}
		return getPostGroup(createReport.createReportASync(reportList.toArray(new Report[reportList.size()]), userInfo, postGroupName, postGroupMessage));
	}
	
	/**
	 * 帳票作成投入（帳票非同期作成）.
	 * @param report 投入する帳票の配列
	 * @return 投入された帳票の帳票IDの配列
	 */
	public ReportId createReportASync(Report[] reports, PostUserInfo userInfo, String postGroupName, String postGroupMessage) {
		return getPostGroup(createReport.createReportASync(reports, userInfo, postGroupName, postGroupMessage));
	}
	

	/*****************************
	 * 作成投入 同期
	 *****************************/
	
	private static final long delay = 500;
	
	/**
	 * 帳票作成投入（帳票同期作成）.
	 * @param report 投入する帳票
	 * @return 投入された帳票の帳票ID
	 */
	public ReportId createReport(List<Report> reportList, PostUserInfo userInfo, String postGroupName, String postGroupMessage) {
		if (reportList == null) {
			return null;
		}
		
		return createReport(reportList.toArray(new Report[reportList.size()]), userInfo, postGroupName, postGroupMessage);
	}
	
	public ReportId createReport(Report[] reports, PostUserInfo userInfo, String postGroupName, String postGroupMessage) {
		ReportId[] idList = createReport.createReportASync(reports, userInfo, postGroupName, postGroupMessage);
		if (ReportId.isError(idList))
			return getPostGroup(idList);
		
		do {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		} while (getStatus.isRunning(idList));
		
		return getPostGroup(idList);
	}
	
	
	
	/*****************************
	 * 状態確認
	 *****************************/
	
	/**
	 * 
	 * @param reportId
	 * @return
	 */
	public ReportStatus getStatus(ReportId reportId) {
		return getStatus.getStatus(reportId);
	}

	public ReportStatus[] getStatus(ReportId[] idList) {
		return getStatus.getStatus(idList);
	}

	
	/*****************************
	 * 一覧取得
	 *****************************/
	
	/**
	 * 
	 * @param start
	 * @return
	 */
	public List<ReportStatus> listReportByPostAfter(LocalDateTime start) {
		return listReport.listReportByPostAfter(start);
	}
	

	/*****************************
	 * 帳票取得
	 *****************************/
	
	/**
	 * 単一帳票取得.
	 * @param reportId
	 * @return
	 */
	public GetReportResult getReport(ReportId reportId) {
		return getReport.getReport(reportId, null);
	}
	
	/**
	 * 投入グループ毎の帳票 Zip ファイル取得.
	 * @param groupId
	 * @return
	 */
	public GetReportResult getPostGroupReport(ReportId groupId) {
		return getReport.getReport(null, getPostGroupReports(groupId));
	}
	
	/**
	 * 複数帳票 Zip ファイル取得.
	 * @param reportId
	 * @return
	 */
	public GetReportResult getReport(ReportId[] reportId) {
		return getReport.getReport(null, reportId);
	}

	
	/*****************************
	 * 投入グループ・帳票ID 変換
	 *****************************/
	public ReportId getPostGroup(ReportId[] reportIds) {
		return postGroupId.getPostGroup(reportIds);
	}
	
	public ReportId[] getPostGroupReports(ReportId groupId) {
		return postGroupId.getPostGroupReports(groupId);
	}
}
