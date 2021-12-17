package jp.bj_one.fw.common;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

/**
 * JSONエスケープクラス。
 * 
 * @author kaoru.amagai
 */
public class JsonCharacterEscapes extends CharacterEscapes {
  /** シリアルバージョン。 */
  private static final long serialVersionUID = 1L;

  /** エスケープ設定。 */
  private final int[] asciiEscapes;

  /**
   * デフォルトコンストラクタ。
   */
  public JsonCharacterEscapes() {
    // エスケープ設定の初期化
    asciiEscapes = standardAsciiEscapesForJSON();
    asciiEscapes['/'] = CharacterEscapes.ESCAPE_STANDARD;
    asciiEscapes['<'] = CharacterEscapes.ESCAPE_STANDARD;
    asciiEscapes['>'] = CharacterEscapes.ESCAPE_STANDARD;
    asciiEscapes['+'] = CharacterEscapes.ESCAPE_STANDARD;
  }

  /**
   * エスケープ設定を呼び出し元に返します。
   * 
   * @return エスケープ設定
   */
  @Override
  public int[] getEscapeCodesForAscii() {
    return asciiEscapes;
  }

  @Override
  public SerializableString getEscapeSequence(int arg0) {
    return null;
  }
}
