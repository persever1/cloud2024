package com.atguigu.cloud.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @FileName Swagger3Config
 * @Description
 * @Author mark
 * @date 2024-05-27
 **/
@Configuration
public class Swagger3Config
{
    @Bean
    public GroupedOpenApi PayApi()
    {
        return GroupedOpenApi.builder().group("支付微服务模块").pathsToMatch("/pay/**").build();
    }
    @Bean
    public GroupedOpenApi OtherApi()
    {
        return GroupedOpenApi.builder().group("其它微服务模块").pathsToMatch("/other/**", "/others").build();
    }
    /*@Bean
    public GroupedOpenApi CustomerApi()
    {
        return GroupedOpenApi.builder().group("客户微服务模块").pathsToMatch("/customer/**", "/customers").build();
    }*/

    /**
     * 创建并配置OpenAPI文档 Bean。
     * 这个方法不接受任何参数，主要用于初始化和配置OpenAPI对象。
     *
     * @return OpenAPI 对象，包含了API的元数据，如标题、描述、版本以及外部文档链接。
     */
    @Bean
    public OpenAPI docsOpenApi()
    {
        // 初始化OpenAPI对象，并配置基本信息
        return new OpenAPI()
                .info(new Info().title("cloud2024")
                        .description("通用设计rest")
                        .version("v1.0"))
                // 配置外部文档链接
                .externalDocs(new ExternalDocumentation()
                        .description("cloud2024测试界面")
                        .url("https://yiyan.baidu.com/"));
    }

}