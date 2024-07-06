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
    /**
     * 创建并配置支付微服务模块的GroupedOpenApi。
     * 该方法不接受任何参数。
     *
     * @return GroupedOpenApi 对象，用于将支付相关的API路径分组到一个指定的组中。
     */
    @Bean
    public GroupedOpenApi PayApi()
    {
        // 构建并返回一个GroupedOpenApi对象，将所有以"/pay/"开头的路径归入“支付微服务模块”组
        return GroupedOpenApi.builder().group("支付微服务模块").pathsToMatch("/pay/**").build();
    }

    /**
     * 创建并配置一个表示“其它微服务模块”的GroupedOpenApi Bean。
     * 这个方法不接受任何参数。
     *
     * @return GroupedOpenApi 对象，它定义了一个API组，包含了所有匹配"/other/**"和"/others"路径的API。
     */
    @Bean
    public GroupedOpenApi OtherApi()
    {
        // 构建并返回一个GroupedOpenApi对象，该对象设置了API分组名称和需要包含的路径。
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
     *         通过这个方法配置的OpenAPI对象可以被Spring Boot自动识别并用于文档展示。
     */
    @Bean
    public OpenAPI docsOpenApi()
    {
        // 初始化OpenAPI对象，并设置API的基本信息，包括标题、描述和版本
        return new OpenAPI()
                .info(new Info().title("cloud2024")
                        .description("通用设计rest")
                        .version("v1.0"))
                // 配置外部文档链接，提供额外的、与API相关的文档资源
                .externalDocs(new ExternalDocumentation()
                        .description("cloud2024测试界面")
                        .url("https://yiyan.baidu.com/"));
    }


}