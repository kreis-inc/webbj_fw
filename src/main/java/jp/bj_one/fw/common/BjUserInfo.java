package jp.bj_one.fw.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.core.authority.AuthorityUtils;

import jp.bj_one.fw.entity.MLoginEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BjUserInfo extends org.springframework.security.core.userdetails.User  implements Serializable{


  public BjUserInfo(MLoginEntity user) {
    super(user.getUserId(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
    this.user = user;
  }
  //bj_m_login
  private MLoginEntity user;
  //システムID
  private String bjSystemId;
  //ログインユーザ名（氏名）
  private String loginUserName;
  //ロール
  private List<String> role;
  private String uuid;


}
