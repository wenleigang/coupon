package com.coupon.core.exception;


import com.coupon.core.enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务异常，为检查时异常，必须捕获
 */
@Slf4j
public class BizException extends RuntimeException {
	/** 异常码 */
	private Integer errorCode;
	/** 对用户友好的错误信息 */
	private String msg;
	/** 错误堆栈信息，便于排查问题 */
	private String developMsg;
	/** 表示这个错误相关的web页面，可以帮助开发人员获取更多的错误的信息 */
	private String uri;

	public BizException(ResponseStatus responseStatus) {
		this.errorCode = responseStatus.getCode();
		this.msg = responseStatus.geteMsg();
	}

	public BizException(Integer errorCode, String message) {
		this.errorCode = errorCode;
		this.msg = message;
	}

	public BizException(Integer errorCode, String message, String developMsg) {
		this(errorCode, message);
		this.developMsg = developMsg;
	}

	public BizException(Integer errorCode, String message, Throwable cause) {
		this(errorCode, message);
		this.developMsg = cause.getMessage();
	}

	public BizException(Integer errorCode, String message, String developMsg, String uri) {
		this(errorCode, message, developMsg);
		this.uri = uri;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDevelopMsg() {
		return developMsg;
	}

	public void setDevelopMsg(String developMsg) {
		this.developMsg = developMsg;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
