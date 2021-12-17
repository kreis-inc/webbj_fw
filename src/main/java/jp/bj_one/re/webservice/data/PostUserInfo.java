package jp.bj_one.re.webservice.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostUserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId = "";
	private String bjSystemId = "";
	private String loginUserName = "";
	
	public void setUserId(String userId) {
		this.userId = userId != null ? userId : "";
	}
	
	public void setBjSystemId(String bjSystemId) {
		this.bjSystemId = bjSystemId != null ? bjSystemId : "";
	}
	
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName != null ? loginUserName : "";
	}
}
