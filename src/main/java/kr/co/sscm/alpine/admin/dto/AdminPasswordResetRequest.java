package kr.co.sscm.alpine.admin.dto;

import java.util.List;

public class AdminPasswordResetRequest {

	private String userNo;
	private List<AdminPasswordResetRequest> list;

	public String getUserNo() { return userNo; }
	public void setUserNo(String userNo) { this.userNo = userNo; }
	public List<AdminPasswordResetRequest> getList() { return list; }
	public void setList(List<AdminPasswordResetRequest> list) { this.list = list; }
}

