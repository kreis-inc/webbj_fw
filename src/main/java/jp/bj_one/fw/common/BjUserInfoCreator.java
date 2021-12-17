package jp.bj_one.fw.common;

import jp.bj_one.fw.entity.MLoginEntity;

public class BjUserInfoCreator implements BjNonAuthenticationImpl {

  @Override
  public BjUserInfo createUserInfo() {

    MLoginEntity user = new MLoginEntity();
    user.setUserId("guest");
    user.setPassword("275876e34cf609db118f3d84b799a790"); // dummy
    return new BjUserInfo(user);
  }

}
