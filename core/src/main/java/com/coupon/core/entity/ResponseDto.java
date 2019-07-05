package com.coupon.core.entity;

import com.coupon.core.enums.ResponseStatus;
import com.coupon.core.exception.BizException;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.core.entity
 * @ClassName: ResponseDto
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/29/029 15:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/29/029 15:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ResponseDto<T> {

    private Integer code;

    private String msg;

    private T data;

    private String uri;

    private String developMsg;

    public ResponseDto(ResponseStatus status) {
        this.code = status.getCode();
        this.msg = status.getcMsg();
        this.developMsg = status.geteMsg();
    }

    public ResponseDto(BizException bizException) {
        this.code = bizException.getErrorCode();
        this.msg = bizException.getMsg();
        this.developMsg = bizException.getDevelopMsg();
        this.uri = bizException.getUri();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDevelopMsg() {
        return developMsg;
    }

    public void setDevelopMsg(String developMsg) {
        this.developMsg = developMsg;
    }
}
