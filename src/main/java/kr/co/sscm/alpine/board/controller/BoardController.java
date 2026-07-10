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
import kr.co.sscm.common.base.BaseController;
import kr.co.sscm.common.exception.ApiException;
import kr.co.sscm.common.util.CommonUtils;
import kr.co.sscm.common.util.FileUploadUtils;

/** Board API controller for the Morpheus mobile app. */
@Controller
@RequestMapping("/api/alpine")
public class BoardController extends BaseController {

	@Autowired
	private BoardService boardService;

	/** Board list. The legacy /board POST call is also treated as list lookup. */
	@PostMapping(value = { "/board", "/board/getBoardList" }, produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> getBoardList(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);

		BoardSearchRequest request = new BoardSearchRequest();
		request.setBoardType(toString(bodyMap.get("boardType")));
		request.setYear(toString(bodyMap.get("year")));
		request.setKeyword(toString(bodyMap.get("keyword")));

		BoardListResponse response = boardService.getBoardList(request);
		return createMspResponse(requestMap, httpRequest, "200", "success", response.getList());
	}

	/** Board detail. View count is increased when detail is opened. */
	@PostMapping(value = "/board/getBoardDetail", produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> getBoardDetail(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		BoardDetailResponse response = boardService.getBoardDetail(toLong(bodyMap.get("boardNo")));
		return createMspResponse(requestMap, httpRequest, "200", "success", response);
	}

	/** Board insert with JSON request body. */
	@PostMapping(value = "/board/insertBoard", consumes = "application/json", produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> insertBoard(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		BoardSaveRequest request = createSaveRequest(getBodyMap(requestMap));
		AlpineSaveResponse response = boardService.insertBoard(request, CommonUtils.getClientIP(httpRequest));
		return createMspResponse(requestMap, httpRequest, "200", "success", response);
	}

	/** Board insert with multipart form-data and optional image files. */
	@PostMapping(value = "/board/insertBoard", consumes = "multipart/form-data", produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> insertBoardMultipart(HttpServletRequest httpRequest) throws ApiException {
		BoardSaveRequest request = createSaveRequest(getParameterMap(httpRequest));
		Map<String, Object> uploadedFileMap = FileUploadUtils.fileUpload(httpRequest);
		String fileGroupUuid = getFirstFileGroupUuid(uploadedFileMap);
		if (fileGroupUuid != null) {
			request.setAppendFileGroupUuid(fileGroupUuid);
		}

		AlpineSaveResponse response = boardService.insertBoard(request, CommonUtils.getClientIP(httpRequest), uploadedFileMap);
		return createMspResponse(null, httpRequest, "200", "success", response);
	}

	/** Board update. */
	@PostMapping(value = "/board/updateBoard", produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> updateBoard(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		BoardSaveRequest request = createSaveRequest(bodyMap);
		Long boardNo = toLong(bodyMap.get("boardNo"));
		Boolean response = boardService.updateBoard(boardNo, request, CommonUtils.getClientIP(httpRequest));
		return createMspResponse(requestMap, httpRequest, "200", "success", response);
	}

	/** Board delete. Rows are soft-deleted by USE_YN = N. */
	@PostMapping(value = "/board/deleteBoard", produces = "application/json; charset=utf8")
	public @ResponseBody Map<String, Object> deleteBoard(@RequestBody(required = false) Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		Map<String, Object> bodyMap = getBodyMap(requestMap);
		Boolean response = boardService.deleteBoard(toLong(bodyMap.get("boardNo")), toString(bodyMap.get("userNo")), CommonUtils.getClientIP(httpRequest));
		return createMspResponse(requestMap, httpRequest, "200", "success", response);
	}

	private Map<String, Object> createMspResponse(Map<String, Object> requestMap, HttpServletRequest httpRequest, String resultCode, String resultMsg, Object result) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> headMap = new HashMap<String, Object>();
		Map<String, Object> bodyMap = new HashMap<String, Object>();

		headMap.put("result_code", resultCode);
		headMap.put("result_msg", resultMsg);
		headMap.put("screen_id", getScreenId(requestMap, httpRequest));

		bodyMap.put("resultCode", resultCode);
		bodyMap.put("resultMsg", resultMsg);
		bodyMap.put("result", result);

		responseMap.put("head", headMap);
		responseMap.put("body", bodyMap);
		return responseMap;
	}

	@SuppressWarnings("unchecked")
	private String getScreenId(Map<String, Object> requestMap, HttpServletRequest httpRequest) {
		if (requestMap != null) {
			Object head = requestMap.get("head");
			if (head instanceof Map) {
				Object screenId = ((Map<String, Object>) head).get("screen_id");
				if (screenId != null) {
					return String.valueOf(screenId);
				}
			}
		}

		String screenId = httpRequest.getHeader("screen_id");
		return screenId == null ? "" : screenId;
	}

	@SuppressWarnings("unchecked")
	private String getFirstFileGroupUuid(Map<String, Object> uploadedFileMap) {
		if (uploadedFileMap == null || uploadedFileMap.isEmpty()) {
			return null;
		}
		for (Object value : uploadedFileMap.values()) {
			if (value instanceof Map) {
				Object fileGroupUuid = ((Map<String, Object>) value).get("FILE_GRP_UUID");
				if (fileGroupUuid != null) {
					return String.valueOf(fileGroupUuid);
				}
			}
		}
		return null;
	}

	private Map<String, Object> getParameterMap(HttpServletRequest request) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("boardNo", request.getParameter("boardNo"));
		paramMap.put("boardType", request.getParameter("boardType"));
		paramMap.put("title", request.getParameter("title"));
		paramMap.put("detail", request.getParameter("detail"));
		paramMap.put("appendFileGroupUuid", request.getParameter("appendFileGroupUuid"));
		paramMap.put("postDate", request.getParameter("postDate"));
		paramMap.put("writerEmpNo", request.getParameter("writerEmpNo"));
		paramMap.put("userNo", request.getParameter("userNo"));
		return paramMap;
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
