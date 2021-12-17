package jp.bj_one.re.webservice.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.bj_one.re.ReportId;

@Service
public class ReportFile {
	@Value("${jp.bj_one.re.reportDirectory}")
	private String reportDirectory;

	public void createDirectory() throws IOException {
		Files.createDirectories(Paths.get(reportDirectory));
	}
	
	public boolean exist(ReportId reportId) throws IOException {
		return Files.exists(getPath(reportId));
	}
	
	public long size(ReportId reportId) throws IOException {
		return Files.size(getPath(reportId));
	}
	
	public OutputStream create(ReportId reportId) throws IOException {
		return Files.newOutputStream(getPath(reportId));
	}
	
	public long copy(ReportId reportId, OutputStream outputStream) throws IOException {
		return Files.copy(getPath(reportId), outputStream);
	}
	
	public Path getPath(ReportId reportId) throws IOException {
		return Paths.get(reportDirectory, reportId.toString());
	}
	
	public InputStream read(ReportId reportId) throws IOException {
		return Files.newInputStream(getPath(reportId));
	}
}
