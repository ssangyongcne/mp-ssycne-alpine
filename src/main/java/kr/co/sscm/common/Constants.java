package kr.co.sscm.common;

public class Constants {

	final static public String CLIENT_REQUEST = "clientRequestwithReturn";

	/* 에러 코드 정의 */
	final static public int ERR_CODE_EXCEPTION = 1000;					// 공통 에러 코드
	final static public int ERR_CODE_API_EXCEPTION = 1100;				// API 에러 코드
	final static public int ERR_CODE_VALIDATION_EXCEPTION = 1200;		// validation 에러 코드
	final static public int ERR_CODE_EAI_EXCEPTION = 1300;				// EAI 에러 코드
	final static public int ERR_CODE_PUSH_EXCEPTION = 1400;				// PUSH 발송 에러 코드
	final static public int ERR_CODE_SESS_EXCEPTION = 1500;				// Session 에러 코드

	/* parameter 한글명 */
	final static public String EMPNO = "사원번호";
	final static public String UZR_PW = "사용자 암호";
	final static public String FWRD_REQNO = "출하 의뢰번호";
	final static public String FWRD_DT = "출하 일자";
	final static public String RCPT_NO = "입금번호";
	
	/* 세션 */
	final static public String LOGIN_INFO = "loginInfo";

	/* AES256Util 키 */
	static final public String KEY = "ssyc0i1l2t3r4o5n";
	final static public byte[] IV = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16};

}
