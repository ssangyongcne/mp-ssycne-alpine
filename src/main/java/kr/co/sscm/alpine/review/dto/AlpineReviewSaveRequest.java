package kr.co.sscm.alpine.review.dto;

import java.util.List;

import kr.co.sscm.alpine.common.dto.AlpinePhotoResponse;

public class AlpineReviewSaveRequest {

	private String title;
	private String content;
	private String writerEmpNo;
	private List<AlpinePhotoResponse> photos;

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public String getWriterEmpNo() { return writerEmpNo; }
	public void setWriterEmpNo(String writerEmpNo) { this.writerEmpNo = writerEmpNo; }
	public List<AlpinePhotoResponse> getPhotos() { return photos; }
	public void setPhotos(List<AlpinePhotoResponse> photos) { this.photos = photos; }
}
