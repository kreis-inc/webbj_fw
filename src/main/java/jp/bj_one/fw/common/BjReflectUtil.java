package jp.bj_one.fw.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.bj_one.fw.exception.BjSystemException;

public class BjReflectUtil {
	
	
	/**
	 * reflectでObjectの中からパラメータのフィールド名で値を取得するメソッド。
	 * @param obj
	 * @param Map<String,Object> フィールド名、値
	 */
	public static Map<String,Object> getSuperClassFildForMap(Object obj , List<String> filedNameList) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if (filedNameList == null) return null;
        for(String name : filedNameList){
        	Object o = BjReflectUtil.getSuperClassFild(obj, name);
        	if (o==null) continue;
        	map.put(name,o);
        }
		return map;
	}
	
	
	/**
	 * パラメータのObject内のフィールドを親クラスまで検索し値を取得するメソッド
	 * フィールドが見つからない場合はNULLを返します。
	 * @param obj 取得したフィールドを持つObject
	 * @param name 取得した値のフィールド名
	 * @return フィールドの値　取得できない場合はNULL
	 */
	public static Object getSuperClassFild(Object obj, String name) {

		if (obj == null || name == null) {
			return null;
		}

		Class<? extends Object> c = obj.getClass();
		Object result = null;
		Field f = null;
		while (c != null) {
			try {
				f = c.getDeclaredField(name);
				f.setAccessible(true);
				result = f.get(obj);
				return result;
			} catch (NoSuchFieldException e) {
				c = c.getSuperclass();
			} catch (IllegalArgumentException | IllegalAccessException eI) {
				eI.printStackTrace();
				return null;
			}
		}
		return null;
			
		
	}
	/**
	 * フィールドの値取得 注意＞親クラスまでは見に行かない
	 * @param obj 取得した値が格納されているObject
	 * @param name 取得したいフィールド名。
	 * @return 値 取得できない場合はNULLを返します。
	 */
	public static Object getField(Object obj, String name ) {

		if (obj == null || name == null) {
			return null;
		}
		Field f = null;
		Object result = null;
		try {
			f = obj.getClass().getDeclaredField(name);
			f.setAccessible(true);
			result = f.get(obj);
		} catch (SecurityException e) {
			//e.printStackTrace();
		} catch (NoSuchFieldException e) {
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		}
		return result;
	}


	/**
	 * フィールドの値を設定します。<br />
	 *
	 * @param obj
	 *            設定対象オブジェクト
	 * @param name
	 *            設定対象フィールド名
	 * @param value
	 *            設定する値
	 */
	public static void put(Object obj, String name, Object value) {
		Field f = null;
		try {
			f = obj.getClass().getDeclaredField(name);
			f.setAccessible(true);
			f.set(obj, value);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Object内にjava.util.Listのfieldがあるかチェック。ある場合該当Listをreturn
	 * @param o
	 * @return List<List<?　extends Object>>
	 */
	
	public static List<List<? extends Object>> getChildList(Object o) {
		try {
			List<List<? extends Object>> returnList = null;
			for (Field field : o.getClass().getDeclaredFields()) {
		           field.setAccessible(true);
		           //int modifier = field.getModifiers();
		           Class<?> type = field.getType();
		           //String name = field.getName();
		           //Object value = field.get(o);
		           //System.out.println(
		              //type.getTypeName() + " " + type.getClass() + " "+ name + " = " + value);
		           if (type.equals(List.class)) {
		        	   
		        	   if(returnList == null) {
		        		   returnList = new ArrayList<List<?>>();
		        	   }
		        	   List<? extends Object>value =( List<?> )field.get(o);
		        	   returnList.add( value );
		           }
			}
			 return returnList;
		}catch( IllegalAccessException e) {
			throw new BjSystemException(e);
		}
	}
	
	/**
	 * 指定したメソッド名を実行し値を取得するメソッド
	 * @param o
	 * @param methodName
	 * @return　methodNameで実行されたReturn値　取得できない場合はNULLが返ります。
	 */
	public static Object getMethod(Object o , String methodName) {
		
		try {
			Method method = o.getClass().getDeclaredMethod(methodName);
			method.setAccessible(true);
			return method.invoke(o);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch ( NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
