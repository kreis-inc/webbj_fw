package jp.bj_one.re.webservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jp.bj_one.re.ReportId;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class GetRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 単一ファイルダウンロード用. */
	ReportId reportId;
	
	/** Zip ファイルダウンロード用. */
	ReportId[] reportIdArray;
}
