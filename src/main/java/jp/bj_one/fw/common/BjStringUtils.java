package jp.bj_one.fw.common;

import org.apache.commons.lang3.StringUtils;

public class BjStringUtils {

	

	
	/**
	 * snake_case から　camelCaseへ変更するメソッド
	 * @param map
	 * @return
	 */
	public static String CamelToSnake(String val){
		
		if(val == null) return null;
		
			String snake = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(val), "_").toLowerCase();
			snake = snake.substring(0, 1).toLowerCase() + snake.substring(1);
	
		return snake;
	}
	
}
