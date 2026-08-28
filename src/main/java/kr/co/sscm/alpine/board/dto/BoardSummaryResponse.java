package kr.co.sscm.alpine.board.dto;

/** Board list row response DTO. */
public class BoardSummaryResponse { 

	private Long boardNo;        // Board number
	private String boardType;    // Board type: N notice, R review
	private Long eventNo;    	 // eventNo : 산행차수 
	private String title;        // Title
	private String writer;       // Writer name or employee number
	private String postDate;     // Post date yyyyMMdd
	private String writerNm;     // Morpheus app field: writer name
	private String regDate;      // Morpheus app field: registration date
	private Integer viewCnt;     // View count

	public Long getBoardNo() { return boardNo; }
	public void setBoardNo(Long boardNo) { this.boardNo = boardNo; }
	public String getBoardType() { return boardType; }
	public void setBoardType(String boardType) { this.boardType = boardType; }
	public Long getEventNo() { return eventNo; }
	public void setEventNo(Long eventNo) { this.eventNo = eventNo; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getWriter() { return writer; }
	public void setWriter(String writer) { this.writer = writer; }
	public String getPostDate() { return postDate; }
	public void setPostDate(String postDate) { this.postDate = postDate; }
	public String getWriterNm() { return writerNm; }
	public void setWriterNm(String writerNm) { this.writerNm = writerNm; }
	public String getRegDate() { return regDate; }
	public void setRegDate(String regDate) { this.regDate = regDate; }
	public Integer getViewCnt() { return viewCnt; }
	public void setViewCnt(Integer viewCnt) { this.viewCnt = viewCnt; }
}