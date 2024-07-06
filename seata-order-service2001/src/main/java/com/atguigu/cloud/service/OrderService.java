package com.atguigu.cloud.service;

import com.atguigu.cloud.entities.Order;

/**
 * @FileName OrderService
 * @Description
 * @Author mark
 * @date 2024-07-05
 **/
public interface OrderService {
    /**
     * 创建订单
     */
    void create(Order order);
}
