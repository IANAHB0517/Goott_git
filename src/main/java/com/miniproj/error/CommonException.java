package com.miniproj.error;

public class CommonException extends Exception {
	private String errorMsg;
	private int errorCode;
	private StackTraceElement[] stackTrace;
	
	
	//에러 메세지 생성
	public CommonException(String message, int errorCode) {
		super(message);
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}


	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}


	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}


	@Override
	public String toString() {
		return "CommonException [errorMsg=" + errorMsg + ", errorCode=" + errorCode + "]";
	}
	
	
}
