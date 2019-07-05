package com.coupon.front.advice;

import com.coupon.core.entity.ResponseDto;
import com.coupon.core.enums.ResponseStatus;
import com.coupon.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception exception, WebRequest request) {
        exception.printStackTrace();
        ResponseDto<String> responseT = null;
        ServletWebRequest webRequest = (ServletWebRequest) request;
        String requestURI;
        requestURI = webRequest.getRequest().getRequestURI();
        if(exception instanceof BizException){ //业务预知的异常
            responseT = new ResponseDto((BizException)exception);
            log.error(((BizException) exception).getMsg());
        } else {
            BizException bizException;
            //构造 BizException
            ResponseStatus error = ResponseStatus.ERROR;
            if(exception == null) {
                bizException = new BizException(error.getCode(), error.getcMsg(), error.geteMsg());
            }else {
                if(exception.getCause() == null) {
                    bizException = new BizException(error.getCode(), exception.toString(), exception.getMessage());
                }else {
                    bizException = new BizException(error.getCode(), exception.getCause().toString(), exception.getMessage());
                }
            }
            responseT = new ResponseDto(bizException);
        }
        responseT.setUri(requestURI);
        return new ResponseEntity<>(responseT, HttpStatus.OK);
    }
}
