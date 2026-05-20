package kr.co.sscm.common.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @FileName MsgSendDto.java
 * @comment PUSH 발송 dto
 * @author AJH
 */
public class MsgSendDto {

	private String ext = "";			// PUSH 부가 히든메시지. 없으면 빈값(blank) 처리. PUSH 커스텀 메시지
	private String msgCategory; 		// 메세지 카테고리
	private String videoUrl;			// 동영상 URL
	private List<String> cuids;			// 사용자ID
	private MultipartFile imageFile;	// 첨부파일 이미지
	private String templateYn = "N";	// 보내는 메시지 내용에 %CUID% 와 같은 치환변수가 있을 경우 반드시 Y 로 발송하여야 치환처리 됩니다.
	private String message;				// 메세지 내용
	private String sendType;			// 발송 구분( 전체발송 : ALL, 그룹발송 : GROUP, 개인발송 : EACH )
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getMsgCategory() {
		return msgCategory;
	}
	public void setMsgCategory(String msgCategory) {
		this.msgCategory = msgCategory;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public List<String> getCuids() {
		return cuids;
	}
	public void setCuids(List<String> cuids) {
		this.cuids = cuids;
	}
	public MultipartFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	public String getTemplateYn() {
		return templateYn;
	}
	public void setTemplateYn(String templateYn) {
		this.templateYn = templateYn;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}


}
