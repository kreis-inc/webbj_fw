package jp.bj_one.fw.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.bj_one.fw.entity.BjLiteralEntity;
import jp.bj_one.fw.entity.BjLiteralParamEntity;

@Controller
public class BjLiteralController {

  @ResponseBody
  @RequestMapping(value = "/itemRefresh", method = RequestMethod.POST)
  public Map<String, Map<Object, String>> itemRefresh(@RequestBody Map<String, BjLiteralParamEntity> params) {

    Map<String, Map<Object, String>> resultMap = new HashMap<String, Map<Object, String>>();
    BjLiteralParamEntity paramEntity = null;
    for (Map.Entry<String, BjLiteralParamEntity> entity : params.entrySet()) {
      paramEntity = entity.getValue();
      if (!paramEntity.isUseBjLiteral()) {
        continue;
      }
      List<BjLiteralEntity> selectEntityList = BjLiteralUtil.getLiteralSelectList(paramEntity);

      // 取得結果の編集
      Map<Object, String> resultList = new LinkedHashMap<Object, String>();
      for (BjLiteralEntity literalEntity : selectEntityList) {
        if ("CHARACTER".equals(literalEntity.getLiteralValueType())) {
          resultList.put(literalEntity.getLiteralStringValue(), literalEntity.getLiteralItemName());
        } else if ("DATE".equals(literalEntity.getLiteralValueType())) {
          resultList.put(literalEntity.getLiteralDateValue(), literalEntity.getLiteralItemName());
        } else if ("NUMBER".equals(literalEntity.getLiteralValueType())) {
          resultList.put(literalEntity.getLiteralNumValue(), literalEntity.getLiteralItemName());
        }
      }
      resultMap.put(entity.getKey(), resultList);
    }
    return resultMap;
  }
}
