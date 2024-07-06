package com.atguigu.cloud.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

/**
 * @FileName PayGateWayController
 * @Description
 * @Author mark
 * @date 2024-06-21
 **/
@RestController
public class PayGateWayController {
    @Resource
    PayService payService;

    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo() {
        return ResultData.success("gateway info test：" + IdUtil.simpleUUID());
    }

    /**
     * 处理支付网关过滤器请求的GET方法。
     * 该方法旨在获取请求头中特定的字段，并将它们拼接成一个字符串作为结果返回。
     * 特定字段包括"X-Request-atguigu1"和"X-Request-atguigu2"。
     *
     * @param request HttpServletRequest对象，用于获取请求头信息。
     * @return 包含请求头信息的字符串结果，以及当前时间戳。
     */
    @GetMapping(value = "/pay/gateway/filter")
    public ResultData<String> getGatewayFilter(HttpServletRequest request) {
        // 初始化结果字符串
        String result = "";
        // 获取请求头的名称
        Enumeration<String> headers = request.getHeaderNames();
        // 遍历所有请求头
        while (headers.hasMoreElements()) {
            // 获取当前请求头的名称
            String headName = headers.nextElement();
            // 获取当前请求头的值
            String headValue = request.getHeader(headName);
            // 打印请求头的名称和值
            System.out.println("请求头名: " + headName + "\t\t\t" + "请求头值: " + headValue);
            // 检查当前请求头名称是否为特定字段，如果是，则将其名称和值添加到结果字符串中
            if (headName.equalsIgnoreCase("X-Request-atguigu1")
                    || headName.equalsIgnoreCase("X-Request-atguigu2")) {
                result = result + headName + "\t" + headValue + " ";
            }
        }
        System.out.println("=============================================");
        String customerId = request.getParameter("customerId");
        System.out.println("request Parameter customerId: "+customerId);

        String customerName = request.getParameter("customerName");
        System.out.println("request Parameter customerName: "+customerName);
        System.out.println("=============================================");
        // 返回包含结果字符串和当前时间戳的成功结果
        return ResultData.success("getGatewayFilter 过滤器 test： " + result + " \t " + DateUtil.now());
    }

}
