package jp.bj_one.re.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.bj_one.re.Report;
import jp.bj_one.re.ReportId;
import jp.bj_one.re.webservice.PostRequest;
import jp.bj_one.re.webservice.data.PostReportDto;
import jp.bj_one.re.webservice.data.PostUserInfo;

@Service
public class CreateReport {
	@Value("${jp.bj_one.re.reportServer}")
	private String reportServer;

	/**
	 * 帳票作成投入（帳票非同期作成）.
	 * 
	 * @param reports 投入する帳票の配列
	 * @return 投入された帳票の帳票IDの配列
	 */
	public ReportId[] createReportASync(Report[] reports, PostUserInfo userInfo, String postGroupName,
			String postGroupMessage) {
		ReportId[] result = new ReportId[1];
		if (reports == null || reports.length < 1) {
			result[0] = new ReportId(ReportId.ErrorCode.CLIENT_EMPTY_REPORT);
			return result;
		}

		PostRequest requestData = new PostRequest();
		requestData.setReports(new PostReportDto[reports.length]);
		int i = 0;
		for (Report report : reports) {
			requestData.getReports()[i] = new PostReportDto();
			requestData.getReports()[i].setClassName(report.getClass().getName());
			try {
				requestData.getReports()[i].setDataJson((new ObjectMapper()).writeValueAsString(report));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				result[0] = new ReportId(ReportId.ErrorCode.CLIENT_REPORT_MAPPING_ERROR);
				return result;
			}
			i++;
		}

		requestData.setPostGroupName(postGroupName != null ? postGroupName : "");
		requestData.setPostGroupMessage(postGroupMessage != null ? postGroupMessage : "");
		if (userInfo == null)
			userInfo = new PostUserInfo();
		requestData.setUserInfo(userInfo);

		RequestEntity<PostRequest> request = null;
		try {
			request = RequestEntity.post(new URI(reportServer + "post")).accept(MediaType.APPLICATION_JSON)
					.body(requestData);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			result[0] = new ReportId(ReportId.ErrorCode.CLIENT_URI_ERROR);
			return result;
		}

		ResponseEntity<ReportId[]> response = (new RestTemplate()).exchange(request, ReportId[].class);

		return response.getBody();
	}
}
