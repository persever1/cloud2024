package com.atguigu.cloud.serivce;

import org.apache.ibatis.annotations.Param;

/**
 * @FileName AccountService
 * @Description
 * @Author mark
 * @date 2024-07-05
 **/
public interface AccountService {

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 本次消费金额
     */
    void decrease(@Param("userId") Long userId, @Param("money") Long money);
}
