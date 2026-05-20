package kr.co.sscm.common.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.sscm.common.dto.ApiRequestDto;
import kr.co.sscm.common.util.CommonUtils;
import kr.msp.constant.Const;

@Component
@SuppressWarnings("unchecked")
public class ApiRequestResolver implements HandlerMethodArgumentResolver {

	protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest)webRequest.getNativeRequest();

        //클라이언트에서 넘어온 request(HEAD+BODY) 모든정보
        Map<String,Object> reqMap =  (Map<String,Object>)req.getAttribute(Const.HTTP_BODY);
        //클라이언트에서 넘어온 공통 헤더 맵정보
        Map<String,Object> reqHeadMap =  (Map<String,Object>)req.getAttribute(Const.HEAD);
        //클라이언트에서 넘긴 파라미터 맵정보
        Map<String,Object> reqBodyMap =  (Map<String,Object>)req.getAttribute(Const.BODY);

        if(reqHeadMap==null){
            reqHeadMap = new HashMap<String, Object>();
        }
        reqHeadMap.put(Const.RESULT_CODE, Const.OK);
        reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

    	boolean isMultipart = false;

    	MultipartHttpServletRequest mReq;
    	if(req instanceof MultipartHttpServletRequest){
    		mReq = (MultipartHttpServletRequest) req;
    		isMultipart = ServletFileUpload.isMultipartContent(mReq);
    	}

    	logger.info("############## parameter ################");
        if(isMultipart) {
    		reqBodyMap = new HashMap<>();
    		reqBodyMap.put("isMultipart", isMultipart);
    		Map<String, String[]> keyMap = req.getParameterMap();
    		List<String> result = new ArrayList<String>(keyMap.keySet());

    		for(String key : result){
    			logger.info("##### " + key + " : " + keyMap.get(key)[0]);
    			reqBodyMap.put(key, CommonUtils.cleanXSS(keyMap.get(key)[0]));
    		}
        }else {
//        		Set<Map.Entry<String,Object>> bodyMapSet = reqBodyMap.entrySet();
//                for(Map.Entry<String,Object> me : bodyMapSet){
//                	logger.info("##### " + me.getKey() + " : " + me.getValue());
//                    reqBodyMap.put(me.getKey(),
//                    					CommonUtils.cleanXSS(CommonUtils.isToString(me.getValue()))
//                    				);
//                }
	        	if( reqBodyMap == null ) {
	    			throw new Exception();
	    		}
	    		
	            Set<Map.Entry<String,Object>> bodyMapSet = reqBodyMap.entrySet();
	            for(Map.Entry<String,Object> me : bodyMapSet){
	            	logger.debug("############## " + me.getKey() + " : " + CommonUtils.isToString(me.getValue()));
	            	if(me.getValue() instanceof Map || me.getValue() instanceof List) {
	            		reqBodyMap.put(me.getKey(),me.getValue());
	            	}
	            	else {
	            		reqBodyMap.put(me.getKey(), CommonUtils.cleanXSS(CommonUtils.isToString(me.getValue())));
	            	}
	            }
        	}
//        }
        logger.debug("########################################");
        return new ApiRequestDto(reqMap, reqHeadMap, reqBodyMap);

	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		final boolean isRegUserAnnotation = parameter.getParameterAnnotation(ApiRequest.class) != null;
	    final boolean isRegisterDto = parameter.getParameterType().equals(ApiRequestDto.class);
	    return isRegUserAnnotation && isRegisterDto;

		//return parameter.hasParameterAnnotation(ApiRequest.class);
        //return parameter.getParameterAnnotation(ApiRequest.class) != null;
	}
}
