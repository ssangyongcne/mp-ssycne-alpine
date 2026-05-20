package kr.co.sscm.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.sscm.common.Constants;
import kr.co.sscm.common.dto.LoginInfoDto;

/**
 * @FileName SessionUtils.java
 * @comment Session util
 * @author AJH
 */
@Component
public class SessionUtils {

	private static Logger logger = LoggerFactory.getLogger(SessionUtils.class);

	/**
	 * 로그인 정보 조회
	 * @return
	 */
	public static LoginInfoDto getLoginInfo() {

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession(false);

		if(session != null) {
			LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(Constants.LOGIN_INFO);
			return loginInfo;
		}else {
			return null;
		}
	}

	/**
	 * 세션 생성
	 * @param loginInfo
	 */
	public static void setLoginInfo(LoginInfoDto loginInfo) {

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(3600);	// 세션시간 1시간 설정
		session.setAttribute(Constants.LOGIN_INFO, loginInfo);
	}

	/**
	 * 세션 사원번호 조회
	 * @return
	 */
	public static String getEmpNo() {
		LoginInfoDto loginInfo = SessionUtils.getLoginInfo();
		if(loginInfo != null) {
			return loginInfo.getEmpNo();
		}
		return null;
	}

	/**
	 * 세션 사용자 성명 조회
	 * @return
	 */
	public static String getUserNm() {
		LoginInfoDto loginInfo = SessionUtils.getLoginInfo();
		if(loginInfo != null) {
			return loginInfo.getUzrNm();
		}
		return null;
	}

	public static String getIP() {
		LoginInfoDto loginInfo = SessionUtils.getLoginInfo();
		if(loginInfo != null) {
			return loginInfo.getIp();
		}
		return null;
	}

}
