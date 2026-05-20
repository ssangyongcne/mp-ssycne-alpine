package kr.co.sscm.common.interceptor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.sscm.common.exception.InterceptorException;
import kr.co.sscm.common.util.CommonUtils;
import kr.co.sscm.common.util.SessionUtils;
import kr.msp.base.security.DefaultSecureAuth;
import kr.msp.base.security.SecureAuth;
import kr.msp.constant.Const;
import kr.msp.event.dto.RequestVo;
import kr.msp.event.manager.EventLogManager;

/**
 * @FileName SscmInterceptor.java
 * @comment 기본 인터셉터
 * @author AJH
 */
public class SscmInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SscmInterceptor.class);

	private SecureAuth secureAuth;

	@Value("${server.eventlog.use}")
	private boolean eventLogUse;

	@Value("${server.eventlog.save_path}")
	private String eventLogPath;

    @Value("${server.type}")
    private String server;

	public void setSecureAuth(SecureAuth secureAuth) {
		this.secureAuth = secureAuth;
	}

	private boolean isPublic(HttpServletRequest request) {
		boolean isAllowed = true;
		String cUri = request.getRequestURI();

		List<String> publicUris = new ArrayList<String>();

		// 제외 url
		publicUris.add("/gw/JMA101Process");				// 모바일 로그인
		publicUris.add("/push/sendPush");					//

		if(!cUri.equals("") && !cUri.equals("/")) {
			isAllowed = false;
			for(String publicUri : publicUris) {

				if(cUri.contains(publicUri)) {
					isAllowed = true;
					break;
				}
			}
		}
		logger.debug("isAllowed:"+isAllowed);
		return isAllowed;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		logger.info("[ # preHandle - 1 # ] ");// + this.eventLogUse);

		logger.info(InetAddress.getLocalHost().getHostAddress());
		Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		if( !"local".equals(server) ) {
			if(!this.isPublic(request)) {
				if (this.eventLogUse) {
					logger.info("[ # this.eventLogUse - 1 # ] " + this.eventLogUse);
					EventLogManager.getInstance().setEventLogPath(this.eventLogPath);
					if (!EventLogManager.getInstance().isStarted()) {
						EventLogManager.getInstance().start();
					}
				}
			}
		}

		String requestMethod = StringUtils.defaultString(request.getMethod());
		if (requestMethod.equalsIgnoreCase(HttpMethod.POST.toString()) || requestMethod.equalsIgnoreCase(HttpMethod.PUT.toString())) {

			String encyn = StringUtils.defaultString(request.getHeader("user_data_enc"), "n");
			if (logger.isInfoEnabled()) {
				logger.info("Encryption : [" + encyn + "]");
			}

			Map bodyMap;
			if (StringUtils.equals(encyn, "y")) {
				String encmodule = request.getHeader("user_enc_name");
				if (StringUtils.equals(encmodule, "UracleSE")) {
					bodyMap = this.secureAuth.requestHandle(request, response);
				} else {
					bodyMap = this.secureAuth.requestHandle(request, response);
				}
			} else {
				DefaultSecureAuth defaultSecureAuth = new DefaultSecureAuth();
				bodyMap = defaultSecureAuth.requestHandle(request, response);
			}

			HttpSession session = request.getSession(false);

			logger.info("User-Agent : [" + request.getHeader("User-Agent") + "]");

			logger.info("########################################");
			logger.info("##### uri : " + request.getRequestURI() );
			logger.info("##### session : " + session );
			logger.info("##### uzrNm : " + SessionUtils.getUserNm() );
			logger.info("##### empNo : " + SessionUtils.getEmpNo() );
			logger.info("##### ip : " + CommonUtils.getClientIP(request) );
			logger.info("########################################");

			request.setAttribute("http-body", bodyMap);
			request.setAttribute("head", bodyMap.get("head"));
			request.setAttribute("body", bodyMap.get("body"));
			request.setAttribute("rest_uri_path_att", pathVariables);

			// 세션 확인 필요 인터페이스
			if(!this.isPublic(request)) {

				//if( !"local".equals(server) ) {
					if( session == null ) {
						throw new InterceptorException("세션이 만료되었습니다. 다시 로그인 해주세요.");
					}
				//}
			}
		}

		logger.info("[ # preHandle - 2 # ]");

		return true;
	}


	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.info("[ # postHandle - 1 # ]");

		Map<String, Object> requestMap = null;
		Map<String, Object> requestHeadMap = null;
		String requestMethod = StringUtils.defaultString(request.getMethod());
		if (!requestMethod.equalsIgnoreCase(HttpMethod.POST.toString()) && !requestMethod.equalsIgnoreCase(HttpMethod.PUT.toString())) {
			requestMap = new HashMap();
			((Map) requestMap).put("head", new HashMap());

		} else {
			requestMap = (Map) request.getAttribute("http-body");
		}

		requestHeadMap = MapUtils.getMap((Map) requestMap, "head");
		logger.info("[ # requestHeadMap - 1 # ]" + requestHeadMap);
		Map<String, Object> responseMap = new HashMap();
		if (modelAndView != null) {
			responseMap = modelAndView.getModel();
		}

		Map<String, Object> responseHeadMap = MapUtils.getMap((Map) responseMap, "head");
		Object resultCodeObj = MapUtils.getObject((Map) responseHeadMap, "result_code");
		String resultCode = "";
		if (resultCodeObj == null) {
			resultCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
			if (MapUtils.isEmpty((Map) responseHeadMap)) {
				responseHeadMap = new HashMap();
			}

			((Map) responseHeadMap).put("result_code", resultCode);
			((Map) responseHeadMap).put("result_msg", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			((Map) responseMap).put("head", responseHeadMap);

		} else {
			resultCode = String.valueOf(resultCodeObj);//(String) resultCodeObj;
		}

		if (requestMethod.equalsIgnoreCase(HttpMethod.POST.toString()) || requestMethod.equalsIgnoreCase(HttpMethod.PUT.toString())) {
			ObjectMapper objectMapper = new ObjectMapper();

			if( !"local".equals(server) ) {
				if(!this.isPublic(request)) {

					String bodyString = objectMapper.writeValueAsString(requestMap);
					String sAppId = MapUtils.getString(((Map) requestMap.get(Const.HEAD)), "appid", "");
					String userId = MapUtils.getString(((Map) requestMap.get(Const.BODY)), "empNo", "");
					String userNm = SessionUtils.getUserNm();

					logger.info((new StringBuilder()).append("[ # postHandle             - DEVICE REQUEST# ]=[").append(bodyString).append("]").toString());
					logger.info((new StringBuilder()).append("[ # requestHeadMap         - ]=[").append(requestHeadMap).append("]").toString());
					logger.info((new StringBuilder()).append("[ # requestBodyMap > appid - ]=[").append(sAppId).append("]").toString());
					logger.info((new StringBuilder()).append("[ # requestBodyMap > userId - ]=[").append(userId).append("]").toString());
					logger.info((new StringBuilder()).append("[ # log result             - ]=[").append((MapUtils.isNotEmpty(requestHeadMap) && eventLogUse && !"".equals(sAppId))).append("]").toString());

					if (MapUtils.isNotEmpty(requestHeadMap) && eventLogUse && !"".equals(sAppId)) {
						RequestVo requestVo = new RequestVo(bodyString, request);
						requestVo.setUserCompCode(request.getRequestURI().replaceAll(request.getContextPath(), ""));
						requestVo.getHead().setAppId(sAppId);
						requestVo.getHead().setUserId(userId);
						requestVo.getHead().setUserName(userNm);
						EventLogManager.getInstance().access(request, requestVo);
					}
				}
			}
//			String bodyString = objectMapper.writeValueAsString(requestMap);

//			logger.info("[ # postHandle - DEVICE REQUEST# ]=[" + bodyString + "]");


//			if (MapUtils.isNotEmpty(requestHeadMap) && this.eventLogUse) {
//				RequestVo requestVo = new RequestVo(bodyString, request);
//				requestVo.setUserCompCode(request.getRequestURI().replaceAll(request.getContextPath(), ""));
//				requestVo.getHead().setAppId((String) requestHeadMap.get("appid"));
//				EventLogManager.getInstance().access(request, requestVo);
//			}
		}

		try {
			this.secureAuth.responseHandle(request, response, (Map) responseMap);
		} catch (Exception var15) {
			response.sendError(417);
			var15.printStackTrace();
		}

		logger.info("[ # postHandle - 2 # ]");
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		logger.info("[ # afterCompletion # ]");
	}

}
