package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.atguigu.cloud.service.OrderService;

/**
 * @FileName OrderController
 * @Description
 * @Author mark
 * @date 2024-07-05
 **/
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 处理创建订单的请求。
     *
     * 通过此方法，可以将订单对象持久化到数据库中。
     * 使用@GetMapping注解指定此方法处理GET请求，请求路径为/order/create。
     * 方法参数Order对象包含了订单的详细信息。
     * 返回ResultData对象，表示操作的结果。如果操作成功，ResultData中的data字段为null。
     *
     * @param order 待创建的订单对象，包含订单的所有相关信息。
     * @return ResultData 对象，表示创建订单的操作结果。
     */
    @GetMapping("/order/create")
    public ResultData create(Order order) {
        // 调用订单服务层的方法，实际创建订单
        orderService.create(order);
        // 返回操作成功的结果，不包含具体的数据返回
        return ResultData.success(null);
    }


}
