package jp.bj_one.fw.common;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.bj_one.fw.entity.BjLiteralEntity;
import jp.bj_one.fw.entity.BjLiteralParamEntity;
import jp.bj_one.fw.svc.BjLiteralService;

/**
 * リテラルを操作するためのユーティリティクラス。
 *
 * @author kaoru-shimizu
 */
@Component
public class BjLiteralUtil {

  private static BjLiteralService bjLiteralService;

  @Autowired
  private BjLiteralService wiredLiteralService;

  @PostConstruct
  private void start() {
    BjLiteralUtil.bjLiteralService = wiredLiteralService;
  }

  public static List<BjLiteralEntity> getLiteralSelectList(BjLiteralParamEntity entity) {

    List<BjLiteralEntity> results = bjLiteralService.findList(entity);
    return results;
  }

}
