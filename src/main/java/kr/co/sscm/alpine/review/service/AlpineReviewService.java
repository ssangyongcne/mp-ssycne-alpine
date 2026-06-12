package kr.co.sscm.alpine.review.service;

import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.board.dto.AlpineBoardSearchRequest;
import kr.co.sscm.alpine.common.dto.AlpineSaveResponse;
import kr.co.sscm.alpine.review.dto.AlpineReviewDetailResponse;
import kr.co.sscm.alpine.review.dto.AlpineReviewListResponse;
import kr.co.sscm.alpine.review.dto.AlpineReviewSaveRequest;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineReviewService extends BaseService {

	public AlpineReviewListResponse getReviewList(AlpineBoardSearchRequest request) {
		// TODO: Implement review list lookup.
		return null;
	}

	public AlpineReviewDetailResponse getReviewDetail(Long reviewId) {
		// TODO: Implement review detail lookup.
		return null;
	}

	public AlpineSaveResponse createReview(AlpineReviewSaveRequest request) {
		// TODO: Implement review creation.
		return null;
	}

	public Boolean updateReview(Long reviewId, AlpineReviewSaveRequest request) {
		// TODO: Implement review update.
		return Boolean.FALSE;
	}

	public Boolean deleteReview(Long reviewId) {
		// TODO: Implement review deletion.
		return Boolean.FALSE;
	}
}
