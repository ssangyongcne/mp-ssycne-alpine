package kr.co.sscm.alpine.board.dto;

import java.util.List;

/** 게시판 목록 응답 DTO. */
public class BoardListResponse {

	private Integer totalCount;                 // 검색 조건 기준 전체 건수
	private List<BoardSummaryResponse> list;    // 목록 표시용 게시글 요약 목록

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
	public List<BoardSummaryResponse> getList() { return list; }
	public void setList(List<BoardSummaryResponse> list) { this.list = list; }
}
