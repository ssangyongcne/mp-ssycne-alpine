package kr.co.sscm.alpine.event.dto;

/** Event list row response DTO. */
public class AlpineEventSummaryResponse {

	private Long eventNo;        // 산행차수
	private String userNo;    	// 사번
	private String userNm;        // 이름
	private String memberYN;        // 회원여부
	private String deptNm;       // 소속
	private String dutyNm;     // 팀
	// private Integer viewCnt;  // Not selected by EventSql.xml.

	public Long geteventNo() { return eventNo; }
	public void seteventNo(Long eventNo) { this.eventNo = eventNo; }
	public String getuserNo() { return userNo; }
	public void setuserNo(String userNo) { this.userNo = userNo; }
	public String getuserNm() { return userNm; }
	public void setuserNm(String userNm) { this.userNm = userNm; }
	public String getmemberYN() { return memberYN; }
	public void setmemberYN(String memberYN) { this.memberYN = memberYN; }
	public String getdeptNm() { return deptNm; }
	public void setdeptNm(String deptNm) { this.deptNm = deptNm; }
	public String getdutyNm() { return dutyNm; }
	public void setdutyNm(String dutyNm) { this.dutyNm = dutyNm; }
}