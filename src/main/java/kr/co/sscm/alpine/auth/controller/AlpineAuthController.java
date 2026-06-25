package kr.co.sscm.alpine.auth.controller;

import java.util.Map;

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
import kr.co.sscm.alpine.auth.service.AlpineAuthService;
import kr.co.sscm.alpine.common.dto.ApiResponse;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineAuthController extends BaseController {

	@Autowired
	private AlpineAuthService alpineAuthService;

	@PostMapping(value = "/login", produces = "application/json; charset=utf8")
	public @ResponseBody ResponseEntity<ApiResponse<AlpineLoginResponse>> login(@RequestBody Map<String, Object> requestMap) {
		// TEMP: Remove after frontend login payload verification. Do not keep password logs in production.
		System.out.println("[ALPINE_LOGIN_REQUEST_MAP] " + requestMap);

		Map<String, Object> bodyMap = getBodyMap(requestMap);

		AlpineLoginRequest request = new AlpineLoginRequest();
		request.setUserNo(toString(bodyMap.get("userNo")));
		request.setPw(toString(bodyMap.get("pw")));

		AlpineLoginResponse response = alpineAuthService.login(request);
		if (response == null) {
			return new ResponseEntity<ApiResponse<AlpineLoginResponse>>(ApiResponse.<AlpineLoginResponse>fail("401", "INVALID_LOGIN"), HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<ApiResponse<AlpineLoginResponse>>(ApiResponse.success(response), HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getBodyMap(Map<String, Object> requestMap) {
		Object body = requestMap == null ? null : requestMap.get("body");
		if (body instanceof Map) {
			return (Map<String, Object>) body;
		}
		return requestMap;
	}

	private String toString(Object value) {
		return value == null ? null : String.valueOf(value);
	}
}


