package com.atguigu.cloud.mygateway;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @FileName MyRoutePredicateFactory
 * @Description
 * @Author mark
 * @date 2024-06-21
 **/
@Component
public class MyRoutePredicateFactory extends AbstractRoutePredicateFactory<MyRoutePredicateFactory.Config> {

    /*该函数是一个构造函数，用于初始化MyRoutePredicateFactory类的实例。
     *它调用了super关键字来调用父类RoutePredicateFactory的构造函数，并传递了一个Config类的参数。
     *MyRoutePredicateFactory类应该是自定义的，
     *用于创建自定义的路由谓词工厂，而Config类应该是该工厂类的配置类，用于配置路由谓词的相关参数。
     */
    public MyRoutePredicateFactory() {
        super(MyRoutePredicateFactory.Config.class);
    }

    /*
     * 这段代码定义了一个名为Config的静态内部类，用于存储用户类型信息。
     * 该类具有一个私有字段userType，类型为String，用于存储钻、金、银等用户等级信息。
     * 该字段使用了@Getter和@Setter注解，可以使用对应的getter和setter方法进行访问和修改。
     * 此外，该字段还使用了@NotEmpty注解，表示该字段不能为空。
     * 最后，该类使用了@Validated注解，表示该类的实例在使用前需要进行验证。*/
    @Validated
    public static class Config {
        @Setter
        @Getter
        @NotEmpty
        private String userType; //钻、金、银等用户等级
    }

    /**
     * 根据给定的配置，应用特定的路由判断逻辑。
     * 该方法旨在创建并返回一个Predicate，该Predicate用于判断ServerWebExchange是否符合特定的条件。
     *
     * @param config MyRoutePredicateFactory的配置对象，包含用于判断的用户类型。
     * @return 返回一个Predicate，该Predicate用于对ServerWebExchange进行判断。
     */
    @Override
    public Predicate<ServerWebExchange> apply(MyRoutePredicateFactory.Config config) {
        // 创建并返回一个新的Predicate实例，用于判断ServerWebExchange是否符合特定条件
        return new Predicate<ServerWebExchange>() {
            /**
             * 对给定的ServerWebExchange进行判断。
             * 该方法检查交换中的请求是否包含"userType"查询参数，并且该参数的值是否与配置中的用户类型匹配。
             *
             * @param serverWebExchange 要判断的ServerWebExchange对象。
             * @return 如果查询参数"userType"存在且与配置中的用户类型匹配，则返回true；否则返回false。
             */
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                // 从请求的查询参数中获取"userType"的值
                // 检查request的参数里面，userType是否为指定的值，符合配置就通过
                String userType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");

                // 如果"userType"查询参数不存在，则返回false
                if (userType == null) return false;

                // 检查获取到的"userType"值是否与配置中的用户类型匹配
                // 如果参数存在，就和config的数据进行比较
                if (userType.equals(config.getUserType())) {
                    return true;
                }

                // 如果"userType"值与配置中的用户类型不匹配，则返回false
                return false;
            }
        };
    }


    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("userType");
    }


}
