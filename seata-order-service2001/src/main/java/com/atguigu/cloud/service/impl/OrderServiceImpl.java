package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @FileName OrderServiceImpl
 * @Description
 * @Author mark
 * @date 2024-07-05
 **/
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource//订单微服务通过OpenFeign去调用库存微服务
    private StorageFeignApi storageFeignApi;
    @Resource//订单微服务通过OpenFeign去调用账户微服务
    private AccountFeignApi accountFeignApi;


    /**
     * 创建订单的方法。
     * 使用全局事务确保订单创建、库存扣减和账户余额扣减的一致性。
     *
     * @param order 待创建的订单对象，包含订单相关信息。
     */
    @Override
    /*创建订单的全局事务方法。
     * 使用@GlobalTransactional注解来声明这是一个全局事务方法，事务名称为"zzyy-create-order"。
     * 指定回滚策略为对所有异常类型(Exception.class)进行回滚，确保事务的一致性。
     * 此注解通常用于分布式事务管理，提供事务的 ACID 属性，保障业务操作的原子性、一致性、隔离性和持久性。
     */
    @GlobalTransactional(name = "zzyy-create-order", rollbackFor = Exception.class) //AT
    //@GlobalTransactional @Transactional(rollbackFor = Exception.class) //XA
    public void create(Order order) {
        // 获取当前分布式事务的XID，用于跟踪和审计事务。
        //xid检查
        String xid = RootContext.getXID();

        // 日志记录开始创建订单，同时记录订单的XID，用于问题追踪。
        //1. 新建订单
        log.info("==================>开始新建订单" + "\t" + "xid_order:" + xid);

        // 设置订单状态为创建中，准备插入数据库。
        // 订单状态status：0：创建中；1：已完结
        order.setStatus(0);
        // 尝试插入订单到数据库。
        int result = orderMapper.insertSelective(order);

        // 如果插入订单成功（插入结果大于0），则进一步处理。
        //插入订单成功后获得插入mysql的实体对象
        Order orderFromDB = null;
        if (result > 0) {
            // 通过订单ID从数据库中再次查询订单，确保数据一致性。
            orderFromDB = orderMapper.selectOne(order);
            // 日志记录订单插入成功，并打印订单信息。
            log.info("-------> 新建订单成功，orderFromDB info: " + orderFromDB);
            System.out.println();

            // 调用库存服务，扣减相应产品的库存。
            //2. 扣减库存
            log.info("-------> 订单微服务开始调用Storage库存，做扣减count");
            storageFeignApi.decrease(orderFromDB.getProductId(), orderFromDB.getCount());
            log.info("-------> 订单微服务结束调用Storage库存，做扣减完成");
            System.out.println();

            // 调用账户服务，扣减用户账户余额。
            //3. 扣减账号余额
            log.info("-------> 订单微服务开始调用Account账号，做扣减money");
            accountFeignApi.decrease(orderFromDB.getUserId(), orderFromDB.getMoney());
            log.info("-------> 订单微服务结束调用Account账号，做扣减完成");
            System.out.println();

            // 修改订单状态为已完结。
            //4. 修改订单状态
            // 订单状态status：0：创建中；1：已完结
            log.info("-------> 修改订单状态");
            orderFromDB.setStatus(1);

            // 构建更新条件，仅更新状态为创建中的订单。
            Example whereCondition = new Example(Order.class);
            Example.Criteria criteria = whereCondition.createCriteria();
            criteria.andEqualTo("userId", orderFromDB.getUserId());
            criteria.andEqualTo("status", 0);

            // 执行订单状态的更新操作。
            int updateResult = orderMapper.updateByExampleSelective(orderFromDB, whereCondition);

            // 日志记录订单状态更新完成，同时打印更新结果和订单信息。
            log.info("-------> 修改订单状态完成" + "\t" + updateResult);
            log.info("-------> orderFromDB info: " + orderFromDB);
        }
        System.out.println();
        // 订单创建流程结束，记录结束日志并打印订单XID。
        log.info("==================>结束新建订单" + "\t" + "xid_order:" + xid);
    }

}
