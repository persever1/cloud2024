package com.atguigu.cloud.mygateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @FileName MyGlobalFilter
 * @Description
 * @Author mark
 * @date 2024-06-21
 **/
@Component //不要忘记
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered
{
    /**
     * 获取当前处理器的优先级。
     * <p>
     * 优先级越小，表示该处理器的优先级越高。此方法返回0，表示该处理器具有最高的优先级。
     * 通常，该方法用于框架中，以决定处理器的执行顺序。
     *
     * @return 优先级，返回0表示最高优先级。
     */
    @Override
    public int getOrder()
    {
        return 0;
    }


    private static final String BEGIN_VISIT_TIME = "begin_visit_time";//开始访问时间
    /**
     * 网关过滤器，用于统计接口访问信息。
     * 在请求开始时记录时间，在请求结束后计算并记录请求耗时，以及接口的主机、端口、URL等信息。
     *
     * @param exchange 服务器web交换机，用于获取和操作请求和响应的相关信息。
     * @param chain 过滤链，用于继续处理请求。
     * @return Mono<Void>，表示异步处理结果。
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 记录请求开始时间
        //先记录下访问接口的开始时间
        exchange.getAttributes().put(BEGIN_VISIT_TIME, System.currentTimeMillis());

        // 继续处理请求，并在处理完成后执行后续操作
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            // 获取请求开始时间
            Long beginVisitTime = exchange.getAttribute(BEGIN_VISIT_TIME);
            if (beginVisitTime != null){
                // 记录并输出接口访问的详细信息，包括主机、端口、URL、请求参数和请求耗时
                log.info("访问接口主机: " + exchange.getRequest().getURI().getHost());
                log.info("访问接口端口: " + exchange.getRequest().getURI().getPort());
                log.info("访问接口URL: " + exchange.getRequest().getURI().getPath());
                log.info("访问接口URL参数: " + exchange.getRequest().getURI().getRawQuery());
                log.info("访问接口时长: " + (System.currentTimeMillis() - beginVisitTime) + "ms");
                log.info("我是美丽分割线: ###################################################");
                System.out.println();
            }
        }));
    }

}