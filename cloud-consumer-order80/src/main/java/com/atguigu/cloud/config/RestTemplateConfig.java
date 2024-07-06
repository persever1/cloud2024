package com.atguigu.cloud.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * @FileName RestTemplateConfig
 * @Description
 * @Author mark
 * @date 2024-05-27
 **/
@Configuration
@LoadBalancerClient(
        //下面的value值大小写一定要和consul里面的名字一样，必须一样
        value = "cloud-payment-service", configuration = RestTemplateConfig.class)
public class RestTemplateConfig {
    /**
     * 创建并返回一个具有负载均衡能力的RestTemplate实例。
     * 使用@LoadBalanced注解来启用RestTemplate的负载均衡功能，
     * 这使得RestTemplate在进行REST调用时能够自动地在服务集群中的多个实例间进行负载。
     *
     * @return RestTemplate 具有负载均衡能力的RestTemplate实例。
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 创建一个随机负载均衡器的Bean实例。
     * 这个方法配置了一个基于随机算法的选择器，用于从服务实例列表中随机选择一个实例进行负载。
     *
     * @param environment 提供环境配置，用于获取负载均衡器的名称。
     * @param loadBalancerClientFactory 负载均衡器客户端工厂，用于根据名称获取服务实例列表的供应商。
     * @return 返回一个配置好的ReactorLoadBalancer实例，它使用随机算法来选择服务实例。
     */
    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        // 从环境配置中获取负载均衡器的名称
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

        // 创建并返回一个随机负载均衡器实例
        return new RandomLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }

}

