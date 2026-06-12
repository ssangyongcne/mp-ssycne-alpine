package kr.co.sscm.alpine.review.dto;

import java.util.List;

import kr.co.sscm.alpine.common.dto.AlpinePhotoResponse;

public class AlpineReviewDetailResponse {

	private Long reviewId;
	private String title;
	private String content;
	private String writer;
	private String regDate;
	private List<AlpinePhotoResponse> photos;

	public Long getReviewId() { return reviewId; }
	public void setReviewId(Long reviewId) { this.reviewId = reviewId; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public String getWriter() { return writer; }
	public void setWriter(String writer) { this.writer = writer; }
	public String getRegDate() { return regDate; }
	public void setRegDate(String regDate) { this.regDate = regDate; }
	public List<AlpinePhotoResponse> getPhotos() { return photos; }
	public void setPhotos(List<AlpinePhotoResponse> photos) { this.photos = photos; }
}
