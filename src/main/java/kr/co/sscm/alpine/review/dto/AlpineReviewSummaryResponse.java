package kr.co.sscm.alpine.review.dto;

public class AlpineReviewSummaryResponse {

	private Long reviewId;
	private String title;
	private String writer;
	private String postDate;

	public Long getReviewId() { return reviewId; }
	public void setReviewId(Long reviewId) { this.reviewId = reviewId; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getWriter() { return writer; }
	public void setWriter(String writer) { this.writer = writer; }
	public String getPostDate() { return postDate; }
	public void setPostDate(String postDate) { this.postDate = postDate; }
}
