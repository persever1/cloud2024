package com.atguigu.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * @FileName MyRequestOriginParser
 * @Description
 * @Author mark
 * @date 2024-07-01
 **/
@Component
public class MyRequestOriginParser implements RequestOriginParser
{
    /**
     * 从HTTP请求中解析服务器名称。
     *
     * 此方法旨在获取客户端请求中指定的服务器名称。通过解析请求参数，它能够提取出服务器名称，
     * 这对于识别请求来源或进行特定的请求路由非常有用。
     *
     * @param httpServletRequest HTTP请求对象，包含客户端发送的所有请求信息。
     * @return 返回请求参数中指定的服务器名称。如果请求中未包含“serverName”参数，则返回空字符串。
     */
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        // 从请求参数中获取服务器名称
        return httpServletRequest.getParameter("serverName");
    }

}