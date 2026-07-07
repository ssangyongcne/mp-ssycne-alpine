package kr.co.sscm.alpine.auth.dto;

public class AlpineLoginResponse {

	private String userNo;
	private String userNm;
	private String deptNm;
	private String dutyNm;
	private String phone;
	private String auth;
	private String firstLogin;

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	public String getDutyNm() {
		return dutyNm;
	}

	public void setDutyNm(String dutyNm) {
		this.dutyNm = dutyNm;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}
}