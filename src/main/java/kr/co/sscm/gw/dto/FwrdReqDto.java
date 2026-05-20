package kr.co.sscm.gw.dto;

/**
 * @FileName FwrdReqDto.java
 * @comment 시간대별내수 출하현황 DTO
 * @author AJH
 */
public class FwrdReqDto {

	private String fwrdDt;	// 출하 일자

	private String factCd;	// 공장 코드

	private String empNo;	// 사원번호

	public String getFwrdDt() {
		return fwrdDt;
	}

	public void setFwrdDt(String fwrdDt) {
		this.fwrdDt = fwrdDt;
	}

	public String getFactCd() {
		return factCd;
	}

	public void setFactCd(String factCd) {
		this.factCd = factCd;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

}
