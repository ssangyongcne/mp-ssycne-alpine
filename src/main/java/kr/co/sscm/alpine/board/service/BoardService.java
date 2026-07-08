package kr.co.sscm.alpine.board.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import kr.co.sscm.alpine.board.dao.BoardDao;
import kr.co.sscm.alpine.board.dto.BoardDetailResponse;
import kr.co.sscm.alpine.board.dto.BoardListResponse;
import kr.co.sscm.alpine.board.dto.BoardSaveRequest;
import kr.co.sscm.alpine.board.dto.BoardSearchRequest;
import kr.co.sscm.alpine.board.dto.BoardSummaryResponse;
import kr.co.sscm.alpine.common.dto.AlpineSaveResponse;
import kr.co.sscm.common.base.BaseService;

/** 게시판 업무 로직. 공지/후기는 boardType으로 구분하고 같은 AP_BOARD 테이블을 사용한다. */
@Service
public class BoardService extends BaseService {

	@Autowired
	private BoardDao boardDao;

	/** 목록과 전체 건수를 같은 검색 조건으로 조회한다. */
	public BoardListResponse getBoardList(BoardSearchRequest request) {
		Map<String, Object> param = createSearchParam(request);
		List<BoardSummaryResponse> list = boardDao.selectBoardList(param);

		BoardListResponse response = new BoardListResponse();
		response.setTotalCount(boardDao.selectBoardCount(param));
		response.setList(list);
		return response;
	}

	/** 상세 조회는 조회수 증가와 조회 결과가 같은 트랜잭션 안에서 처리된다. */
	@Transactional(transactionManager = "transactionManager1")
	public BoardDetailResponse getBoardDetail(Long boardNo) {
		boardDao.updateBoardViewCount(boardNo);
		return boardDao.selectBoardDetail(boardNo);
	}

	/** 등록 전 게시일자, 작성자, IP 같은 공통 저장값을 보정한다. */
	@Transactional(transactionManager = "transactionManager1")
	public AlpineSaveResponse insertBoard(BoardSaveRequest request, String clientIp) {
		prepareSaveRequest(request, clientIp);
		boardDao.insertBoard(request);

		AlpineSaveResponse response = new AlpineSaveResponse();
		response.setId(request.getBoardNo());
		return response;
	}

	/** 수정 대상 번호는 요청 본문보다 URL path 값을 우선한다. */
	@Transactional(transactionManager = "transactionManager1")
	public Boolean updateBoard(Long boardNo, BoardSaveRequest request, String clientIp) {
		prepareSaveRequest(request, clientIp);
		request.setBoardNo(boardNo);
		return boardDao.updateBoard(request) > 0;
	}

	/** 삭제는 AP_BOARD.USE_YN 값을 N으로 바꾸는 소프트 삭제다. */
	@Transactional(transactionManager = "transactionManager1")
	public Boolean deleteBoard(Long boardNo, String userNo, String clientIp) {
		BoardSaveRequest request = new BoardSaveRequest();
		request.setBoardNo(boardNo);
		request.setUserNo(defaultString(userNo, "system"));
		request.setClientIp(defaultString(clientIp, "127.0.0.1"));
		return boardDao.deleteBoard(request) > 0;
	}

	/** MyBatis XML에서 쓰는 검색 조건 Map을 만든다. */
	private Map<String, Object> createSearchParam(BoardSearchRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (request != null) {
			param.put("boardType", request.getBoardType());
			param.put("year", request.getYear());
			param.put("keyword", request.getKeyword());
		}
		return param;
	}

	/** 클라이언트가 생략할 수 있는 저장 공통값을 서버 기준 기본값으로 채운다. */
	private void prepareSaveRequest(BoardSaveRequest request, String clientIp) {
		if (!StringUtils.hasText(request.getPostDate())) {
			request.setPostDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		}
		if (!StringUtils.hasText(request.getWriterEmpNo())) {
			request.setWriterEmpNo(defaultString(request.getUserNo(), "system"));
		}
		request.setUserNo(defaultString(request.getUserNo(), request.getWriterEmpNo()));
		request.setClientIp(defaultString(clientIp, "127.0.0.1"));
	}

	private String defaultString(String value, String defaultValue) {
		return StringUtils.hasText(value) ? value : defaultValue;
	}
}

