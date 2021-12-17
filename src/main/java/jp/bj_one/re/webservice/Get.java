package jp.bj_one.re.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.bj_one.re.PrintStatus;
import jp.bj_one.re.ReportId;
import jp.bj_one.re.database.ManagementEntity;
import jp.bj_one.re.database.ManagementRepository;
import jp.bj_one.re.webservice.GetResponse.ErrorCode;
import jp.bj_one.re.webservice.data.ReportFile;

@RestController
@RequestMapping(value="re/", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
public class Get {
	static final int BUFFER_SIZE = 1024 * 32;
	
	@Autowired
	ManagementRepository repository;
	
	@Autowired
	ReportFile reportFile;
	
	@RequestMapping(value="get")
	public void get(@RequestBody GetRequest request, HttpServletResponse response) {
		// 入力チェック
		if (request == null || (request.reportId == null && (request.reportIdArray == null || request.reportIdArray.length < 1))) {
			errorResponse(response, ErrorCode.REQUEST_IS_NULL);
			return;
		}
		
		CheckReportResult checkReportResult;
		String[] filenames = null;
		if (request.reportId != null) {
			// 単一ファイル
			checkReportResult = checkReport(request.reportId);
		} else {
			// Zip ファイル
			filenames = new String[request.reportIdArray.length];
			checkReportResult = new CheckReportResult();
			checkReportResult.errorCode = ErrorCode.NO_ERROR;
			
			for (int i = 0; i < request.reportIdArray.length; i++) {
				CheckReportResult r = checkReport(request.reportIdArray[i]);
				if (r.errorCode != ErrorCode.NO_ERROR) {
					checkReportResult.errorCode = r.errorCode;
					break;
				}
				filenames[i] = uniqFilename(r.fileName, filenames);
			}
		}
		
		if (checkReportResult.errorCode != ErrorCode.NO_ERROR) {
			errorResponse(response, checkReportResult.errorCode);
			return;
		}

		// ダウンロード開始
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/force-download");
		if (request.reportId != null) {
			// 単一ファイル
	    	InputStream inputStream = null;
			try {
		    	byte[] buffer = new byte[BUFFER_SIZE];
				response.setContentLengthLong(checkReportResult.fileSize);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "filename*=UTF-8''" + URLEncoder.encode(checkReportResult.fileName, StandardCharsets.UTF_8.name()));
		    	inputStream = reportFile.read(request.reportId);
		        int readLength = 0;
		        while (0 < (readLength = inputStream.read(buffer)))
		        	response.getOutputStream().write(buffer, 0, readLength);
				response.getOutputStream().flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		} else {
			// Zip ファイル
			try (ZipFile zip = new ZipFile(response.getOutputStream())) {
				for (int i = 0; i < request.reportIdArray.length; i++) {
					zip.add(reportFile.read(request.reportIdArray[i]), filenames[i]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void errorResponse(HttpServletResponse response, ErrorCode errorCode) {
		byte[] body;
		try {
			String bodyString = (new ObjectMapper()).writeValueAsString(new GetResponse(errorCode));
			body = bodyString.getBytes("UTF-8");
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			body = new byte[0];
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setContentLength(body.length);
		try {
			response.getOutputStream().write(body);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class CheckReportResult {
		ErrorCode	errorCode;
		String fileName = null;
		long fileSize = -1;
	}
	
	private CheckReportResult checkReport(ReportId reportId) {
		CheckReportResult result = new CheckReportResult();
		
		if (reportId.isError()) {
			result.errorCode = ErrorCode.REPORTID_ERROR;
			return result;
		}
		
		// print 終了状態チェック
		if (!repository.existsById(reportId.getValue())) {
			result.errorCode = ErrorCode.REPORT_ENTITY_NOT_FOUND;
			return result;
		}
		
		ManagementEntity entity = repository.getOne(reportId.getValue());
		if (entity.getStatus() == null) {
			result.errorCode = ErrorCode.REPORT_ENTITY_NOT_FOUND;
			return result;
		}
		PrintStatus printStatus = entity.getStatus();
		if (printStatus.isError()) {
			result.errorCode = ErrorCode.REPORT_PRINT_ERROR;
			return result;
		}
		if (printStatus.isRunning()) {
			result.errorCode = ErrorCode.REPORT_PRINT_NOT_COMPLETE;
			return result;
		}
		if (entity.getFilename() == null || entity.getFilename().isEmpty()) {
			result.errorCode = ErrorCode.REPORT_FILENAME_EMPTY;
			return result;
		}
		result.fileName = entity.getFilename();
		
		// ファイル確認
		boolean fileExist = false;
		try {
			fileExist = reportFile.exist(reportId);
			result.fileSize = reportFile.size(reportId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!fileExist) {
			result.errorCode = ErrorCode.REPORT_FILE_NOT_FOUND;
			return result;
		}
		
		result.errorCode = ErrorCode.NO_ERROR;
		return result;
	}
	
	private String uniqFilename(String original, String[] checkArray) {
		final Locale locale = Locale.US;
		String result = original;
		int suffix = 1;
		String body;
		String ext;
		int exi = original.lastIndexOf('.');
		if (exi >= 0) {
			body = original.substring(0, exi);
			ext = original.substring(exi);
		} else {
			body = original;
			ext = "";
		}
		boolean uniq;
		do {
			uniq = true;
			for (String checkName : checkArray) {
				if (checkName != null) {
					if (result.toUpperCase(locale).equals(checkName.toUpperCase(locale))) {
						uniq = false;
						break;
					}
				}
			}
			if (!uniq)
				result = body + "(" + String.valueOf(suffix++) + ")" + ext;
		} while (!uniq);
		
		return result;
	}
}
