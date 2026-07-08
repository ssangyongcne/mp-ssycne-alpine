package kr.co.sscm.alpine.board.dto;

/** 게시판 목록 한 건 응답 DTO. */
public class BoardSummaryResponse {

	private Long boardNo;        // 게시글번호
	private String boardType;    // 게시판구분(N: 공지, R: 후기)
	private String title;        // 제목
	private String writer;       // 게시자명. 사용자명이 없으면 사번으로 내려간다.
	private String postDate;     // 게시일자(yyyyMMdd)

	public Long getBoardNo() { return boardNo; }
	public void setBoardNo(Long boardNo) { this.boardNo = boardNo; }
	public String getBoardType() { return boardType; }
	public void setBoardType(String boardType) { this.boardType = boardType; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getWriter() { return writer; }
	public void setWriter(String writer) { this.writer = writer; }
	public String getPostDate() { return postDate; }
	public void setPostDate(String postDate) { this.postDate = postDate; }
}
