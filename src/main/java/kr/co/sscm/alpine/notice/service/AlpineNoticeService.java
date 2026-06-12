package kr.co.sscm.alpine.notice.service;

import org.springframework.stereotype.Service;

import kr.co.sscm.alpine.board.dto.AlpineBoardSearchRequest;
import kr.co.sscm.alpine.common.dto.AlpineSaveResponse;
import kr.co.sscm.alpine.notice.dto.AlpineNoticeDetailResponse;
import kr.co.sscm.alpine.notice.dto.AlpineNoticeListResponse;
import kr.co.sscm.alpine.notice.dto.AlpineNoticeSaveRequest;
import kr.co.sscm.common.base.BaseService;

@Service
public class AlpineNoticeService extends BaseService {

	public AlpineNoticeListResponse getNoticeList(AlpineBoardSearchRequest request) {
		// TODO: Implement notice list lookup.
		return null;
	}

	public AlpineNoticeDetailResponse getNoticeDetail(Long noticeId) {
		// TODO: Implement notice detail lookup.
		return null;
	}

	public AlpineSaveResponse createNotice(AlpineNoticeSaveRequest request) {
		// TODO: Implement notice creation.
		return null;
	}

	public Boolean updateNotice(Long noticeId, AlpineNoticeSaveRequest request) {
		// TODO: Implement notice update.
		return Boolean.FALSE;
	}

	public Boolean deleteNotice(Long noticeId) {
		// TODO: Implement notice deletion.
		return Boolean.FALSE;
	}
}
