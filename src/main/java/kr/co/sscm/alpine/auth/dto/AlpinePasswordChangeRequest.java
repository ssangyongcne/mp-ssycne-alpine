package kr.co.sscm.alpine.auth.dto;

public class AlpinePasswordChangeRequest {

	private String userNo;
	private String currentPw;
	private String newPw;

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getCurrentPw() {
		return currentPw;
	}

	public void setCurrentPw(String currentPw) {
		this.currentPw = currentPw;
	}

	public String getNewPw() {
		return newPw;
	}

	public void setNewPw(String newPw) {
		this.newPw = newPw;
	}
}