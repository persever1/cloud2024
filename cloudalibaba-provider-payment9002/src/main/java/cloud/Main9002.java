package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @FileName Main9001
 * @Description
 * @Author mark
 * @date 2024-06-30
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class Main9002
{
    public static void main(String[] args)
    {
        SpringApplication.run(Main9002.class,args);
    }
}
