package kr.co.sscm.alpine.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.board.dto.AlpineBoardSearchRequest;
import kr.co.sscm.alpine.common.dto.AlpineSaveResponse;
import kr.co.sscm.alpine.common.dto.ApiResponse;
import kr.co.sscm.alpine.notice.dto.AlpineNoticeDetailResponse;
import kr.co.sscm.alpine.notice.dto.AlpineNoticeListResponse;
import kr.co.sscm.alpine.notice.dto.AlpineNoticeSaveRequest;
import kr.co.sscm.alpine.notice.service.AlpineNoticeService;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineNoticeController extends BaseController {

	@Autowired
	private AlpineNoticeService alpineNoticeService;

	@GetMapping(value = "/notices", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AlpineNoticeListResponse> getNoticeList(@ModelAttribute AlpineBoardSearchRequest request) {
		return ApiResponse.success(alpineNoticeService.getNoticeList(request));
	}

	@GetMapping(value = "/notices/{noticeId}", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AlpineNoticeDetailResponse> getNoticeDetail(@PathVariable Long noticeId) {
		return ApiResponse.success(alpineNoticeService.getNoticeDetail(noticeId));
	}

	@PostMapping(value = "/notices", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AlpineSaveResponse> createNotice(@RequestBody AlpineNoticeSaveRequest request) {
		return ApiResponse.success(alpineNoticeService.createNotice(request));
	}

	@PutMapping(value = "/notices/{noticeId}", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> updateNotice(@PathVariable Long noticeId, @RequestBody AlpineNoticeSaveRequest request) {
		return ApiResponse.success(alpineNoticeService.updateNotice(noticeId, request));
	}

	@DeleteMapping(value = "/notices/{noticeId}", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> deleteNotice(@PathVariable Long noticeId) {
		return ApiResponse.success(alpineNoticeService.deleteNotice(noticeId));
	}
}
