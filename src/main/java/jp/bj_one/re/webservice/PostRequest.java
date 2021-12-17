package jp.bj_one.re.webservice;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jp.bj_one.re.webservice.data.PostReportDto;
import jp.bj_one.re.webservice.data.PostUserInfo;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class PostRequest implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	PostReportDto[] reports;
	String postGroupName;
	String postGroupMessage;
	PostUserInfo userInfo;
}
