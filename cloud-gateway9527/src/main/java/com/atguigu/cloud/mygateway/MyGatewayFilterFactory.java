package com.atguigu.cloud.mygateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
/**
 * @FileName MyGatewayFilterFactory
 * @Description
 * @Author mark
 * @date 2024-06-21
 **/
@Component
public class MyGatewayFilterFactory extends AbstractGatewayFilterFactory<MyGatewayFilterFactory.Config>
{
    public MyGatewayFilterFactory()
    {
        super(MyGatewayFilterFactory.Config.class);
    }


    /**
     * 根据配置应用自定义的网关过滤器。
     *
     * @param config 过滤器的配置信息，包含特定的状态码。
     * @return 返回一个实现了GatewayFilter接口的新过滤器实例。
     */
    @Override
    public GatewayFilter apply(MyGatewayFilterFactory.Config config) {
        // 创建一个新的GatewayFilter实例
        return new GatewayFilter() {
            /**
             * 在请求处理链中应用过滤器。
             *
             * @param exchange 当前的服务器web交换机。
             * @param chain 过滤器链，用于继续或中断请求处理。
             * @return 表示异步操作的Mono<Void>实例。
             */
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                // 获取当前请求
                ServerHttpRequest request = exchange.getRequest();
                // 打印配置的状态码
                System.out.println("进入了自定义网关过滤器MyGatewayFilterFactory，status："+config.getStatus());
                // 检查请求参数中是否包含"atguigu"
                if (request.getQueryParams().containsKey("atguigu")) {
                    // 如果包含，则继续处理请求
                    return chain.filter(exchange);
                } else {
                    // 如果不包含，则设置响应状态码为400（错误请求），并完成响应
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    return exchange.getResponse().setComplete();
                }
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("status");
    }

    public static class Config
    {
        @Getter@Setter
        private String status;//设定一个状态值/标志位，它等于多少，匹配和才可以访问
    }
}
//单一内置过滤器GatewayFilter

