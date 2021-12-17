package jp.bj_one.fw.svc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.bj_one.fw.common.BJSeqUtil;
import jp.bj_one.fw.entity.BjLogOperationEntity;
import jp.bj_one.fw.form.BjForm;
import jp.bj_one.fw.repo.BjLogOperationRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class BjLogOperationService {

	/**
	 * アクセス証跡ログをDBへセットするメソッド。
	 */
	@Autowired
	  BjLogOperationRepository repository;
	@Async("bjLogger")
	public void setBjLogOperation(BjLogOperationEntity logEntity,BjForm form) {


		 logEntity.setSeq(BJSeqUtil.getBjSeq());
		 logEntity.setDate(new Timestamp(System.currentTimeMillis()));


		if (form != null) {
			 logEntity.setUserId(form.getUserInfo().getUser().getUserId());
		  	 logEntity.setUserName(form.getUserInfo().getUser().getUserName());
		  	 logEntity.setSystemId(form.getUserInfo().getBjSystemId());
		  	 logEntity.setServiceId(form.getTriggerScreenId());
		  	 logEntity.setTriggerEventId(form.getTriggerEventId());
		  	 logEntity.setActionId(form.getTriggerItemId());
		  	 logEntity.setActionName(form.getTriggerItemName());
		  	 logEntity.setChangeScreenId(form.getChangeScreenId());
		  	 logEntity.setChangeScreenName(form.getChangeScreenName());
		  	 if(logEntity.getServiceName() == null || "".equals(logEntity.getServiceName())){
			  	logEntity.setServiceName(form.getTriggerScreenName());
			 }
			 if( logEntity.getTriggerCtrlId()==null || "".equals(logEntity.getTriggerCtrlId())) {
			  	logEntity.setTriggerCtrlId(form.getTriggerCtrlId());
			 }
			try {
				//input情報SET
			 	String formclassName = form.getClass().getName();
		    	Class<?> c = Class.forName(formclassName);
	            // インスタンスの生成
	            //Object formObj = c.newInstance();
	            Field[] fields = c.getDeclaredFields();
	            StringBuffer buf = new StringBuffer("■" + formclassName + " ");
	            for (Field field : fields) {
	     			//System.out.println(field.getName() + " : " + field.getType());
	    			if (java.lang.String.class == field.getType()) {
	    				Method m = c.getMethod("get" + StringUtils.capitalize(field.getName()));
	    				String str = (String)m.invoke(form);
	    				//System.out.println(field.getName() + " : " + str + " : " + field.getType());
	    				buf.append(field.getName() + " : " + str + " ");
	    				buf.append(System.getProperty("line.separator"));
	    			}
	    		}
	            //StringBuffer buf = new StringBuffer();
				//buf.append("controller終了後dump :" + ((BjForm)form).toString());
	            //buf.append(System.getProperty("line.separator"));
	            buf.append(form.toString());
	            logEntity.setInput(buf.toString());

			}catch(Exception e) {
				e.printStackTrace();
				//処理はそのまま続行。
			}
		}
		repository.insert(logEntity);
		//System.out.println("■■■■■■■■ログ実行終了");

	}


}
