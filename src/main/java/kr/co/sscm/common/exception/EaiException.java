package kr.co.sscm.common.exception;

import kr.co.sscm.common.Constants;

/**
 * @FileName EaiException.java
 * @comment EAI 통신시 Exception
 * @author AJH
 */
public class EaiException extends Exception {

	private static final long serialVersionUID = 1L;

	private int resultCode = Constants.ERR_CODE_EAI_EXCEPTION;

	public EaiException() {
		super();
	}
	public EaiException(int resultCode, String message) {
		super(message);
		this.resultCode = resultCode;
	}
	public EaiException(String message) {
		super(message);
	}
	public EaiException(String message, Throwable casuse) {
		super(message, casuse);
	}
	public EaiException( Throwable cause) {
		super(cause);
	}
	protected EaiException(String message, Throwable cause, boolean enableSupression, boolean writableStackTrace) {
		super(message, cause, enableSupression, writableStackTrace);
	}
	public int getResultCode() {
		return this.resultCode;
	}

}
