package kr.co.sscm.alpine.auth.dto;

public class AlpineLoginRequest {

	private String userNo;
	private String pw;

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
}
