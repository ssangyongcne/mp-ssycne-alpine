package kr.co.sscm.alpine.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	public @ResponseBody ApiResponse<AlpineLoginResponse> login(@RequestBody AlpineLoginRequest request) {
		return ApiResponse.success(alpineAuthService.login(request));
	}
}
