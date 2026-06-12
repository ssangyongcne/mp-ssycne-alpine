package kr.co.sscm.alpine.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.admin.dto.AlpinePasswordResetRequest;
import kr.co.sscm.alpine.admin.service.AlpineAdminService;
import kr.co.sscm.alpine.common.dto.ApiResponse;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineAdminController extends BaseController {

	@Autowired
	private AlpineAdminService alpineAdminService;

	@PostMapping(value = "/admin/password/reset", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> resetPassword(@RequestBody AlpinePasswordResetRequest request) {
		return ApiResponse.success(alpineAdminService.resetPassword(request));
	}
}
