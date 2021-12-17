package jp.bj_one.fw.common.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * 暗号化ユーティリティクラス。
 * 
 * @author kaoru.amagai
 */
public class Encryptor {
  /**
   * MD5ハッシュ文字列を作成します。
   * 
   * @param target 生成対象文字列
   * @return 生成されたハッシュ文字列
   */
  public static String md5(String target) {
    // 暗号化アルゴリズム
    Charset charset = StandardCharsets.UTF_8;
    String algorithm = "MD5";

    // 文字列生成
    String res = null;
    try {
      byte[] bytes = MessageDigest.getInstance(algorithm).digest(target.getBytes(charset));
      res = DatatypeConverter.printHexBinary(bytes).toLowerCase();
    } catch (NoSuchAlgorithmException e) {
      // アルゴリズムが不正な場合
      e.printStackTrace();
    }

    return res;
  }
}
