package com.security.demo.entity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fanglingxiao
 * @desc
 * @date 2019/12/11
 */
public class ProcessParamsContext {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String requestBody;
    // 不同平台接收参数统一字段入库 这里String一个字段demo
    private String requestParam;

    public ProcessParamsContext(HttpServletRequest request, HttpServletResponse response, String requestBody) {
        this.request = request;
        this.response = response;
        this.requestBody = requestBody;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
