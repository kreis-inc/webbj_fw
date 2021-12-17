package jp.bj_one.re.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

public class DownloadUtil {
	static final int BUFFER_SIZE = 1024;
	
	/**
	 * ダウンロード処理.
	 * @param response HttpServletResponse
	 * @param inputStream ダウンロードさせるファイルのストリーム
	 * @param filename ダウンロード時に付与されるファイル名
	 * @return 成功時 true
	 */
	public static boolean download(HttpServletResponse response, InputStream inputStream, String filename) {
		boolean result = false;
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/force-download");
		try {
//			response.setContentLengthLong(Files.size(file)); ストリームなのでサイズは不明
			if (filename != null && !filename.isEmpty())
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; " +
						"filename=\"" + filename + "\"; " +
						"filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
	        byte[] buf = new byte[BUFFER_SIZE];
	        int n;
	        while ((n = inputStream.read(buf)) > 0) {
	        	response.getOutputStream().write(buf, 0, n);
	        }
			response.getOutputStream().flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
