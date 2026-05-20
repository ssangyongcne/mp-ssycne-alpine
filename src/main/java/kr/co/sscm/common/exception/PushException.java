package kr.co.sscm.common.exception;

import kr.co.sscm.common.Constants;

/**
 * @FileName PushException.java
 * @comment PUSH 발송 Exception
 * @author AJH
 */
public class PushException extends Exception {

	private static final long serialVersionUID = 1L;

	private int resultCode = Constants.ERR_CODE_PUSH_EXCEPTION;

	public PushException() {
		super();
	}
	public PushException(int resultCode, String message) {
		super(message);
		this.resultCode = resultCode;
	}
	public PushException(String message) {
		super(message);
	}
	public PushException(String message, Throwable casuse) {
		super(message, casuse);
	}
	public PushException( Throwable cause) {
		super(cause);
	}
	protected PushException(String message, Throwable cause, boolean enableSupression, boolean writableStackTrace) {
		super(message, cause, enableSupression, writableStackTrace);
	}
	public int getResultCode() {
		return this.resultCode;
	}
}
