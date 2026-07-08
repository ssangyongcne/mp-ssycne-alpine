package kr.co.sscm.alpine.board.dto;

/** 게시판 상세 응답 DTO. */
public class BoardDetailResponse {

	private Long boardNo;                  // 게시글번호
	private String boardType;              // 게시판구분(N: 공지, R: 후기)
	private String title;                  // 제목
	private String detail;                 // 본문
	private String appendFileGroupUuid;    // 첨부파일그룹UUID
	private String postDate;               // 게시일자(yyyyMMdd)
	private String writerEmpNo;            // 게시자 사번
	private String writer;                 // 게시자명. 사용자명이 없으면 사번으로 내려간다.
	private Integer viewCount;             // 조회수

	public Long getBoardNo() { return boardNo; }
	public void setBoardNo(Long boardNo) { this.boardNo = boardNo; }
	public String getBoardType() { return boardType; }
	public void setBoardType(String boardType) { this.boardType = boardType; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDetail() { return detail; }
	public void setDetail(String detail) { this.detail = detail; }
	public String getAppendFileGroupUuid() { return appendFileGroupUuid; }
	public void setAppendFileGroupUuid(String appendFileGroupUuid) { this.appendFileGroupUuid = appendFileGroupUuid; }
	public String getPostDate() { return postDate; }
	public void setPostDate(String postDate) { this.postDate = postDate; }
	public String getWriterEmpNo() { return writerEmpNo; }
	public void setWriterEmpNo(String writerEmpNo) { this.writerEmpNo = writerEmpNo; }
	public String getWriter() { return writer; }
	public void setWriter(String writer) { this.writer = writer; }
	public Integer getViewCount() { return viewCount; }
	public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
}
