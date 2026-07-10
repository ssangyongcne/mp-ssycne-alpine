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
import kr.co.sscm.alpine.board.dto.BoardAppndFileRequest;
import kr.co.sscm.alpine.board.dto.BoardDetailResponse;
import kr.co.sscm.alpine.board.dto.BoardListResponse;
import kr.co.sscm.alpine.board.dto.BoardSaveRequest;
import kr.co.sscm.alpine.board.dto.BoardSearchRequest;
import kr.co.sscm.alpine.board.dto.BoardSummaryResponse;
import kr.co.sscm.alpine.common.dto.AlpineSaveResponse;
import kr.co.sscm.common.base.BaseService;

/** Board service. Notice/review rows share AP_BOARD and are separated by boardType. */
@Service
public class BoardService extends BaseService {

	@Autowired
	private BoardDao boardDao;

	/** Select list rows and total count with the same search condition. */
	public BoardListResponse getBoardList(BoardSearchRequest request) {
		Map<String, Object> param = createSearchParam(request);
		List<BoardSummaryResponse> list = boardDao.selectBoardList(param);

		BoardListResponse response = new BoardListResponse();
		response.setTotalCount(boardDao.selectBoardCount(param));
		response.setList(list);
		return response;
	}

	/** Detail lookup increases the view count in the same transaction. */
	@Transactional(transactionManager = "transactionManager1")
	public BoardDetailResponse getBoardDetail(Long boardNo) {
		boardDao.updateBoardViewCount(boardNo);
		return boardDao.selectBoardDetail(boardNo);
	}

	/** Insert a board row without uploaded files. */
	@Transactional(transactionManager = "transactionManager1")
	public AlpineSaveResponse insertBoard(BoardSaveRequest request, String clientIp) {
		prepareSaveRequest(request, clientIp);
		boardDao.insertBoard(request);

		AlpineSaveResponse response = new AlpineSaveResponse();
		response.setId(request.getBoardNo());
		return response;
	}

	/** Insert a board row and its attachment metadata in one DB transaction. */
	@Transactional(transactionManager = "transactionManager1")
	public AlpineSaveResponse insertBoard(BoardSaveRequest request, String clientIp, Map<String, Object> uploadedFileMap) {
		prepareSaveRequest(request, clientIp);
		boardDao.insertBoard(request);
		insertAppndFiles(request, uploadedFileMap);

		AlpineSaveResponse response = new AlpineSaveResponse();
		response.setId(request.getBoardNo());
		return response;
	}

	/** Update target boardNo is taken from the path/controller argument. */
	@Transactional(transactionManager = "transactionManager1")
	public Boolean updateBoard(Long boardNo, BoardSaveRequest request, String clientIp) {
		prepareSaveRequest(request, clientIp);
		request.setBoardNo(boardNo);
		return boardDao.updateBoard(request) > 0;
	}

	/** Soft delete by setting AP_BOARD.USE_YN to N. */
	@Transactional(transactionManager = "transactionManager1")
	public Boolean deleteBoard(Long boardNo, String userNo, String clientIp) {
		BoardSaveRequest request = new BoardSaveRequest();
		request.setBoardNo(boardNo);
		request.setUserNo(defaultString(userNo, "system"));
		request.setClientIp(defaultString(clientIp, "127.0.0.1"));
		return boardDao.deleteBoard(request) > 0;
	}

	/** Store metadata rows returned by FileUploadUtils.fileUpload. */
	@SuppressWarnings("unchecked")
	private void insertAppndFiles(BoardSaveRequest request, Map<String, Object> uploadedFileMap) {
		if (uploadedFileMap == null || uploadedFileMap.isEmpty()) {
			return;
		}

		for (Map.Entry<String, Object> entry : uploadedFileMap.entrySet()) {
			if (!(entry.getValue() instanceof Map)) {
				continue;
			}

			Map<String, Object> fileInfo = (Map<String, Object>) entry.getValue();
			BoardAppndFileRequest appndFile = new BoardAppndFileRequest();
			appndFile.setAppndFileUuid(toStringValue(fileInfo.get("FILE_UUID")));
			appndFile.setAppndFileBassCnts("AP_BOARD:" + request.getBoardNo());
			appndFile.setAppndFileGropUuid(defaultString(request.getAppendFileGroupUuid(), toStringValue(fileInfo.get("FILE_GRP_UUID"))));
			appndFile.setAppndFilePathCnts(toStringValue(fileInfo.get("FILE_POS")));
			appndFile.setAppndFileOrigNm(toStringValue(fileInfo.get("FILE_ORIGI_NM")));
			appndFile.setAppndFileTransmsNm(toStringValue(fileInfo.get("FILE_NM_UUID")));
			appndFile.setAppndFileFlextNm(toStringValue(fileInfo.get("FILE_EXT")));
			appndFile.setAppndFileMimeCnts(toStringValue(fileInfo.get("MIME_TYPE")));
			appndFile.setAppndFileSiz(toIntegerValue(fileInfo.get("FILE_SIZE")));
			appndFile.setAppndFileParmNm(entry.getKey());
			appndFile.setUserNo(request.getUserNo());
			appndFile.setClientIp(request.getClientIp());
			boardDao.insertAppndFile(appndFile);
		}
	}

	/** Create the MyBatis search parameter map. */
	private Map<String, Object> createSearchParam(BoardSearchRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (request != null) {
			param.put("boardType", request.getBoardType());
			param.put("year", request.getYear());
			param.put("keyword", request.getKeyword());
		}
		return param;
	}

	/** Fill common save values that the client may omit. */
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

	private String toStringValue(Object value) {
		return value == null ? null : String.valueOf(value);
	}

	private Integer toIntegerValue(Object value) {
		if (value instanceof Number) {
			return Integer.valueOf(((Number) value).intValue());
		}
		String stringValue = toStringValue(value);
		if (!StringUtils.hasText(stringValue)) {
			return null;
		}
		return Integer.valueOf(stringValue);
	}
}