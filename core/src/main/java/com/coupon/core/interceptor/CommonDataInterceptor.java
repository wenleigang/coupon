package com.coupon.core.interceptor;

import com.coupon.core.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.core.interceptor
 * @ClassName: CommonDataInterceptor
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/27/027 14:15
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/27/027 14:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Slf4j
public class CommonDataInterceptor extends HandlerInterceptorAdapter {
    private static final String[] DEFAULT_EXCLUDE_PARAMETER_PATTERN = new String[] { "\\&\\w*page.pn=\\d+", "\\?\\w*page.pn=\\d+",
            "\\&\\w*page.size=\\d+" };
    private String[] excludeParameterPatterns = DEFAULT_EXCLUDE_PARAMETER_PATTERN;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CommonDataInterceptor--preHandle");
        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        request.setAttribute(Constants.CONTEXT_PATH, path);
        if(request.getAttribute(Constants.NO_QUERYSTRING_CURRENT_URL) == null) {
            request.setAttribute(Constants.NO_QUERYSTRING_CURRENT_URL, extractCurrentURL(request, false));
        }
        if(request.getAttribute(Constants.BACK_URL) == null) {
            request.setAttribute(Constants.BACK_URL, extractBackURL(request));
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 获取当前访问地址url
     * @param request
     * @param needQueryString
     * @return
     */
    private String extractCurrentURL(HttpServletRequest request, boolean needQueryString) {
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        if(!StringUtils.isEmpty(queryString)) {
            queryString = "?" + queryString;
            for (String pattern : excludeParameterPatterns) {
                queryString = queryString.replaceAll(pattern, "");
            }
            if(queryString.startsWith("&")) {
                queryString = "?" + queryString.substring(1);
            }
        }
        if(!StringUtils.isEmpty(queryString) && needQueryString) {
            url = url + queryString;
        }
        return getBasePath(request) + url;
    }

    /**
     * 获取项目root路径
     * @param request
     * @return
     */
    private String getBasePath(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        sb.append(scheme).append("://").append(serverName);
        if((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
            sb.append(":").append(port);
        }
        return sb.toString();
    }

    private String extractBackURL(HttpServletRequest request) {
        String url = request.getParameter(Constants.BACK_URL);
        if(StringUtils.isEmpty(url)) {
            url = request.getHeader("referer");
        }
        if(!StringUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
            return url;
        }
        if(!StringUtils.isEmpty(url) && url.startsWith(request.getContextPath())) {
            url = getBasePath(request) + url;
        }
        return url;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("CommonDataInterceptor--postHandle");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("CommonDataInterceptor--afterCompletion");
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CommonDataInterceptor--afterConcurrentHandlingStarted");
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    //对于请求是ajax请求重定向问题的处理方法
    public void reDirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取当前请求的路径
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"  + request.getServerPort()+request.getContextPath();
        //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            //告诉ajax我重定向的路径
            response.setHeader("CONTENTPATH", basePath+"/");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            response.sendRedirect(basePath + "/");
        }
    }
}
