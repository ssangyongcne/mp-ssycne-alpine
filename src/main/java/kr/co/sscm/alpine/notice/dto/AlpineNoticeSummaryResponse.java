package kr.co.sscm.alpine.notice.dto;

public class AlpineNoticeSummaryResponse {

	private Long noticeId;
	private String title;
	private String writer;
	private String postDate;

	public Long getNoticeId() { return noticeId; }
	public void setNoticeId(Long noticeId) { this.noticeId = noticeId; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getWriter() { return writer; }
	public void setWriter(String writer) { this.writer = writer; }
	public String getPostDate() { return postDate; }
	public void setPostDate(String postDate) { this.postDate = postDate; }
}
