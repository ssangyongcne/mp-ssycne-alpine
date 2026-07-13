package kr.co.sscm.alpine.board.dto;

import java.util.List;

/** Board detail response DTO. */
public class BoardDetailResponse {

	private Long boardNo;                              // Board number
	private String boardType;                          // Board type(N: notice, R: review)
	private String title;                              // Title
	private String detail;                             // Content body
	private String appendFileGroupUuid;                // Attachment file group UUID
	private String postDate;                           // Post date(yyyyMMdd)
	private String regDate;                            // Registration date time(yyyyMMddHHmmss)
	private String writerEmpNo;                        // Writer employee number
	private String writer;                             // Writer name or employee number
	private String writerNm;                           // Writer name
	private Integer viewCount;                         // View count
	private List<BoardAppndFileResponse> appendFiles;  // Attachment files ordered by sortOrder

	public Long getBoardNo() { return boardNo; }
	public void setBoardNo(Long boardNo) { this.boardNo = boardNo; }
	public String getBoardType() { return boardType; }
	public void setBoardType(String boardType) { this.boardType = boardType; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDetail() { return detail; }
	public void setDetail(String detail) { this.detail = detail; }
	public String getContent() { return detail; }
	public void setContent(String content) { this.detail = content; }
	public String getAppendFileGroupUuid() { return appendFileGroupUuid; }
	public void setAppendFileGroupUuid(String appendFileGroupUuid) { this.appendFileGroupUuid = appendFileGroupUuid; }
	public String getPostDate() { return postDate; }
	public void setPostDate(String postDate) { this.postDate = postDate; }
	public String getRegDate() { return regDate; }
	public void setRegDate(String regDate) { this.regDate = regDate; }
	public String getWriterEmpNo() { return writerEmpNo; }
	public void setWriterEmpNo(String writerEmpNo) { this.writerEmpNo = writerEmpNo; }
	public String getWriter() { return writer; }
	public void setWriter(String writer) { this.writer = writer; }
	public String getWriterNm() { return writerNm; }
	public void setWriterNm(String writerNm) { this.writerNm = writerNm; }
	public Integer getViewCount() { return viewCount; }
	public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
	public List<BoardAppndFileResponse> getAppendFiles() { return appendFiles; }
	public void setAppendFiles(List<BoardAppndFileResponse> appendFiles) { this.appendFiles = appendFiles; }
}