package jp.bj_one.re.service;

import java.io.InputStream;
import java.io.Serializable;
import jp.bj_one.re.webservice.GetResponse;
import lombok.Data;

@Data
public class GetReportResult implements Serializable {
	private static final long serialVersionUID = 1L;

	GetResponse response = null;
	InputStream stream = null;
	String filename;
	
	public boolean isError() {
		return response == null || response.isError() || stream == null;
	}
}
