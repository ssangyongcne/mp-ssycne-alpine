package kr.co.sscm.alpine.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.admin.dto.AdminPasswordResetRequest;
import kr.co.sscm.alpine.admin.dto.AdminUserListResponse;
import kr.co.sscm.alpine.admin.dto.AdminUserSearchRequest;
import kr.co.sscm.alpine.admin.service.AdminService;
import kr.co.sscm.alpine.common.dto.ApiResponse;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AdminController extends BaseController {

	@Autowired
	private AdminService adminService;

	@PostMapping(value = "/admin/user/getUserList", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AdminUserListResponse> getUserList(@RequestBody(required = false) Map<String, Object> requestMap) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		AdminUserSearchRequest request = new AdminUserSearchRequest();
		request.setUserNo(toString(bodyMap.get("userNo")));
		request.setUserNm(toString(bodyMap.get("userNm")));
		request.setDeptNm(toString(bodyMap.get("deptNm")));
		request.setAuth(toString(bodyMap.get("auth")));
		request.setUseYn(toString(bodyMap.get("useYn")));

		return ApiResponse.success(adminService.getUserList(request));
	}

	@PostMapping(value = "/admin/password/reset", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> resetPassword(@RequestBody(required = false) Map<String, Object> requestMap) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		AdminPasswordResetRequest request = new AdminPasswordResetRequest();
		request.setTargetEmpNo(toString(bodyMap.get("targetEmpNo")));
		request.setAdminEmpNo(toString(bodyMap.get("adminEmpNo")));
		return ApiResponse.success(adminService.resetPassword(request));
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
