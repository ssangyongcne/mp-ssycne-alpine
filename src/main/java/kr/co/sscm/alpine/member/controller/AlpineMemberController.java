package kr.co.sscm.alpine.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.common.dto.ApiResponse;
import kr.co.sscm.alpine.member.dto.AlpineMemberResponse;
import kr.co.sscm.alpine.member.service.AlpineMemberService;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineMemberController extends BaseController {

	@Autowired
	private AlpineMemberService alpineMemberService;

	@GetMapping(value = "/members", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<List<AlpineMemberResponse>> getMemberList() {
		return ApiResponse.success(alpineMemberService.getMemberList());
	}
}
