package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName PayController
 * @Description
 * @Author mark
 * @date 2024-05-27
 **/
@RestController
@Slf4j
@Tag(name = "支付微服务模块", description = "支付CRUD")
public class PayController {
    @Resource
    private PayService payService;

    /**
     * 添加支付记录
     * <p>
     * 通过接收前端传来的Pay对象，将其添加到支付记录中。
     * 使用@RequestBody注解表明pay参数是从请求体中获取的JSON对象转换而来的。
     * </p>
     *
     * @param pay 包含支付信息的对象，该对象从客户端提交的JSON数据转换而来。
     * @return 返回一个结果对象，其中包含操作成功与否的信息及插入的记录数。
     */
    @Operation(summary = "添加支付记录", description = "添加支付记录,json串做参数")
    @PostMapping(value = "/pay/add")
    public ResultData<String> addPay(@RequestBody Pay pay) {
        // 调用服务层方法，尝试添加支付记录
        int i = payService.add(pay);
        // 根据添加结果返回相应的操作状态
        if (i == 0) {
            return ResultData.fail("999", "添加失败");
        }
        // 添加成功，返回成功信息及插入的记录数
        return ResultData.success("成功插入记录，返回值：" + i);
    }


    /**
     * 删除支付记录
     *
     * @param id 支付记录的唯一标识符，通过URL路径变量传递。此标识符用于确定要删除的特定支付记录。
     * @return 返回一个结果对象，包含删除操作影响的行数。如果删除操作成功，将返回受影响的行数；如果失败，将返回相应的错误信息。
     */
    @Operation(summary = "删除支付记录", description = "删除支付记录，根据id")
    @DeleteMapping(value = "/pay/del/{id}")
    public ResultData<Integer> deletePay(@PathVariable("id") Integer id) {

        // 调用支付服务的删除方法，尝试根据id删除支付记录
        int i = payService.delete(id);
        // 如果删除操作没有影响到任何行数，则视为删除失败
        if (i == 0) {
            return ResultData.fail("999", "删除失败");
        }
        // 如果删除成功，返回删除的行数
        return ResultData.success(i);
    }


    /**
     * 修改支付记录
     * 使用JSON格式的支付信息作为参数，更新数据库中的支付记录。
     *
     * @param payDTO 包含支付信息的DTO（数据传输对象），通过RequestBody接收前端传来的JSON数据。
     *               其中包含了需要更新的支付信息。
     * @return ResultData<String> 返回一个结果数据对象，其中包含操作结果的信息。
     *         如果操作成功，会返回成功信息和更新的记录数；如果失败，则返回失败原因。
     */
    @Operation(summary = "修改支付记录", description = "修改支付记录，json串做参数")
    @PutMapping(value = "/pay/update")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {
        // 创建一个新的Pay对象，并从payDTO复制属性值
        Pay pay = new Pay();
        BeanUtils.copyProperties(payDTO, pay);
        // 调用支付服务，尝试更新支付记录
        int i = payService.update(pay);
        // 根据更新结果返回相应的操作结果
        if (i == 0) {
            return ResultData.fail("999", "修改失败");
        }
        return ResultData.success("成功修改记录，返回值：" + i);
    }


    /**
     * 根据支付记录的id进行查询
     *
     * @param id 支付记录的唯一标识符
     * @return ResultData<PayDTO> 包含查询到的支付记录详情的PayDTO对象，如果未查询到则为空
     */
    @Operation(summary = "根据id查询支付记录", description = "根据id查询支付记录")
    @GetMapping(value = "/pay/get/{id}")
    public ResultData<PayDTO> getById(@PathVariable("id") Integer id) {
        Pay pay = payService.getById(id);
        if (pay == null) {
            return ResultData.fail("999", "未查询到记录");
        }
        // 创建PayDTO对象，准备填充数据
        PayDTO payDTO = new PayDTO();
        // 从支付服务中根据id查询支付记录，并将其属性复制到payDTO对象中
        BeanUtils.copyProperties(pay, payDTO);
        // 返回成功响应，包含填充好的payDTO对象
        return ResultData.success(payDTO);
    }

    /**
     * 查询所有支付记录
     * <p>
     * 该接口用于查询系统中的所有支付记录。不接受任何请求参数，
     * 返回所有支付记录的列表。支付记录被转换为PayDTO对象列表，以方便前端应用程序使用。
     *
     * @return ResultData<List < PayDTO>> 包含所有支付记录的PayDTO对象列表。如果查询成功，结果的status为SUCCESS，如果失败则为FAIL。
     */
    @Operation(summary = "查询所有支付记录", description = "查询所有支付记录")
    @GetMapping(value = "/pay/getall")
    public ResultData<List<PayDTO>> getAll() {
        // 从支付服务获取所有支付记录
        List<Pay> pays = payService.gatAll();
        // 初始化支付记录DTO列表
        List<PayDTO> payDtos = new ArrayList<>();
        // 遍历支付记录，将每个Pay对象转换为PayDTO对象后添加到列表中
        for (Pay pay : pays) {
            PayDTO payDTO = new PayDTO();
            BeanUtils.copyProperties(pay, payDTO);
            payDtos.add(payDTO);
        }
        // 返回包含所有转换后的支付记录的ResultData对象
        return ResultData.success(payDtos);
    }
    /**
     * 通过@Value注解从应用配置中动态注入服务器端口。
     * 这个属性用于存储服务器的端口号，其值来自于应用的配置文件中"server.port"的配置项。
     *
     * @Value注解的作用是将配置文件中的值直接注入到字段中，无需手动设置。
     * 使用该方式可以使得应用配置更加灵活，便于不同环境下的配置管理。
     */
    @Value("${server.port}")
    private String port;

    /**
     * 通过Consul获取配置信息
     *
     * @param atguiguInfo 通过@Value注解从Consul配置中心动态获取的atguigu.info配置值
     * @return 返回包含atguiguInfo和当前服务端口信息的字符串
     */
    @GetMapping(value = "/pay/get/info")
    private String getInfoByConsul(@Value("${atguigu.info}") String atguiguInfo)
    {
        // 组装并返回包含配置信息和端口的字符串
        return "atguiguInfo: "+atguiguInfo+"\t"+"port: "+port;
    }

}
