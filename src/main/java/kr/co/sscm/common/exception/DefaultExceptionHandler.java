package kr.co.sscm.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sscm.common.Constants;
import kr.msp.constant.Const;

/**
 * @FileName DefaultExceptionHandler.java
 * @comment 기본 Exception 핸들러 정의
 * @author AJH
 */
@ControllerAdvice
public class DefaultExceptionHandler {

	protected Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	/**
	 * API 공통 에러 처리
	 * @param request
	 * @param e
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ExceptionHandler(ApiException.class)
    protected ModelAndView handleException(HttpServletRequest request, ApiException e) {
        logger.error("ApiException handleException", e);

        ModelAndView mv = new ModelAndView("defaultJsonView");

        Map<String,Object> requstHead = (Map<String,Object>)request.getAttribute(Const.HEAD);
        Map<String,Object> resBody = new HashMap<String, Object>();
        resBody.put("resultCode", e.getResultCode());
        if(e.getMessage() != null && !e.getMessage().isEmpty()) {
        	resBody.put("resultMsg", e.getMessage());
        }
        else {
        	resBody.put("resultMsg","인터페이스 에러 입니다.");
        }

		mv.addObject(Const.HEAD, requstHead);
		mv.addObject(Const.BODY, resBody);

        return mv;

    }

	/**
	 * validation 공통 에러 처리
	 * @param request
	 * @param e
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ExceptionHandler(ValidationException.class)
    protected ModelAndView handleException(HttpServletRequest request, ValidationException e) {
        logger.error("ValidationException handleException", e);

        ModelAndView mv = new ModelAndView("defaultJsonView");

        Map<String,Object> requstHead = (Map<String,Object>)request.getAttribute(Const.HEAD);
        Map<String,Object> resBody = new HashMap<String, Object>();
        resBody.put("resultCode", e.getResultCode());
        if(e.getMessage() != null && !e.getMessage().isEmpty()) {
        	resBody.put("resultMsg", e.getMessage() + "이[가] 없습니다.");
        }
        else {
        	resBody.put("resultMsg","파라미터 에러 입니다.");
        }

		mv.addObject(Const.HEAD, requstHead);
		mv.addObject(Const.BODY, resBody);

        return mv;

    }

	/**
	 * EAI 공통 에러 처리
	 * @param request
	 * @param e
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ExceptionHandler(EaiException.class)
    protected ModelAndView handleException(HttpServletRequest request, EaiException e) {
        logger.error("EaiException handleException", e);

        ModelAndView mv = new ModelAndView("defaultJsonView");

        Map<String,Object> requstHead = (Map<String,Object>)request.getAttribute(Const.HEAD);
        Map<String,Object> resBody = new HashMap<String, Object>();
        resBody.put("resultCode", e.getResultCode());
        if(e.getMessage() != null && !e.getMessage().isEmpty()) {
        	resBody.put("resultMsg", e.getMessage());
        }
        else {
        	resBody.put("resultMsg","EAI 에러 입니다.");
        }

		mv.addObject(Const.HEAD, requstHead);
		mv.addObject(Const.BODY, resBody);

        return mv;

    }

	/**
	 * PUSH 발송 에러 처리
	 * @param request
	 * @param e
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ExceptionHandler(PushException.class)
    protected ModelAndView handleException(HttpServletRequest request, PushException e) {
        logger.error("PushException handleException", e);

        ModelAndView mv = new ModelAndView("defaultJsonView");

        Map<String,Object> requstHead = (Map<String,Object>)request.getAttribute(Const.HEAD);
        Map<String,Object> resBody = new HashMap<String, Object>();
        resBody.put("resultCode", e.getResultCode());
        if(e.getMessage() != null && !e.getMessage().isEmpty()) {
        	resBody.put("resultMsg", e.getMessage());
        }
        else {
        	resBody.put("resultMsg","PUSH 발송 에러 입니다.");
        }

		mv.addObject(Const.HEAD, requstHead);
		mv.addObject(Const.BODY, resBody);

        return mv;

    }

	/**
	 * 로그인(세션) 에러 처리
	 * @param request
	 * @param e
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ExceptionHandler(InterceptorException.class)
    protected ModelAndView handleException(HttpServletRequest request, InterceptorException e) {
        logger.error("InterceptorException handleException");
        logger.debug(e.toString());

        ModelAndView mv = new ModelAndView("defaultJsonView");

        Map<String,Object> requstHead = (Map<String,Object>)request.getAttribute(Const.HEAD);
        if(requstHead == null) {
        	requstHead = new HashMap<String, Object>();
        }
        Map<String,Object> resBody = new HashMap<String, Object>();
        requstHead.put(Const.RESULT_CODE, e.getResultCode());
        resBody.put("resultCode", e.getResultCode());
        if(e.getMessage() != null && !e.getMessage().isEmpty()) {
        	resBody.put("resultMsg", e.getMessage());
        	requstHead.put(Const.RESULT_MESSAGE, e.getMessage());
        }
        else {
        	resBody.put("resultMsg","intercepter 에러 입니다.");
        	requstHead.put(Const.RESULT_MESSAGE, "intercepter 에러 입니다.");
        }

		mv.addObject(Const.HEAD, requstHead);
		mv.addObject(Const.BODY, resBody);

        return mv;

    }

	/**
	 * 그외 공통 에러 처리
	 * @param request
	 * @param e
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ExceptionHandler(Exception.class)
    protected ModelAndView handleException(HttpServletRequest request, Exception e) {
        logger.error("Exception handleException", e);

        ModelAndView mv = new ModelAndView("defaultJsonView");

        Map<String,Object> requstHead = (Map<String,Object>)request.getAttribute(Const.HEAD);
        Map<String,Object> resBody = new HashMap<String, Object>();
        resBody.put("resultCode", Constants.ERR_CODE_EXCEPTION);
        if(e.getMessage() != null && !e.getMessage().isEmpty()) {
        	resBody.put("resultMsg", e.getMessage());
        }
        else {
        	resBody.put("resultMsg","에러가 발생하였습니다.");
        }

		mv.addObject(Const.HEAD, requstHead);
		mv.addObject(Const.BODY, resBody);

        return mv;

    }
}
