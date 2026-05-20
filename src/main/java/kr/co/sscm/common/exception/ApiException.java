package kr.co.sscm.common.exception;

import kr.co.sscm.common.Constants;

/**
 * @FileName ApiException.java
 * @comment I/F 호출시 Exception
 * @author AJH
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = 1L;

	private int resultCode = Constants.ERR_CODE_API_EXCEPTION;

	public ApiException() {
		super();
	}
	public ApiException(int resultCode, String message) {
		super(message);
		this.resultCode = resultCode;
	}
	public ApiException(String message) {
		super(message);
	}
	public ApiException(String message, Throwable casuse) {
		super(message, casuse);
	}
	public ApiException( Throwable cause) {
		super(cause);
	}
	protected ApiException(String message, Throwable cause, boolean enableSupression, boolean writableStackTrace) {
		super(message, cause, enableSupression, writableStackTrace);
	}
	public int getResultCode() {
		return this.resultCode;
	}
}
