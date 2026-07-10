package kr.co.sscm.alpine.board.dto;

/** Attachment metadata row for AP_APPND_FILE_MGMT. */
public class BoardAppndFileRequest {

	private String appndFileUuid;
	private String appndFileBassCnts;
	private String appndFileGropUuid;
	private String appndFilePathCnts;
	private String appndFileOrigNm;
	private String appndFileTransmsNm;
	private String appndFileFlextNm;
	private String appndFileMimeCnts;
	private Integer appndFileSiz;
	private String appndFileParmNm;
	private Integer srtOrd;
	private String userNo;
	private String clientIp;

	public String getAppndFileUuid() { return appndFileUuid; }
	public void setAppndFileUuid(String appndFileUuid) { this.appndFileUuid = appndFileUuid; }
	public String getAppndFileBassCnts() { return appndFileBassCnts; }
	public void setAppndFileBassCnts(String appndFileBassCnts) { this.appndFileBassCnts = appndFileBassCnts; }
	public String getAppndFileGropUuid() { return appndFileGropUuid; }
	public void setAppndFileGropUuid(String appndFileGropUuid) { this.appndFileGropUuid = appndFileGropUuid; }
	public String getAppndFilePathCnts() { return appndFilePathCnts; }
	public void setAppndFilePathCnts(String appndFilePathCnts) { this.appndFilePathCnts = appndFilePathCnts; }
	public String getAppndFileOrigNm() { return appndFileOrigNm; }
	public void setAppndFileOrigNm(String appndFileOrigNm) { this.appndFileOrigNm = appndFileOrigNm; }
	public String getAppndFileTransmsNm() { return appndFileTransmsNm; }
	public void setAppndFileTransmsNm(String appndFileTransmsNm) { this.appndFileTransmsNm = appndFileTransmsNm; }
	public String getAppndFileFlextNm() { return appndFileFlextNm; }
	public void setAppndFileFlextNm(String appndFileFlextNm) { this.appndFileFlextNm = appndFileFlextNm; }
	public String getAppndFileMimeCnts() { return appndFileMimeCnts; }
	public void setAppndFileMimeCnts(String appndFileMimeCnts) { this.appndFileMimeCnts = appndFileMimeCnts; }
	public Integer getAppndFileSiz() { return appndFileSiz; }
	public void setAppndFileSiz(Integer appndFileSiz) { this.appndFileSiz = appndFileSiz; }
	public String getAppndFileParmNm() { return appndFileParmNm; }
	public void setAppndFileParmNm(String appndFileParmNm) { this.appndFileParmNm = appndFileParmNm; }
	public Integer getSrtOrd() { return srtOrd; }
	public void setSrtOrd(Integer srtOrd) { this.srtOrd = srtOrd; }
	public String getUserNo() { return userNo; }
	public void setUserNo(String userNo) { this.userNo = userNo; }
	public String getClientIp() { return clientIp; }
	public void setClientIp(String clientIp) { this.clientIp = clientIp; }
}