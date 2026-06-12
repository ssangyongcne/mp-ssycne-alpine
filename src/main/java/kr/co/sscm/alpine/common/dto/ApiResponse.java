package kr.co.sscm.alpine.common.dto;

public class ApiResponse<T> {

	private String resultCode;
	private String resultMsg;
	private T result;

	public ApiResponse() {
	}

	public ApiResponse(String resultCode, String resultMsg, T result) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.result = result;
	}

	public static <T> ApiResponse<T> success(T result) {
		return new ApiResponse<T>("200", "OK", result);
	}

	public static <T> ApiResponse<T> notImplemented() {
		return new ApiResponse<T>("500", "NOT_IMPLEMENTED", null);
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
