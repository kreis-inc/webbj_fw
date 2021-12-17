package jp.bj_one.fw.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jp.bj_one.fw.common.BjUserInfo;
import jp.bj_one.fw.entity.MLoginEntity;
import jp.bj_one.fw.repo.MLoginRepository;

@Component
public class BjUserDetailsService implements UserDetailsService {

  @Autowired
  MLoginRepository mLoginRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    MLoginEntity user = null;
    try {
      user = new MLoginEntity();
      user.setUserId(userId);

      user = mLoginRepository.findOne(user);
    } catch (Exception e) {
      throw new UsernameNotFoundException("");
    }

    return new BjUserInfo(user);
  }

}
