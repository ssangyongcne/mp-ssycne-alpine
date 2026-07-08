package kr.co.sscm.alpine.board.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sscm.alpine.board.dto.BoardDetailResponse;
import kr.co.sscm.alpine.board.dto.BoardListResponse;
import kr.co.sscm.alpine.board.dto.BoardSaveRequest;
import kr.co.sscm.alpine.board.dto.BoardSearchRequest;
import kr.co.sscm.alpine.board.service.BoardService;
import kr.co.sscm.alpine.common.dto.AlpineSaveResponse;
import kr.co.sscm.alpine.common.dto.ApiResponse;
import kr.co.sscm.common.base.BaseController;
import kr.co.sscm.common.util.CommonUtils;

/**
 * 산악회 게시판 API 컨트롤러.
 * 모바일 MNet 호출 방식에 맞춰 모든 게시판 API를 POST로 받는다.
 */
@Controller
@RequestMapping("/api/alpine")
public class BoardController extends BaseController {

	@Autowired
	private BoardService boardService;

	/** 게시판 목록 조회. 기존 /board POST 호출도 목록조회로 처리한다. */
	@PostMapping(value = { "/board", "/board/getBoardList" }, produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<BoardListResponse> getBoardList(@RequestBody(required = false) Map<String, Object> requestMap) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		BoardSearchRequest request = new BoardSearchRequest();
		request.setBoardType(toString(bodyMap.get("boardType")));
		request.setYear(toString(bodyMap.get("year")));
		request.setKeyword(toString(bodyMap.get("keyword")));

		return ApiResponse.success(boardService.getBoardList(request));
	}

	/** 게시글 상세 조회. 상세 진입 시 조회수도 함께 증가한다. */
	@PostMapping(value = "/board/getBoardDetail", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<BoardDetailResponse> getBoardDetail(@RequestBody(required = false) Map<String, Object> requestMap) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		return ApiResponse.success(boardService.getBoardDetail(toLong(bodyMap.get("boardNo"))));
	}

	/** 게시글 등록. 등록자 IP는 서버에서 request 기준으로 세팅한다. */
	@PostMapping(value = "/board/insertBoard", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<AlpineSaveResponse> insertBoard(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		BoardSaveRequest request = createSaveRequest(getBodyMap(requestMap));
		return ApiResponse.success(boardService.insertBoard(request, CommonUtils.getClientIP(httpRequest)));
	}

	/** 게시글 수정. 요청 body의 boardNo를 기준으로 수정 대상을 확정한다. */
	@PostMapping(value = "/board/updateBoard", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> updateBoard(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		BoardSaveRequest request = createSaveRequest(bodyMap);
		Long boardNo = toLong(bodyMap.get("boardNo"));
		return ApiResponse.success(boardService.updateBoard(boardNo, request, CommonUtils.getClientIP(httpRequest)));
	}

	/** 게시글 삭제. 실제 row 삭제가 아니라 USE_YN = 'N'으로 비활성 처리한다. */
	@PostMapping(value = "/board/deleteBoard", produces = "application/json; charset=utf8")
	public @ResponseBody ApiResponse<Boolean> deleteBoard(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		return ApiResponse.success(boardService.deleteBoard(toLong(bodyMap.get("boardNo")), toString(bodyMap.get("userNo")), CommonUtils.getClientIP(httpRequest)));
	}

	private BoardSaveRequest createSaveRequest(Map<String, Object> bodyMap) {
		BoardSaveRequest request = new BoardSaveRequest();
		request.setBoardNo(toLong(bodyMap.get("boardNo")));
		request.setBoardType(toString(bodyMap.get("boardType")));
		request.setTitle(toString(bodyMap.get("title")));
		request.setDetail(toString(bodyMap.get("detail")));
		request.setAppendFileGroupUuid(toString(bodyMap.get("appendFileGroupUuid")));
		request.setPostDate(toString(bodyMap.get("postDate")));
		request.setWriterEmpNo(toString(bodyMap.get("writerEmpNo")));
		request.setUserNo(toString(bodyMap.get("userNo")));
		return request;
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

	private Long toLong(Object value) {
		if (value instanceof Number) {
			return Long.valueOf(((Number) value).longValue());
		}
		String stringValue = toString(value);
		if (stringValue == null || stringValue.trim().length() == 0) {
			return null;
		}
		return Long.valueOf(stringValue);
	}
}
