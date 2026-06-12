package kr.co.sscm.alpine.member.dto;

public class AlpineMemberResponse {

	private String empNo;
	private String name;
	private String dutyName;
	private String deptName;
	private String phone;
	private String photoUrl;

	public String getEmpNo() { return empNo; }
	public void setEmpNo(String empNo) { this.empNo = empNo; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getDutyName() { return dutyName; }
	public void setDutyName(String dutyName) { this.dutyName = dutyName; }
	public String getDeptName() { return deptName; }
	public void setDeptName(String deptName) { this.deptName = deptName; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public String getPhotoUrl() { return photoUrl; }
	public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
