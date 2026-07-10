package kr.co.sscm.alpine.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.sscm.alpine.board.dto.BoardAppndFileRequest;
import kr.co.sscm.alpine.board.dto.BoardAppndFileResponse;
import kr.co.sscm.alpine.board.dto.BoardDetailResponse;
import kr.co.sscm.alpine.board.dto.BoardSaveRequest;
import kr.co.sscm.alpine.board.dto.BoardSummaryResponse;
import kr.co.sscm.common.base.BaseDao;

/** AP_BOARD and attachment MyBatis mapper DAO. */
@Repository
public class BoardDao extends BaseDao {

	private String nameSpace = "ssyc.BoardDao";

	/** Count rows for list filtering. */
	public int selectBoardCount(Map<String, Object> param) {
		return gwSqlSession.selectOne(nameSpace + ".selectBoardCount", param);
	}

	/** Select summary rows for the list screen. */
	public List<BoardSummaryResponse> selectBoardList(Map<String, Object> param) {
		return gwSqlSession.selectList(nameSpace + ".selectBoardList", param);
	}

	/** Select detail body, attachment group, and view count. */
	public BoardDetailResponse selectBoardDetail(Long boardNo) {
		return gwSqlSession.selectOne(nameSpace + ".selectBoardDetail", boardNo);
	}

	/** Select attachment metadata rows by group UUID in sort order. */
	public List<BoardAppndFileResponse> selectAppndFileList(String appndFileGroupUuid) {
		return gwSqlSession.selectList(nameSpace + ".selectAppndFileList", appndFileGroupUuid);
	}

	/** Increase view count by one. */
	public int updateBoardViewCount(Long boardNo) {
		return gwSqlSession.update(nameSpace + ".updateBoardViewCount", boardNo);
	}

	/** Insert a board row. Generated BOARD_NO is mapped to request.boardNo. */
	public int insertBoard(BoardSaveRequest request) {
		return gwSqlSession.insert(nameSpace + ".insertBoard", request);
	}

	/** Insert attachment file metadata. */
	public int insertAppndFile(BoardAppndFileRequest request) {
		return gwSqlSession.insert(nameSpace + ".insertAppndFile", request);
	}

	/** Update an existing active board row. */
	public int updateBoard(BoardSaveRequest request) {
		return gwSqlSession.update(nameSpace + ".updateBoard", request);
	}

	/** Soft-delete an existing board row. */
	public int deleteBoard(BoardSaveRequest request) {
		return gwSqlSession.update(nameSpace + ".deleteBoard", request);
	}
}