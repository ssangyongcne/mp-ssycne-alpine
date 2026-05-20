


package kr.co.sscm.common.dto;

import java.io.Serializable;

/**
 * @FileName LoginInfoDto.java
 * @comment 로그인 정보
 * @author AJH
 */
public class LoginInfoDto implements Serializable  {

	private static final long serialVersionUID = -1L;

	private String empNo;			// 사원번호
	private String uzrNm;			// 사용자 성명
	private String combOrgCd;		// 통합 조직 코드
	private String combNm;			// 조직 명
	private String pwChk;			// 비밀번호 체크
	private String salUtCd;			// 영업 단위 코드
	private String authId;			// 권한 아이디1
	private String authId2;			// 권한 아이디2
	private String authId3;			// 권한 아이디3
	private String authId4;			// 권한 아이디4
	private String meetClose1;		// 사용 여부1
	private String meetClose2;		// 사용 여부2
	private String retrvSalUt;		// 관리 영업단위
	private String salPartCd;		// 영업 부문 코드
	private String matRcvpayOrgCd;	
	private String matRcvpayOrgNm;
	private String uppBgtOrgCd;		// 소속 사업장코드
	
	private String ip;				// IP

	public String getMatRcvpayOrgCd() {
		return matRcvpayOrgCd;
	}
	public void setMatRcvpayOrgCd(String matRcvpayOrgCd) {
		this.matRcvpayOrgCd = matRcvpayOrgCd;
	}
	public String getMatRcvpayOrgNm() {
		return matRcvpayOrgNm;
	}
	public void setMatRcvpayOrgNm(String matRcvpayOrgNm) {
		this.matRcvpayOrgNm = matRcvpayOrgNm;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getUzrNm() {
		return uzrNm;
	}
	public void setUzrNm(String uzrNm) {
		this.uzrNm = uzrNm;
	}
	public String getUppBgtOrgCd() {
		return uppBgtOrgCd;
	}
	public void setUppBgtOrgCd(String uppBgtOrgCd) {
		this.uppBgtOrgCd = uppBgtOrgCd;
	}
	public String getCombOrgCd() {
		return combOrgCd;
	}
	public void setCombOrgCd(String combOrgCd) {
		this.combOrgCd = combOrgCd;
	}
	public String getCombNm() {
		return combNm;
	}
	public void setCombNm(String combNm) {
		this.combNm = combNm;
	}
	public String getPwChk() {
		return pwChk;
	}
	public void setPwChk(String pwChk) {
		this.pwChk = pwChk;
	}
	public String getSalUtCd() {
		return salUtCd;
	}
	public void setSalUtCd(String salUtCd) {
		this.salUtCd = salUtCd;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getAuthId2() {
		return authId2;
	}
	public void setAuthId2(String authId2) {
		this.authId2 = authId2;
	}
	public String getAuthId3() {
		return authId3;
	}
	public void setAuthId3(String authId3) {
		this.authId3 = authId3;
	}
	public String getAuthId4() {
		return authId4;
	}
	public void setAuthId4(String authId4) {
		this.authId4 = authId4;
	}
	public String getMeetClose1() {
		return meetClose1;
	}
	public void setMeetClose1(String meetClose1) {
		this.meetClose1 = meetClose1;
	}
	public String getMeetClose2() {
		return meetClose2;
	}
	public void setMeetClose2(String meetClose2) {
		this.meetClose2 = meetClose2;
	}
	public String getRetrvSalUt() {
		return retrvSalUt;
	}
	public void setRetrvSalUt(String retrvSalUt) {
		this.retrvSalUt = retrvSalUt;
	}
	public String getSalPartCd() {
		return salPartCd;
	}
	public void setSalPartCd(String salPartCd) {
		this.salPartCd = salPartCd;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}






