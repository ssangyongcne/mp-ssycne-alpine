package kr.co.sscm.alpine.board.dto;

/** Attachment metadata returned with board detail. */
public class BoardAppndFileResponse {

	private String appndFileUuid;
	private String appndFileGroupUuid;
	private String filePath;
	private String originalName;
	private String savedName;
	private String extension;
	private String mimeType;
	private Integer fileSize;
	private String paramName;
	private Integer sortOrder;
	private Integer downloadCount;

	public String getAppndFileUuid() { return appndFileUuid; }
	public void setAppndFileUuid(String appndFileUuid) { this.appndFileUuid = appndFileUuid; }
	public String getAppndFileGroupUuid() { return appndFileGroupUuid; }
	public void setAppndFileGroupUuid(String appndFileGroupUuid) { this.appndFileGroupUuid = appndFileGroupUuid; }
	public String getFilePath() { return filePath; }
	public void setFilePath(String filePath) { this.filePath = filePath; }
	public String getOriginalName() { return originalName; }
	public void setOriginalName(String originalName) { this.originalName = originalName; }
	public String getSavedName() { return savedName; }
	public void setSavedName(String savedName) { this.savedName = savedName; }
	public String getExtension() { return extension; }
	public void setExtension(String extension) { this.extension = extension; }
	public String getMimeType() { return mimeType; }
	public void setMimeType(String mimeType) { this.mimeType = mimeType; }
	public Integer getFileSize() { return fileSize; }
	public void setFileSize(Integer fileSize) { this.fileSize = fileSize; }
	public String getParamName() { return paramName; }
	public void setParamName(String paramName) { this.paramName = paramName; }
	public Integer getSortOrder() { return sortOrder; }
	public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
	public Integer getDownloadCount() { return downloadCount; }
	public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
}