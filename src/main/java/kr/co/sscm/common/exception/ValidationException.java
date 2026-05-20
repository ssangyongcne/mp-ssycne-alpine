package kr.co.sscm.common.exception;

import kr.co.sscm.common.Constants;

/**
 * @FileName ValidationException.java
 * @comment Validation Exception
 * @author AJH
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	private int resultCode = Constants.ERR_CODE_VALIDATION_EXCEPTION;

	public ValidationException(int resultCode, String message) {
		super(message);
		this.resultCode = resultCode;
	}
	public ValidationException(String message) {
		super(message);
	}
	public ValidationException(String message, Throwable casuse) {
		super(message, casuse);
	}
	public ValidationException( Throwable cause) {
		super(cause);
	}
	protected ValidationException(String message, Throwable cause, boolean enableSupression, boolean writableStackTrace) {
		super(message, cause, enableSupression, writableStackTrace);
	}
	public int getResultCode() {
		return this.resultCode;
	}


}
