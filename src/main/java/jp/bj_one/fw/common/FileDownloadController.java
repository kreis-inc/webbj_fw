package jp.bj_one.fw.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//import jp.bj_one.fw.BjOneControllerInterceptor;

@Controller
public class FileDownloadController {
  Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

  @Value("${bj.out.uploadFileDir}")
  private String uploadPath;

  /**
   * ファイルダウンロード処理。
   */
  @RequestMapping(value = "/download", method = RequestMethod.POST)
  public void download(@RequestParam("uid") String uid, HttpServletResponse response) throws IOException {
    File file = new File(uploadPath + "/" + uid);
    try {
      BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
      BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

      response.setContentType("application/octet-stream");

      byte[] bf = new byte[1024];
      int len;
      while ((len = inStream.read(bf, 0, bf.length)) > 0) {
        outStream.write(bf, 0, len);
      }
      inStream.close();
      outStream.close();
    } catch (FileNotFoundException e) {
      logger.error("ファイルの読取に失敗しました。", e);
      e.printStackTrace();
    }
  }
}
