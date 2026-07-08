package kr.co.sscm.alpine.board.dto;

/** 게시글 등록/수정/삭제 요청 DTO. */
public class BoardSaveRequest {

	private Long boardNo;                 // 게시글번호
	private String boardType;             // 게시판구분(N: 공지, R: 후기)
	private String title;                 // 게시판제목내용
	private String detail;                // 게시판상세내용
	private String appendFileGroupUuid;   // 첨부파일그룹UUID
	private String postDate;              // 게시판게시일자(yyyyMMdd)
	private String writerEmpNo;           // 게시판게시자 사번
	private String userNo;                // 등록/수정/삭제 처리자
	private String clientIp;              // 등록/수정/삭제 처리자 IP

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
	public String getUserNo() { return userNo; }
	public void setUserNo(String userNo) { this.userNo = userNo; }
	public String getClientIp() { return clientIp; }
	public void setClientIp(String clientIp) { this.clientIp = clientIp; }
}
