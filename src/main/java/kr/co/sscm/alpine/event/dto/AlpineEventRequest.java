package kr.co.sscm.alpine.event.dto;

/** 관리자 사용자 목록 검색 조건 DTO. */
public class AlpineEventRequest {

	private String userNo;
	private String userNm;
	private String deptNm;
	private String auth;
	private String useYn;

	public String getUserNo() { return userNo; }
	public void setUserNo(String userNo) { this.userNo = userNo; }
	public String getUserNm() { return userNm; }
	public void setUserNm(String userNm) { this.userNm = userNm; }
	public String getDeptNm() { return deptNm; }
	public void setDeptNm(String deptNm) { this.deptNm = deptNm; }
	public String getAuth() { return auth; }
	public void setAuth(String auth) { this.auth = auth; }
	public String getUseYn() { return useYn; }
	public void setUseYn(String useYn) { this.useYn = useYn; }
}

