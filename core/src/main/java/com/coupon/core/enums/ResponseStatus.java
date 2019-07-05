package com.coupon.core.enums;

/**
 * 
 * @ClassName: ResponseStatus 
 * @Description: 响应状态码枚举类
 * @author: wenleigang
 * @date: 2018年4月11日 下午1:39:34
 */
public enum ResponseStatus {
	
	SUCCESS(200, "响应成功!", "成功"),

    ERROR(500, "system error", "服务端错误!"),

    UNKNOW(555, "unknow error","未知异常!"),

    PARAM_ERROR(501, "param error","参数错误!"),
    
    MISS_REQUIRED_PARAM(502, "miss required param", "缺少必要参数"),

    LOGIN_ERROR(401, "login error","登录错误!"),

    NET_ERROR(1000001, "internet connect error","网络异常，请稍后重试!"),

	/**
	 * 查询失败
	 */
	FAILURE_SELECT(10001, "Select Failure", "查询失败"),
	/**
     * 删除失败
     */
    FAILURE_DELETE(10002, "Delete Failure", "删除失败"),
    /**
     * 添加失败
     */
    FAILURE_ADD(10003, "Add Failure", "添加失败"),
    /**
     * 修改失败
     */
    FAILURE_UPDATE(10004, "Update Failure", "修改失败"),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR(10005, "unknown error");

	Integer code;
	
	String eMsg;
	
	String cMsg;
	
	ResponseStatus() {}
	
	ResponseStatus(Integer code) {
		this.code = code;
	}
	
	ResponseStatus(Integer code, String eMsg) {
		this.code = code;
		this.eMsg = eMsg;
	}
	
	ResponseStatus(Integer code, String eMsg, String cMsg) {
		this.code = code;
		this.eMsg = eMsg;
		this.cMsg = cMsg;
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String geteMsg() {
		return eMsg;
	}

	public void seteMsg(String eMsg) {
		this.eMsg = eMsg;
	}

	public String getcMsg() {
		return cMsg;
	}

	public void setcMsg(String cMsg) {
		this.cMsg = cMsg;
	}

	public static ResponseStatus codeOf(Integer code) {
		for (ResponseStatus responseStatus : ResponseStatus.values()) {
			if(code.equals(responseStatus.getCode())) {
				return responseStatus;
			}
		}
		return null;
	}
}
