package kr.co.sscm.alpine.auth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.auth.dto.AlpineLoginRequest;
import kr.co.sscm.alpine.auth.dto.AlpineLoginResponse;
import kr.co.sscm.alpine.auth.dto.AlpinePasswordChangeRequest;
import kr.co.sscm.alpine.auth.service.AlpineAuthService;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineAuthController extends BaseController {

	@Autowired
	private AlpineAuthService alpineAuthService;

	@PostMapping(value = "/login", produces = "application/json; charset=utf8")
	public @ResponseBody ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		AlpineLoginRequest request = new AlpineLoginRequest();
		request.setUserNo(toString(bodyMap.get("userNo")));
		request.setPw(toString(bodyMap.get("pw")));

		AlpineLoginResponse response = alpineAuthService.login(request);
		if (response == null) {
			return new ResponseEntity<Map<String, Object>>(createMspResponse(requestMap, httpRequest, "401", "INVALID_LOGIN", null), HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Map<String, Object>>(createMspResponse(requestMap, httpRequest, "200", "success", response), HttpStatus.OK);
	}

	@PostMapping(value = "/password/change", produces = "application/json; charset=utf8")
	public @ResponseBody ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		AlpinePasswordChangeRequest request = new AlpinePasswordChangeRequest();
		request.setUserNo(toString(bodyMap.get("userNo")));
		request.setNewPw(toString(bodyMap.get("newPw")));

		try {
			String result = alpineAuthService.changePassword(request);
			if (AlpineAuthService.CHANGE_SUCCESS.equals(result)) {
				return new ResponseEntity<Map<String, Object>>(createMspResponse(requestMap, httpRequest, "200", "success", null), HttpStatus.OK);
			}
			if (AlpineAuthService.CHANGE_INVALID_POLICY.equals(result)) {
				return new ResponseEntity<Map<String, Object>>(createMspResponse(requestMap, httpRequest, "400", "비밀번호는 최소 8자 이상 입력해 주세요.", null), HttpStatus.OK);
			}

			return new ResponseEntity<Map<String, Object>>(createMspResponse(requestMap, httpRequest, "500", "서버 오류가 발생했습니다.", null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(createMspResponse(requestMap, httpRequest, "500", "서버 오류가 발생했습니다.", null), HttpStatus.OK);
		}
	}

	private Map<String, Object> createMspResponse(Map<String, Object> requestMap, HttpServletRequest httpRequest, String resultCode, String resultMsg, Object result) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> headMap = new HashMap<String, Object>();
		Map<String, Object> bodyMap = new HashMap<String, Object>();

		try {
			headMap.put("result_code", Integer.valueOf(resultCode));
		} catch (NumberFormatException e) {
			headMap.put("result_code", resultCode);
		}
		headMap.put("result_msg", resultMsg);
		headMap.put("screen_id", getScreenId(requestMap, httpRequest));

		bodyMap.put("resultCode", resultCode);
		bodyMap.put("resultMsg", resultMsg);
		bodyMap.put("result", result);

		responseMap.put("head", headMap);
		responseMap.put("body", bodyMap);
		return responseMap;
	}

	@SuppressWarnings("unchecked")
	private String getScreenId(Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		if (requestMap != null) {
			Object head = requestMap.get("head");
			if (head instanceof Map) {
				Object screenId = ((Map<String, Object>) head).get("screen_id");
				if (screenId != null) {
					return String.valueOf(screenId);
				}
			}
		}

		String screenId = httpRequest.getHeader("screen_id");
		return screenId == null ? "" : screenId;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getBodyMap(Map<String, Object> requestMap) {
		if (requestMap == null) {
			return new HashMap<String, Object>();
		}
		Object body = requestMap.get("body");
		if (body instanceof Map) {
			return (Map<String, Object>) body;
		}
		return requestMap;
	}

	private String toString(Object value) {
		return value == null ? null : String.valueOf(value);
	}
}
