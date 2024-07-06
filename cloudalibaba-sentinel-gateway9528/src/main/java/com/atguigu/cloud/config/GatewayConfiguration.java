package com.atguigu.cloud.config;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
/**
 * @FileName GatewayConfiguration
 * @Description 使用时只需注入对应的 SentinelGatewayFilter 实例以及 SentinelGatewayBlockExceptionHandler 实例即可
 * @Author mark
 * @date 2024-07-02
 **/
@Configuration
public class GatewayConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
 * GatewayConfiguration 构造器，用于初始化对象时设置视图解析器提供者和服务器编码配置。
 *
 * @param viewResolversProvider 视图解析器提供者，如果未提供则使用空列表作为默认值。
 *                             类型为 ObjectProvider<List<ViewResolver>>，允许在运行时提供视图解析器列表。
 * @param serverCodecConfigurer 服务器编码配置器，用于配置服务器如何序列化和反序列化HTTP消息体。
 *                             这是一个必不可少的参数，用于定制Spring WebFlux的编码行为。
 */
public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                           ServerCodecConfigurer serverCodecConfigurer)
{
    this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
    this.serverCodecConfigurer = serverCodecConfigurer;
}

    /**
     * 创建并注册Sentinel Gateway的Block异常处理器，该处理器具有最高优先级。
     * @return 返回一个新的SentinelGatewayBlockExceptionHandler实例，用于处理Spring Cloud Gateway中的流控或降级导致的异常。
     *
     * viewResolvers 视图解析器集合，用于处理错误视图的渲染。
     * serverCodecConfigurer Spring的ServerCodecConfigurer，配置服务器端的编码和解码相关设置，可能涉及到异常信息的序列化和反序列化。
     */
    @Bean //使用@Bean注解，使得Spring容器能够管理该对象，并将其作为bean注入到应用上下文中。
    @Order(Ordered.HIGHEST_PRECEDENCE) //通过@Order(Ordered.HIGHEST_PRECEDENCE)注解设置其执行顺序为最高，确保在其他拦截器之前处理异常。
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // 实例化SentinelGatewayBlockExceptionHandler，并传入视图解析器和服务器编码配置，以便在发生流控或降级时提供适当的响应处理。
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /**
     * 创建一个Sentinel Gateway过滤器 Bean，该过滤器将在所有其他过滤器之前执行。
     *
     * @return SentinelGatewayFilter 实例，用于Sentinel的网关流量控制。
     * @see SentinelGatewayFilter 过滤器类，实现了全局流量控制功能。
     * @see org.springframework.context.annotation.Bean Spring的@Bean注解，用于声明一个配置对象。
     * @see org.springframework.core.annotation.Order Order注解，设置过滤器的执行顺序，负数表示优先级高，先执行。
     */
    @Bean // 使用了Spring的@Bean注解，使得该方法会在Spring初始化时被调用，将返回的过滤器实例注册为Bean。
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * 在实例初始化后调用的方法。用于执行一些特殊的初始化逻辑。
     * 这种方法通常用于初始化类的成员变量，或者执行一次性配置任务。
     */
    @PostConstruct //javax.annotation.PostConstruct 通过使用@PostConstruct注解，保证这个方法会在任何依赖注入完成后被调用。
    public void doInit() {
        initBlockHandler();
    }



    /**
     * 初始化网关流控规则和自定义限流处理逻辑。
     * 此方法用于配置Sentinel对特定路由的流量控制规则，并设定当请求被流控时的处理方式。
     * 具体配置了每秒通过的请求数为2，流控效果为间隔一秒。
     * 当请求被流控时，返回一个包含错误代码和消息的JSON响应，告知客户端请求过于频繁。
     */
    //处理/自定义返回的例外信息
    private void initBlockHandler() {
        // 创建一个流控规则集合
        Set<GatewayFlowRule> rules = new HashSet<>();
        // 添加一个流控规则，针对"pay_routh1"路径，每秒最多处理2个请求，间隔1秒
        rules.add(new GatewayFlowRule("pay_routh1").setCount(2).setIntervalSec(1));

        // 加载流控规则
        GatewayRuleManager.loadRules(rules);
        // 自定义处理限流逻辑的处理器
        BlockRequestHandler handler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
                // 创建一个Map用于存放返回的错误信息
                Map<String,String> map = new HashMap<>();
                // 设置错误代码和消息
                map.put("errorCode", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
                map.put("errorMessage", "请求太过频繁，系统忙不过来，触发限流(sentinel+gataway整合Case)");
                // 返回一个状态为429（请求过多）的响应，内容为上述错误信息的JSON格式
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(map));
            }
        };
        // 设置自定义的限流处理处理器
        GatewayCallbackManager.setBlockHandler(handler);
    }


}