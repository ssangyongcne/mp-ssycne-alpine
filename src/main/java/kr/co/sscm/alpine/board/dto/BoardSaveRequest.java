package kr.co.sscm.alpine.board.dto;

/** Board save/update/delete request DTO. */
public class BoardSaveRequest {

	private Long boardNo;
	private String boardType;
	private String title;
	private String detail;
	private String appendFileGroupUuid;
	private String postDate;
	private String writerEmpNo;
	private String userNo;
	private String clientIp;
	private String deletedFileUuids;
	private String fileOrderTokens;

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
	public String getDeletedFileUuids() { return deletedFileUuids; }
	public void setDeletedFileUuids(String deletedFileUuids) { this.deletedFileUuids = deletedFileUuids; }
	public String getFileOrderTokens() { return fileOrderTokens; }
	public void setFileOrderTokens(String fileOrderTokens) { this.fileOrderTokens = fileOrderTokens; }
}