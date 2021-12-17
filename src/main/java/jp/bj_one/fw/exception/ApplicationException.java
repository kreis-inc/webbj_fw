package jp.bj_one.fw.exception;

import jp.bj_one.fw.common.MessageData;

public class ApplicationException extends RuntimeException {

  /**
   * メッセージリスト。
   */
  private MessageData messageData;

  /**
   * 例外。
   */
  private Exception exception;

  /**
   * コンストラクタ。
   */
  public ApplicationException() {
    this(new MessageData());
  };

  /**
   * コンストラクタ。
   *
   * @param message
   */
  public ApplicationException(MessageData messageData) {
    this.setMessageData(messageData);
  }

  /**
   * コンストラクタ。
   *
   * @param message メッセージ
   * @param exception Exception
   */
  public ApplicationException(MessageData messageData, Exception exception) {
    this(messageData);
    this.setException(exception);
  }

  /**
   * messageDataを取得.
   *
   * @return messageData
   */
  public MessageData getMessageData() {
    return messageData;
  }

  /**
   * messageListを設定.
   *
   * @param messageList messageList
   */
  public void setMessageData(MessageData messageData) {
    this.messageData = messageData;
  }

  /**
   * exceptionを取得.
   *
   * @return exception
   */
  public Exception getException() {
    return exception;
  }

  /**
   * exceptionを設定.
   *
   * @param exception exception
   */
  public void setException(Exception exception) {
    this.exception = exception;
  }

}
