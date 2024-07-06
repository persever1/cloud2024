package com.atguigu.cloud.exp;

import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @FileName GlobalExceptionHandler
 * @Description
 * @Author mark
 * @date 2024-05-27
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 默认全局异常处理。
     * 该方法用于捕获运行时异常，并返回一个包含错误信息的结果对象。
     *
     * @param e 异常对象，表示发生的运行时异常。
     * @return ResultData<String> 返回一个结果数据对象，其中包含错误代码和错误信息。
     */

    @ExceptionHandler(RuntimeException.class)//通过在方法上添加@ExceptionHandler注解，使得该方法能够捕获到匹配的异常类型，并且针对这些异常执行自定义的异常处理逻辑。此处指定的异常类型为RuntimeException.class，即能够处理所有的运行时异常。

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//用于设置响应状态为服务器内部错误（HTTP 500）的注解。该注解通常应用于异常处理的方法上，以指定当方法抛出异常时，服务器应返回的HTTP状态码为500。
    public ResultData<String> exception(Exception e) {
        // 打印进入全局异常处理器的信息
        System.out.println("----come in GlobalExceptionHandler");
        // 记录异常信息到日志
        log.error("全局异常信息exception:{}", e.getMessage(), e);
        // 返回一个失败的结果，包含错误代码和错误信息
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
    }

}