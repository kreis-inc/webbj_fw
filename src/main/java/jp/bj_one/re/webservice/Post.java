package jp.bj_one.re.webservice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.bj_one.re.PrintStatus;
import jp.bj_one.re.Report;
import jp.bj_one.re.ReportId;
import jp.bj_one.re.ReportId.ErrorCode;
import jp.bj_one.re.ReportInfo;
import jp.bj_one.re.database.ManagementEntity;
import jp.bj_one.re.database.ManagementRepository;
import jp.bj_one.re.webservice.data.PostReportDto;
import jp.bj_one.re.webservice.data.PostUserInfo;
import jp.bj_one.re.webservice.data.ReportFile;

@RestController
@RequestMapping(value="re/", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
public class Post {
	@Autowired
	ManagementRepository repository;
	
	@Autowired
	ReportFile reportFile;
	
	@Autowired
	AsyncPrint asyncPrint;
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="post")
	public ReportId[] post(@RequestBody PostRequest request) {
		ReportId[] result = new ReportId[1];
		
		// 入力値チェック
		if (request == null) {
			result[0] = new ReportId(ErrorCode.SERVER_REQUEST_IS_NULL); 
			return result;
		}
		if (request.getReports() == null || request.getReports().length < 1) {
			result[0] = new ReportId(ErrorCode.SERVER_EMPTY_REPORT); 
			return result;
		}
		if (request.getUserInfo() == null)
			request.setUserInfo(new PostUserInfo());
		
		// print 出力先ディレクトリ作成
		try {
			reportFile.createDirectory();
		} catch (IOException e) {
			e.printStackTrace();
			result[0] = new ReportId(ErrorCode.SERVER_REPORT_DIR_ERROR); 
			return result;
		}
		
		// 戻り値を report 数分に拡張、すべて印刷キャンセルにしておく
		result = new ReportId[request.getReports().length];
		for (int i = 0; i < result.length; i++)
			result[i] = new ReportId(ErrorCode.SERVER_PRINT_DOES_NOT_START); 

		// 帳票毎の印刷
		List<PrintResult> printResultList = new ArrayList<PrintResult>();
		long postGroupId = -1;
		for (int i = 0; i < result.length; i++) {
			PrintResult printResult = print(request.getReports()[i], postGroupId, request.postGroupName, request.postGroupMessage, request.getUserInfo());
			result[i] = printResult.reportId;
			if (printResult.reportId.isError())
				return result;
			if (postGroupId < 0)
				postGroupId = printResult.reportId.getValue();
			printResultList.add(printResult);
		}
		for (PrintResult printResult : printResultList) {
			// 非同期 print 実行
			asyncPrint.print(printResult.report, printResult.entity);
		}
		
		return result;
	}
	
	
	private class PrintResult {
		public final ReportId				reportId;
		public final jp.bj_one.re.Report	report;
		public final ManagementEntity		entity;
		
		private PrintResult(long reportId, jp.bj_one.re.Report	report, ManagementEntity entity) {
			this.reportId = new ReportId(reportId);
			this.report = report;
			this.entity = entity;
		}

		public PrintResult(ErrorCode errorCode) {
			this.reportId = new ReportId(errorCode);
			this.report = null;
			this.entity = null;
		}
	}
	
	private PrintResult print(PostReportDto reportDto, long postGroupId, String postGroupName, String postGroupMessage, PostUserInfo userInfo) {
		// 入力データチェック
		if (reportDto.getClassName() == null || reportDto.getClassName().isEmpty())
			return new PrintResult(ErrorCode.SERVER_EMPTY_CLASS_NAME);
		if (reportDto.getDataJson() == null || reportDto.getDataJson().isEmpty())
			return new PrintResult(ErrorCode.SERVER_ENPTY_DATA_JSON);
		
		// 呼出元帳票クラス取得
		jp.bj_one.re.Report targetReport = null;
		Class<?> reportClass = null;
		try {
			ClassLoader classLoader = Report.class.getClassLoader();
			reportClass = classLoader.loadClass(reportDto.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return new PrintResult(ErrorCode.SERVER_REPORT_CLASS_NOT_FOUND);
		}
		if (reportClass == null)
			return new PrintResult(ErrorCode.SERVER_REPORT_CLASS_IS_NULL);
		
		// 呼出元帳票インスタンス復元
		Object fromJsonData = null;
		try {
			fromJsonData = ((new ObjectMapper()).readValue(reportDto.getDataJson(), reportClass));
		} catch (IOException e) {
			e.printStackTrace();
			return new PrintResult(ErrorCode.SERVER_JSON_DATA_DECODE_ERROR);
		}
		
		if (fromJsonData == null || !(fromJsonData instanceof jp.bj_one.re.Report))
			return new PrintResult(ErrorCode.SERVER_REPORT_MAPPING_ERROR);
		
		targetReport = (jp.bj_one.re.Report)fromJsonData;
		
		// print 実行準備
		ManagementEntity entity = new ManagementEntity();
		ReportInfo info = targetReport.getInfo();
		entity.setStatus(PrintStatus.AWAITING);
		if (info.getReportName() == null || info.getReportName().isEmpty())
			entity.setReportName(targetReport.getClass().getSimpleName());
		else
			entity.setReportName(info.getReportName());
		entity.setReportSubName(info.getReportSubName());
		entity.setReportMessage(info.getReportMessage());
		if (info.getInternalNumber() != null)
			entity.setInternalNumber(info.getInternalNumber());
		if (info.getSalesOrderNo() != null)
			entity.setSalesOrderNo(info.getSalesOrderNo());
		if (postGroupId >= 0)
			entity.setPostGroupId(postGroupId);
		entity.setPostGroupName(postGroupName != null ? postGroupName : "");
		//entity.setPostGroupMessage(postGroupMessage != null ? postGroupMessage : "");
		entity.setPostGroupMessage(info.getPostGroupMessage());
		entity.setPostDateTime(LocalDateTime.now());
		entity.setUserId(userInfo.getUserId());
		entity.setBjSystemId(userInfo.getBjSystemId());
		entity.setLoginUserName(userInfo.getLoginUserName());
		try {
			repository.save(entity);
			repository.flush();
			if (postGroupId < 0) {
				entity.setPostGroupId(entity.getId());
				repository.save(entity);
				repository.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new PrintResult(ErrorCode.SERVER_DATABASE_ERROR);
		}

		long reportId = entity.getId();
		targetReport.getInfo().setId(new ReportId(reportId));
		
		return new PrintResult(reportId, targetReport, entity);
	}
}
