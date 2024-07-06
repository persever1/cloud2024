package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @FileName OrderController
 * @Description
 * @Author mark
 * @date 2024-05-27
 **/
@RestController
public class OrderController{
    public static final String PaymentSrv_URL = "http://cloud-payment-service";//服务注册中心上的微服务名称
    //public static final String PaymentSrv_URL = "http://localhost:8001";//先写死，硬编码
    @Resource
    private RestTemplate restTemplate;

    /**
     * 模拟消费者发送GET请求来添加订单的操作。
     * 本方法底层实际调用的是POST方法，通过REST模板向指定URL发送请求，以添加订单信息。
     * 注意：由于是模拟GET请求，方法参数不需要标记为@RequestBody。
     *
     * @param payDTO 订单信息载体，包含支付相关的详细信息。
     * @return 返回处理结果，封装在ResultData对象中。
     */
    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO){
        // 通过REST模板向支付服务添加订单，并返回处理结果
        return restTemplate.postForObject(PaymentSrv_URL + "/pay/add",payDTO,ResultData.class);
    }

    /**
     * 从支付服务获取支付信息。
     * 这是一个通过REST模板向支付服务发起GET请求，以获取特定支付信息的函数。
     *
     * @param id 支付信息的唯一标识符，从URL路径变量中获取。
     * @return ResultData 返回一个包含支付信息的结果对象。如果请求成功，结果对象中将包含支付信息；否则，可能包含错误信息。
     */
    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id){
        // 向支付服务发送GET请求，获取指定ID的支付信息
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/"+id, ResultData.class, id);
    }
    /**
     * 通过Consul获取支付信息。
     *
     * <p>该方法不接受任何参数，通过调用REST模板向指定的支付服务URL发出GET请求，
     * 以获取支付相关的详细信息。</p>
     *
     * @return 返回从支付服务中获取到的信息，类型为String。
     */
    @GetMapping(value = "/consumer/pay/get/info")
    private String getInfoByConsul()
    {
        // 通过restTemplate向支付服务发起GET请求获取信息
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
    }
    @Resource
    private DiscoveryClient discoveryClient;
    /**
     * 查询并展示服务发现的结果。
     * 首先，该方法会列出所有已知的服务名称；
     * 然后，它会显示指定服务("cloud-payment-service")的所有实例的详细信息；
     * 最后，返回第一个找到的指定服务实例的ID和端口。
     *
     * @return 返回一个字符串，包含指定服务的第一个实例的ID和端口，格式为 ID:端口。
     */
    @GetMapping("/consumer/discovery")
    public String discovery()
    {
        // 获取并打印所有已知的服务名称
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        // 获取并打印指定服务("cloud-payment-service")的所有实例信息
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        // 返回指定服务的第一个实例的ID和端口
        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }

}
