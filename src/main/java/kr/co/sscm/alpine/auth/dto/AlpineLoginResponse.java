package kr.co.sscm.alpine.auth.dto;

public class AlpineLoginResponse {

	private String EMPNO;
	private String UZR_NM;
	private String DEPT_NM;
	private String DUTY_NM;
	private String PHONE;
	private String ADMIN_YN;

	public String getEMPNO() {
		return EMPNO;
	}

	public void setEMPNO(String EMPNO) {
		this.EMPNO = EMPNO;
	}

	public String getUZR_NM() {
		return UZR_NM;
	}

	public void setUZR_NM(String UZR_NM) {
		this.UZR_NM = UZR_NM;
	}

	public String getDEPT_NM() {
		return DEPT_NM;
	}

	public void setDEPT_NM(String DEPT_NM) {
		this.DEPT_NM = DEPT_NM;
	}

	public String getDUTY_NM() {
		return DUTY_NM;
	}

	public void setDUTY_NM(String DUTY_NM) {
		this.DUTY_NM = DUTY_NM;
	}

	public String getPHONE() {
		return PHONE;
	}

	public void setPHONE(String PHONE) {
		this.PHONE = PHONE;
	}

	public String getADMIN_YN() {
		return ADMIN_YN;
	}

	public void setADMIN_YN(String ADMIN_YN) {
		this.ADMIN_YN = ADMIN_YN;
	}
}
