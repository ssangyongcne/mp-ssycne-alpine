package kr.co.sscm.alpine.auth.controller;

import java.util.HashMap;
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
import kr.co.sscm.alpine.auth.dto.AlpinePasswordChangeRequest;
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
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		AlpineLoginRequest request = new AlpineLoginRequest();
		request.setUserNo(toString(bodyMap.get("userNo")));
		request.setPw(toString(bodyMap.get("pw")));

		AlpineLoginResponse response = alpineAuthService.login(request);
		if (response == null) {
			return new ResponseEntity<ApiResponse<AlpineLoginResponse>>(ApiResponse.<AlpineLoginResponse>fail("401", "INVALID_LOGIN"), HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<ApiResponse<AlpineLoginResponse>>(new ApiResponse<AlpineLoginResponse>("200", "success", response), HttpStatus.OK);
	}

	@PostMapping(value = "/password/change", produces = "application/json; charset=utf8")
	public @ResponseBody ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody Map<String, Object> requestMap) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		AlpinePasswordChangeRequest request = new AlpinePasswordChangeRequest();
		request.setUserNo(toString(bodyMap.get("userNo")));
		request.setCurrentPw(toString(bodyMap.get("currentPw")));
		request.setNewPw(toString(bodyMap.get("newPw")));

		try {
			String result = alpineAuthService.changePassword(request);
			if (AlpineAuthService.CHANGE_SUCCESS.equals(result)) {
				return new ResponseEntity<ApiResponse<Void>>(new ApiResponse<Void>("200", "success", null), HttpStatus.OK);
			}
			if (AlpineAuthService.CHANGE_INVALID_POLICY.equals(result)) {
				return new ResponseEntity<ApiResponse<Void>>(ApiResponse.<Void>fail("400", "\uBE44\uBC00\uBC88\uD638\uB294 \uCD5C\uC18C 8\uC790 \uC774\uC0C1 \uC785\uB825\uD574 \uC8FC\uC138\uC694."), HttpStatus.BAD_REQUEST);
			}
			if (AlpineAuthService.CHANGE_INVALID_CURRENT_PASSWORD.equals(result)) {
				return new ResponseEntity<ApiResponse<Void>>(ApiResponse.<Void>fail("401", "\uD604\uC7AC \uBE44\uBC00\uBC88\uD638\uAC00 \uC77C\uCE58\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4."), HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<ApiResponse<Void>>(ApiResponse.<Void>fail("500", "\uC11C\uBC84 \uC624\uB958\uAC00 \uBC1C\uC0DD\uD588\uC2B5\uB2C8\uB2E4."), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse<Void>>(ApiResponse.<Void>fail("500", "\uC11C\uBC84 \uC624\uB958\uAC00 \uBC1C\uC0DD\uD588\uC2B5\uB2C8\uB2E4."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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