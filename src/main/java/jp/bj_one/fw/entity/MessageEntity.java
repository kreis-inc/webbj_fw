package jp.bj_one.fw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * メッセージ格納クラス
 * @author jin-xin
 *
 */
public class MessageEntity implements Serializable{


	private String msgId;
	private String[] param;
	private String messageStr;
	private List<String> itemIdList;





	public List<String> getItemIdList() {
		return itemIdList;
	}

	public void setItemIdList(List<String> itemIdList) {
		this.itemIdList = itemIdList;
	}

	public String getMessageStr() {
		return messageStr;
	}

	public void setMessageStr(String messageStr) {
		this.messageStr = messageStr;
	}

	public MessageEntity() {

	}

	public MessageEntity(String msgId, String[] param) {
		this.msgId = msgId;
		this.param = param;

	}

	public MessageEntity(String msgId, String param) {

		this.msgId = msgId;
		this.param = new String[] {param};

	}

	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String[] getParam() {
		return param;
	}
	public void setParam(String[] param) {
		this.param = param;
	}










}
