package kr.co.sscm.common.exception;

import kr.co.sscm.common.Constants;

/**
 * @FileName InterceptorException.java
 * @comment Interceptor Exception
 * @author AJH
 */
public class InterceptorException extends Exception {

	private static final long serialVersionUID = 1L;

	private int resultCode = Constants.ERR_CODE_SESS_EXCEPTION;

	public InterceptorException() {
		super();
	}
	public InterceptorException(int resultCode, String message) {
		super(message);
		this.resultCode = resultCode;
	}
	public InterceptorException(String message) {
		super(message);
	}
	public InterceptorException(String message, Throwable casuse) {
		super(message, casuse);
	}
	public InterceptorException( Throwable cause) {
		super(cause);
	}
	protected InterceptorException(String message, Throwable cause, boolean enableSupression, boolean writableStackTrace) {
		super(message, cause, enableSupression, writableStackTrace);
	}
	public int getResultCode() {
		return this.resultCode;
	}
}
