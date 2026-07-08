package kr.co.sscm.alpine.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.sscm.alpine.board.dto.BoardDetailResponse;
import kr.co.sscm.alpine.board.dto.BoardSaveRequest;
import kr.co.sscm.alpine.board.dto.BoardSummaryResponse;
import kr.co.sscm.common.base.BaseDao;

/** AP_BOARD MyBatis 매퍼 호출 전용 DAO. */
@Repository
public class BoardDao extends BaseDao {

	private String nameSpace = "ssyc.BoardDao";

	/** 목록 페이징/표시용 전체 건수 조회. */
	public int selectBoardCount(Map<String, Object> param) {
		return gwSqlSession.selectOne(nameSpace + ".selectBoardCount", param);
	}

	/** 목록 화면에 필요한 요약 정보만 조회한다. */
	public List<BoardSummaryResponse> selectBoardList(Map<String, Object> param) {
		return gwSqlSession.selectList(nameSpace + ".selectBoardList", param);
	}

	/** 상세 화면에 필요한 본문/첨부그룹/조회수를 조회한다. */
	public BoardDetailResponse selectBoardDetail(Long boardNo) {
		return gwSqlSession.selectOne(nameSpace + ".selectBoardDetail", boardNo);
	}

	/** 상세 조회 시 조회수를 1 증가시킨다. */
	public int updateBoardViewCount(Long boardNo) {
		return gwSqlSession.update(nameSpace + ".updateBoardViewCount", boardNo);
	}

	/** 신규 게시글 등록. 생성된 BOARD_NO는 BoardSaveRequest.boardNo에 매핑된다. */
	public int insertBoard(BoardSaveRequest request) {
		return gwSqlSession.insert(nameSpace + ".insertBoard", request);
	}

	/** 기존 게시글 수정. USE_YN = 'Y'인 글만 수정한다. */
	public int updateBoard(BoardSaveRequest request) {
		return gwSqlSession.update(nameSpace + ".updateBoard", request);
	}

	/** 기존 게시글 삭제 처리. 실제 DELETE가 아니라 USE_YN = 'N' 업데이트다. */
	public int deleteBoard(BoardSaveRequest request) {
		return gwSqlSession.update(nameSpace + ".deleteBoard", request);
	}
}

