package kr.co.sscm.alpine.admin.dto;

public class AdminPasswordResetRequest {

	private String targetEmpNo;
	private String adminEmpNo;

	public String getTargetEmpNo() { return targetEmpNo; }
	public void setTargetEmpNo(String targetEmpNo) { this.targetEmpNo = targetEmpNo; }
	public String getAdminEmpNo() { return adminEmpNo; }
	public void setAdminEmpNo(String adminEmpNo) { this.adminEmpNo = adminEmpNo; }
}

