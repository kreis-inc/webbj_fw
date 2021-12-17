package jp.bj_one.fw.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jp.bj_one.fw.dto.ResponseDto;

@Controller
@MultipartConfig
public class FileUploadController {
  Logger logger = LoggerFactory.getLogger(FileUploadController.class);
  
  @Value("${bj.out.uploadFileDir}") 
  private String uploadPath;

  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  @ResponseBody
  public ResponseDto fileUpload(@RequestParam("name") String name, @RequestParam("uid") String uid,
      @RequestParam("file") MultipartFile file) throws IOException {

    final File uploadDir = new File(uploadPath);
    uploadDir.mkdir();

    save(file, new File(uploadDir, uid));

    return new ResponseDto();

  }

  public void save(MultipartFile inFile, File outFile) throws IOException {

    BufferedInputStream reader = new BufferedInputStream(inFile.getInputStream());
    try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outFile))) {
      int len = 0;
      byte[] buff = new byte[1024];
      while ((len = reader.read(buff)) != -1) {
        writer.write(buff, 0, len);
      }
    }
  }

}
