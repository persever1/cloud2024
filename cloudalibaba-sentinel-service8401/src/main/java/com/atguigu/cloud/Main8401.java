package com.atguigu.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @FileName Main8401
 * @Description
 * @Author mark
 * @date 2024-07-01
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class Main8401 {
    public static void main(String[] args)
    {
        SpringApplication.run(Main8401.class,args);
    }
}
