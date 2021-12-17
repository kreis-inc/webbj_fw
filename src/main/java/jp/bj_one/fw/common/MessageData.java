package jp.bj_one.fw.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import jp.bj_one.fw.entity.MessageEntity;

public class MessageData  implements Serializable{

	private List<MessageEntity> messageList;

	public List<MessageEntity> getMessageList() {
		return messageList;
	}


	/**
	 *エラーが発生しているか確認するメソッド.
	 *
	 * @return true エラー有
	 */
	public boolean isError() {

		for(MessageEntity entity :messageList ) {
			String id = entity.getMsgId();

			String id2 = String.valueOf(id.charAt(1));
			if ("E".equals(id2)) {
			return true;
			}
		}
		return false;

	}

	public void setMessageList(List<MessageEntity> messageList) {
		this.messageList = messageList;
	}
	public MessageData() {
		this.messageList = new ArrayList<MessageEntity>();

	}
	public MessageData(String messageId, String[] param) {
		MessageEntity entity = new MessageEntity();
		entity.setMsgId(messageId);
		entity.setParam(param);

		if(this.messageList==null) {
			this.messageList = new ArrayList<MessageEntity>();
		}

		this.messageList.add(entity);
	}

	public void add(MessageEntity msgEntity) {

		if(this.messageList==null) {
			this.messageList = new ArrayList<MessageEntity>();
		}
		this.messageList.add(msgEntity);

	}

	public void add(String msgId, String param) {

		if(this.messageList==null) {
			this.messageList = new ArrayList<MessageEntity>();
		}
		String[] params = new String[] {param};
		this.messageList.add( new MessageEntity(msgId,params));
	}

	public void add(String msgId, String[] param) {

		if(this.messageList==null) {
			this.messageList = new ArrayList<MessageEntity>();
		}

		this.messageList.add( new MessageEntity(msgId,param));
	}

	public void getMessageStr( MessageSource messageSource, Locale locale) {

		if (this.messageList == null) {
			return;
		}


		for(MessageEntity entity : this.messageList) {

			String str = messageSource.getMessage(entity.getMsgId(), entity.getParam(), locale);
			entity.setMessageStr(str);
		}

	}

}
