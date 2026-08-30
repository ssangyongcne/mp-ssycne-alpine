package kr.co.sscm.alpine.event.dto;

/** Event participant count grouped by membership status. */
public class AlpineEventCountResponse { 

	private String memberYN;
	private Integer cnt;

	public String getmemberYN() { return memberYN; }
	public void setmemberYN(String memberYN) { this.memberYN = memberYN; }
	public Integer getcnt() { return cnt; }
	public void setcnt(Integer cnt) { this.cnt = cnt; }
}



