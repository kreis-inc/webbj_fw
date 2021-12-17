package jp.bj_one.re.webservice.data;

import java.io.Serializable;

import lombok.Data;

@Data
public class PostReportDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String className;
	String dataJson;
}
