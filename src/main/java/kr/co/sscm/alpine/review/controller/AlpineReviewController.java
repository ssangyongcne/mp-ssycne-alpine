package kr.co.sscm.alpine.review.controller;

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
import kr.co.sscm.alpine.review.dto.AlpineReviewDetailResponse;
import kr.co.sscm.alpine.review.dto.AlpineReviewListResponse;
import kr.co.sscm.alpine.review.dto.AlpineReviewSaveRequest;
import kr.co.sscm.alpine.review.service.AlpineReviewService;
import kr.co.sscm.common.base.BaseController;

@Controller
@RequestMapping("/api/alpine")
public class AlpineReviewController extends BaseController {

	@Autowired
	private AlpineReviewService alpineReviewService;

	@GetMapping(value = "/reviews", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AlpineReviewListResponse> getReviewList(@ModelAttribute AlpineBoardSearchRequest request) {
		return ApiResponse.success(alpineReviewService.getReviewList(request));
	}

	@GetMapping(value = "/reviews/{reviewId}", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AlpineReviewDetailResponse> getReviewDetail(@PathVariable Long reviewId) {
		return ApiResponse.success(alpineReviewService.getReviewDetail(reviewId));
	}

	@PostMapping(value = "/reviews", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AlpineSaveResponse> createReview(@RequestBody AlpineReviewSaveRequest request) {
		return ApiResponse.success(alpineReviewService.createReview(request));
	}

	@PutMapping(value = "/reviews/{reviewId}", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> updateReview(@PathVariable Long reviewId, @RequestBody AlpineReviewSaveRequest request) {
		return ApiResponse.success(alpineReviewService.updateReview(reviewId, request));
	}

	@DeleteMapping(value = "/reviews/{reviewId}", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> deleteReview(@PathVariable Long reviewId) {
		return ApiResponse.success(alpineReviewService.deleteReview(reviewId));
	}
}
