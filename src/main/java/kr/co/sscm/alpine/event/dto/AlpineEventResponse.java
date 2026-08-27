package kr.co.sscm.alpine.event.dto;

// import java.util.List; // Unused.

public class AlpineEventResponse {

	private String userNo;
	private String userNm;
	private String deptNm;
	private String dutyNm;
	private String phone;
	private String auth;
	private String firstLogin;
	private String useYn;
	private String loginDdtm;
	private String pw;

	public String getUserNo() { return userNo; }
	public void setUserNo(String userNo) { this.userNo = userNo; }
	public String getUserNm() { return userNm; }
	public void setUserNm(String userNm) { this.userNm = userNm; }
	public String getDeptNm() { return deptNm; }
	public void setDeptNm(String deptNm) { this.deptNm = deptNm; }
	public String getDutyNm() { return dutyNm; }
	public void setDutyNm(String dutyNm) { this.dutyNm = dutyNm; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public String getAuth() { return auth; }
	public void setAuth(String auth) { this.auth = auth; }
	public String getFirstLogin() { return firstLogin; }
	public void setFirstLogin(String firstLogin) { this.firstLogin = firstLogin; }
	public String getUseYn() { return useYn; }
	public void setUseYn(String useYn) { this.useYn = useYn; }
	public String getLoginDdtm() { return loginDdtm; }
	public void setLoginDdtm(String loginDdtm) { this.loginDdtm = loginDdtm; }
	public String getPw() { return pw; }
	public void setPw(String pw) { this.pw = pw; }
}
