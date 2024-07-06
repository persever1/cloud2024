package com.atguigu.cloud.service;

/**
 * @FileName StorageService
 * @Description
 * @Author mark
 * @date 2024-07-05
 **/
public interface StorageService {
    /**
     * 扣减库存
     */
    void decrease(Long productId, Integer count);
}
