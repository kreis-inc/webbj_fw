package jp.bj_one.fw;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.JavaScriptUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.bj_one.fw.common.BjNonAuthenticationImpl;
import jp.bj_one.fw.common.BjUserInfo;
import jp.bj_one.fw.common.MessageData;
import jp.bj_one.fw.config.BjSecurityConfigurationProperties;
import jp.bj_one.fw.dto.ResponseDto;
import jp.bj_one.fw.entity.BjLogOperationEntity;
import jp.bj_one.fw.entity.BjMSystemInfoEntity;
import jp.bj_one.fw.entity.MessageEntity;
import jp.bj_one.fw.exception.ApplicationException;
import jp.bj_one.fw.form.BjForm;
import jp.bj_one.fw.svc.BjLogOperationService;
import jp.bj_one.fw.svc.BjSystemInfoService;

@Aspect
@Component
public class BjOneControllerInterceptor {

  Logger logger = LoggerFactory.getLogger(BjOneControllerInterceptor.class);

  @Autowired
  protected MessageSource messageSource;

  @Autowired
  BjSecurityConfigurationProperties configurationProperties;

  @Autowired
  protected Mapper mapper;

  @Autowired
  private AutowireCapableBeanFactory factory;

  @Autowired
  BjLogOperationService logService;

  @Autowired
  BjSystemInfoService bjSystemInfoService;

  /**
   * ???????????????????????????????????????
   *
   * @param joinPoint
   */
  @Before("execution(public * jp.bj_one.fw.AbstractController+.*(..))")
  public void before(JoinPoint joinPoint) {

    /*// ?????????Info??????
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
    request.getSession();*/

  }

