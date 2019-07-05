package com.coupon.front.advice;

import com.alibaba.fastjson.JSON;
import com.coupon.core.entity.ResponseDto;
import com.coupon.core.enums.ResponseStatus;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.front.advice
 * @ClassName: ExtResponseBodyAdvice
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/29/029 15:37
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/29/029 15:37
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@ControllerAdvice
public class ExtResponseBodyAdvice<T> implements ResponseBodyAdvice<T> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public T beforeBodyWrite(T body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(body instanceof ResponseDto) {
            return (T) JSON.toJSON(body);
        }
        ResponseDto<T> responseDto = new ResponseDto<>(ResponseStatus.SUCCESS);
        responseDto.setData(body);
        responseDto.setUri(serverHttpRequest.getURI().toString());
        if(body instanceof String) {
            return (T) JSON.toJSONString(responseDto);
        }else {
            return (T) JSON.toJSON(responseDto);
        }
    }
}
