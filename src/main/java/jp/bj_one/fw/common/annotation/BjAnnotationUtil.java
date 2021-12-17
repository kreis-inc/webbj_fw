package jp.bj_one.fw.common.annotation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import jp.bj_one.fw.exception.BjSystemException;

public class BjAnnotationUtil {

	
	/**
	 * アノテーション '@BjDuplicateChk' 項目取得メソッド
	 * @param obj
	 * @return Map<String,Map<String,String[]>> 
	 */
	
	public static Map<String,Map<String,String[]>> getBjDuplicateChk(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		//Map<String,String[]> map = new HashMap<String,String[]>();
		Map<String,Map<String,String[]>>groupKeyMap = new HashMap<String,Map<String,String[]>>();
        for(Field f : fields) {
            if(f.isAnnotationPresent(BjDuplicateChk.class)) {
            	String groupNo = "";
            	String keyVal = "";
            	String name = "";
            	try {
            		f.setAccessible(true);
            		//値取得
            		if(f.get(obj)!=null){
            			keyVal = f.get(obj).toString();
            		}
            		//param(name)取得
            		 name= f.getAnnotation(BjDuplicateChk.class).name();
            		//gloupNoの取得
            		 groupNo = f.getAnnotation(BjDuplicateChk.class).groupKeyNo();
            		 
            	}catch(IllegalAccessException e) {
            		e.printStackTrace();
            		throw new BjSystemException(e);
            	}
            	String[] values= {keyVal,name};
            	
           		if( groupKeyMap.containsKey(groupNo)) {
           			Map<String,String[]> map= groupKeyMap.get(groupNo);
           		   	map.put(f.getName(),values);
        		}else {
        			Map<String,String[]> gmap = new HashMap<String,String[]>();
        			gmap.put(f.getName(), values);
        			groupKeyMap.put(groupNo, gmap);
        		}
            	
            }
        }
        return groupKeyMap;
	}
	
	/**
	 * アノテーション '@BjDbRequiredChk' 項目取得メソッド
	 * @param obj
	 * @return Map<String,Map<string,String[]>>   {keyVal,name,tableName,deleteFlgName};
	 */
	public static  Map<String,Map<String,String[]>> getBjDbRequiredChk(Object obj) {

		Field[] fields = obj.getClass().getDeclaredFields();
		//HashMap<String,String> map = new HashMap<String,String>();
		Map<String,Map<String,String[]>>groupKeyMap = new HashMap<String,Map<String,String[]>>();
		for(Field f : fields) {
            if(f.isAnnotationPresent(BjDbRequiredChk.class)) {
  
            	String keyVal = "";
            	String name = "";
            	String groupNo = "";
            	String tableName = "";
            	String deleteFlgName = "";
               	try {
            		f.setAccessible(true);
            		//値取得
            		if(f.get(obj)!=null){
            			keyVal = f.get(obj).toString();
            		}
            		//param(name)取得
            		 name= f.getAnnotation(BjDbRequiredChk.class).name();
            		 groupNo = f.getAnnotation(BjDbRequiredChk.class).groupKeyNo();
            		 tableName = f.getAnnotation(BjDbRequiredChk.class).tableName();
            		 deleteFlgName = f.getAnnotation(BjDbRequiredChk.class).deleteFlgName();
            		 
            	}catch(IllegalAccessException e) {
            		e.printStackTrace();
            		throw new BjSystemException(e);
            	}
            	String[] values= {keyVal,name,tableName,deleteFlgName};
            	
           		if( groupKeyMap.containsKey(groupNo)) {
           			Map<String,String[]> map= groupKeyMap.get(groupNo);
           		   	map.put(f.getName(),values);
        		}else {
        			Map<String,String[]> gmap = new HashMap<String,String[]>();
        			gmap.put(f.getName(), values);
        			groupKeyMap.put(groupNo, gmap);
        		}
            	
            }
        }
        return groupKeyMap;
	}

	/**
	 * アノテーション '@Bjkey' 項目取得メソッド
	 * @param obj
	 * @return Map<String,String[]> key = 項目ID, String[0] = 項目IDの値、String[1] = パラメータのname
	 */
	public static HashMap<String,String[]> getBjKeyMap(Object obj) {

		Field[] fields = obj.getClass().getDeclaredFields();
		HashMap<String,String[]> map = new HashMap<String,String[]>();
        for(Field f : fields) {
            if(f.isAnnotationPresent(BjKey.class)) {
                //String expression = f.getAnnotation(BjKey.class).expression();
                //if(expression.length() == 0) continue;
            	String keyVal = "";
            	String name = "";
            	try {
            		f.setAccessible(true);
            		//値取得
            		if(f.get(obj)!=null){
            			keyVal = f.get(obj).toString();
            		}
            		//param(name)取得
            		 name= f.getAnnotation(BjKey.class).name();
            	}catch(IllegalAccessException e) {
            		e.printStackTrace();
            		throw new BjSystemException(e);
            	}
            	String[] values= {keyVal,name};
            	map.put(f.getName(),values);
            }
        }
        return map;
	}
	
	
}