  /**
   * ??????????????????????????????
   *
   * @param pjp
   * @return
   * @throws Throwable
   */
  @Around("execution(public * jp.bj_one.fw.AbstractController+.*(..))")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {

	Object obj = null;
	long start = System.currentTimeMillis();
    // ?????????????????????
    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
    BjUserInfo userInfo = null;
    String userId = null;

    Object[] objArray = pjp.getArgs();
    BjForm form = null;
    Model model = null;
    for (Object objs : objArray) {
      if (objs instanceof BjForm) {
         form = (BjForm) objs;
        // form.setLockFlg(systemInfoEntity.getBjLockFlg());
      }else if(objs instanceof Model) {
        model = (Model)objs;
      }
    }

    if (configurationProperties.isActivate()) {
      if (authentication.getPrincipal() instanceof BjUserInfo) {
        userInfo = (BjUserInfo) authentication.getPrincipal();
      }
    } else {
      @SuppressWarnings("rawtypes")
      Class creatorClazz = Class.forName(configurationProperties.getNonAuthUserInfoCreatorClass());
      @SuppressWarnings("unchecked")
      BjNonAuthenticationImpl creator = (BjNonAuthenticationImpl) factory.createBean(creatorClazz);
      userInfo = creator.createUserInfo();
    }
    if (userInfo != null) {
      userId = userInfo.getUsername();
    }

    if (form == null ) {
    	form = new BjForm();
    }
    ((BjForm)form).setUserInfo(userInfo);

    BjMSystemInfoEntity systemInfoEntity = bjSystemInfoService.getBjMSystemInfoEntity();

    // ??????????????????
    logger.info(
      "User:" + form.getUserInfo().getUser().getUserId()
      + ",Screen:" + form.getTriggerScreenId()
      + ",Ctrl:" + form.getTriggerCtrlId()
      + ",Event:" + form.getTriggerEventId()
      + ",Item:" + form.getTriggerItemName()
    );

    logger.info(pjp.getTarget().getClass().getSimpleName() + "?????????????????????[" + userId + "]");

    //????????????????????????SET???entity
    BjLogOperationEntity logEntity = new BjLogOperationEntity();
    //BjLogOperationEntity logEntity = null;
    try {
        if( systemInfoEntity == null ) {
        	//?????????????????????DB?????????????????????????????????????????????
        	form.getMessageData().add("LE_BJ01","");
        	throw new ApplicationException(form.getMessageData());
        } else {
        	form.setLockFlg(systemInfoEntity.getBjLockFlg());
        }
     form.setMessageData(new MessageData());
     obj = pjp.proceed();

    } catch (ApplicationException e) {

      MessageData messageData = e.getMessageData();
      if ( systemInfoEntity.getBjLockFlg() != null && "1".equals(systemInfoEntity.getBjLockFlg())) {
	      if (messageData != null && messageData.isError()) {
	    	messageData.getMessageStr(messageSource, Locale.JAPAN);
	        // ????????????
	    	StringBuffer buf =  new StringBuffer("");
	        for (MessageEntity entity : messageData.getMessageList()) {
	          logger.error(entity.getMsgId() + ":" + entity.getMessageStr());
	          buf.append(entity.getMessageStr());
	          buf.append(System.getProperty("line.separator"));
	        }
	        logEntity.setStatus("BjError");
	        logEntity.setErrorCd("2");
	        logEntity.setError(buf.toString());

	      } else {
	    	  logEntity.setStatus("success");
	    	  logEntity.setErrorCd("0");
	      }
      }
      ResponseDto dto = new ResponseDto();
      dto.setForm(form);
      //dto.setMessageData(messageData);

      // display ??? view ????????????????????????????????????????????????????????????????????????????????????
      if( model != null ) {
        // ???????????????????????????????????????????????? display or view ??????????????????JSP?????????????????????
        // DTO ????????????????????????????????????
        model.addAttribute("responseDto", JavaScriptUtils.javaScriptEscape(new ObjectMapper().writeValueAsString(dto)).replaceAll("&", "&amp;"));

        if(form.getChengeJsp() == null || "".equals(form.getChengeJsp())) {
        	return ((AbstractController)pjp.getTarget()).getJspPath();
        } else {
        	return form.getChengeJsp();
        }



      }else {
        // ?????????????????????????????????????????????????????????????????????????????????DTO???????????????
      return dto;
      }
    } catch (Exception e) {
      e.printStackTrace();
      if ( systemInfoEntity.getBjLockFlg() != null && "1".equals(systemInfoEntity.getBjLockFlg())) {
	      //logEntity = new BjLogOperationEntity();
	      logEntity.setStatus("SystemError");
	      logEntity.setErrorCd("1");
	      try {
	    	  //Exception?????????DB??????????????????
		      StringWriter sw = new StringWriter();
		      PrintWriter pw = new PrintWriter(sw);
		      e.printStackTrace(pw);
		      pw.flush();
		      logEntity.setError(sw.toString());
	      }catch(Exception ee) {
	    	  ee.printStackTrace();
	      }
      }
      throw e;
    } finally {
      // ??????????????????
    	if(form.getMessageData() != null && form.getMessageData().isError()) {
        	 form.getMessageData().getMessageStr(messageSource, Locale.JAPAN);
         }
      logger.info(pjp.getTarget().getClass().getSimpleName() + "????????????????????????[" + userId + "]");

      if ( systemInfoEntity.getBjLockFlg() != null && "1".equals(systemInfoEntity.getBjLockFlg())) {
	      //BjLogOperation?????????????????????
	      	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
	  	            .getRequest();
	      	 //if (request.getHeader("CONTENT-TYPE") !=null ) {

	      	String requestData = null;
	      	Map<String,String> map = new LinkedHashMap<>();
	      	  try {
	      		requestData = request.getReader().lines().collect(Collectors.joining());
	    		// JSON -> Map
	      		if (requestData != null && !"".equals(requestData)) {
		    		ObjectMapper mapper = new ObjectMapper();
		    		map = mapper.readValue(requestData, new TypeReference<LinkedHashMap<String,String>>(){});
		    		if( map != null) {
		    			String names = map.get("triggerItemName");
		    			if(names != null && !"".equals(names) && "menu".equals(names.substring(0,4))){
		    				logEntity.setObject("????????????");
		    			}
		    		}
					logEntity.setTriggerCtrlId(map.get("triggerCtrlId"));
					logEntity.setServiceName(map.get("triggerItemName"));
	      		}
	      	  } catch (Exception e) {
	      		  /*report an error*/
	      		 // e.printStackTrace();
	      	  }
	      	  if(logEntity.getStatus() == null || "".equals(logEntity.getStatus())) {
		      	  logEntity.setStatus("success");
		      	  logEntity.setErrorCd("0");
	      	  }
	          //???????????????????????????
	      	if( logEntity.getObject() == null || "".equals(logEntity.getObject())) {
	      		logEntity.setObject("??????");
			 }

		    try {
	  	    	//DB????????????@Async??????????????????????????????
		  	    logEntity.setIp(request.getLocalAddr());
		    	long end = System.currentTimeMillis();
		    	//System.out.println((end - start)  + "ms");
		    	BigDecimal time = BigDecimal.valueOf(end - start);
		    	logEntity.setActiontime(time);
	  	    	logService.setBjLogOperation(logEntity,form);
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
	      }
	    }
    return obj;
  }

  /**
   * ???????????????????????????????????????
   *
   * @param joinPoint
   */

   	@After("execution(public * jp.bj_one.userPortal.app.*.*Controller.*(..))")
    public void after(JoinPoint joinPoint) {

   	//Object[] objArray = joinPoint.getArgs();
	   /*
	   *
	   * HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
	   * .currentRequestAttributes()).getRequest();
	   *
	   * // AJAX????????????Model???????????????????????? String header =
	   * request.getHeader("X-Requested-With"); //if ((header != null) &&
	   * header.equals("XMLHttpRequest")) { //return; //}
	   *
	   * Model model = null; Form form = null; for (Object obj : objArray) { if (obj
	   * instanceof Model) { model = (Model) obj; } if (obj instanceof Form) { form =
	   * (Form) obj; } }
	   *
	   *
	   * // ????????????????????????JSON???ResponseDto????????????????????????????????????????????? ResponseDto dto = new
	   * ResponseDto(); dto.setForm((BjForm)form);
	   *
	   *
	   * if (model != null && form != null) { try { model.addAttribute("responseDto",
	   * StringEscapeUtils.escapeHtml4 (new ObjectMapper().writeValueAsString(dto)));
	   * } catch(JsonProcessingException ex) { ex.printStackTrace(); } } }
	   */
   	}
 }
