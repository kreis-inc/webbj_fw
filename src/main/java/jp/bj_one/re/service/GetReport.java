package jp.bj_one.re.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.bj_one.re.ReportId;
import jp.bj_one.re.webservice.GetRequest;
import jp.bj_one.re.webservice.GetResponse;
import jp.bj_one.re.webservice.GetResponse.ErrorCode;

@Service
class GetReport {
	static final String RE_PREFIX = "BJONE_RE_";
	
	@Value("${jp.bj_one.re.reportServer}")
	private String reportServer;
	
	/**
	 * ストリーム返却.
	 * @param reportId
	 * @return
	 */
	GetReportResult getReport(ReportId reportId, ReportId[] reportIdArray) {
		GetReportResult result = new GetReportResult();
		GetRequest dto = new GetRequest();
		if (reportId != null)
			dto.setReportId(reportId);
		if (reportIdArray != null)
			dto.setReportIdArray(reportIdArray);
		
		RestTemplate restTemplate = new RestTemplate();
		RequestEntity<GetRequest> requestEntity = null;
		try {
			requestEntity = RequestEntity
			        .post(new URI(reportServer + "get"))
			        .accept(MediaType.APPLICATION_JSON)
			        .body(dto);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return result;
		}

		ResponseEntity<Resource> responseEntity = restTemplate.exchange(requestEntity, Resource.class);
		
	 	MediaType contentType = responseEntity.getHeaders().getContentType();
		if (contentType.equals(MediaType.APPLICATION_JSON) || contentType.equals(MediaType.APPLICATION_JSON_UTF8)) {
			try {
				result.setResponse((new ObjectMapper()).readValue(responseEntity.getBody().getInputStream(), GetResponse.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		if (reportId != null) {
//          springframework 5 の場合は上記の替わりにで行ける
//			result.setFilename(responseEntity.getHeaders().getContentDisposition().getFilename().replace('+', ' '));
			result.filename = getFilename(responseEntity.getHeaders().getValuesAsList(HttpHeaders.CONTENT_DISPOSITION));
		}
		try {
			result.stream = responseEntity.getBody().getInputStream();
			result.response = new GetResponse(ErrorCode.NO_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	private String getFilename(List<String> contentDisposition) {
		final String filenameNew = "filename*=UTF-8''";
		final String filenameOld = "filename=";
		
		for (String s : contentDisposition) {
			int n = s.indexOf(filenameNew);
			if (n >= 0) {
				String ns = s.substring(n + filenameNew.length());
				n = ns.indexOf(';');
				if (n >= 0)
					ns = ns.substring(0, n);
				ns = ns.trim();
				if (!ns.isEmpty()) {
					try {
						return URLDecoder.decode(ns, StandardCharsets.UTF_8.name()).replace('+', ' ');
					} catch (UnsupportedEncodingException e) {
					}
				}
			}
			
			int o = s.indexOf(filenameOld);
			if (o >= 0) {
				return deQuote(s.substring(o));
			}
		}
		return "";
	}
	
	private String deQuote(String s) {
		int i = s.indexOf('\'');
		int j = s.indexOf('"');
		if (i >= 0 && (j < 0 || i < j)) {
			s = s.substring(i);
			int k = s.indexOf('\'');
			if (k >= 0)
				return s.substring(0, k);
		} else if (j >= 0) {
			s = s.substring(j);
			int k = s.indexOf('"');
			if (k >= 0)
				return s.substring(0, k);
		}
		return "";
	}
}
