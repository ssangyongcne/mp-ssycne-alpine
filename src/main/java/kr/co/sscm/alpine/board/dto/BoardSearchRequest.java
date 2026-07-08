package kr.co.sscm.alpine.board.dto;

/** 게시판 목록 검색 조건 DTO. */
public class BoardSearchRequest {

	private String boardType;  // 게시판구분(N: 공지, R: 후기)
	private String year;       // 게시연도(yyyy)
	private String keyword;    // 제목/본문 검색어

	public String getBoardType() {
		return boardType;
	}

	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
