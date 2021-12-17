package jp.bj_one.fw.common;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class BjBeanUtils {
	
	
	/**
	 * snake_caseのマップをcamelCaseへ変更するメソッド
	 * @param map
	 * @return
	 */
	public static Map<String,Object> snakeToCamel(Map<String,Object> map) {

		if(map == null) return null;
		Iterator<String> ite = map.keySet().iterator();
		Map<String,Object> camelMap = new HashMap<String,Object>();
		while (ite.hasNext()) {
			String val = (String) ite.next();
			String camel = StringUtils.remove(WordUtils.capitalizeFully(val, '_'), "_");
			camel = camel.substring(0, 1).toLowerCase() + camel.substring(1);
			camelMap.put(camel, map.get(val));
		}
		return camelMap;
	}
	
	/**
	 * snake_caseのマップをcamelCaseへ変更するメソッド
	 * @param map
	 * @return
	 */
	public static Map<String,Object>CamelToSnake(Map<String,Object>map){
		
		if(map == null) return null;
		Iterator<String> ite = map.keySet().iterator();
		Map<String,Object> snakeMap = new HashMap<String,Object>();
		while (ite.hasNext()) {
			String val = (String) ite.next();
			String snake = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(val), "_").toLowerCase();
//			System.out.println(snake);
			snake = snake.substring(0, 1).toLowerCase() + snake.substring(1);
			snakeMap.put(snake, map.get(val));
		}
		return snakeMap;
	}

	/**
	 * データベースから取得したMap<String,Object>からBeanへpopulateするUtilityMethod
	 * データベースからのMap　snake_kase →　BeanのCamel対応。
	 * @param resultMap
	 * @return Object　o exeption時はNULLを返します。
	 */
	public static Object populateUtil( Object o ,Map<String,Object> resultMap) {
		
		try {
			BeanUtils.populate(o, BjBeanUtils.snakeToCamel(resultMap));			
		} catch( IllegalAccessException  | InvocationTargetException e ) {
			e.printStackTrace();
			return null;
		}
		return o;
	}
	
}
