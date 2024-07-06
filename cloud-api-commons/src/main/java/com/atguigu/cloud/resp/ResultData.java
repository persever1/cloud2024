package com.atguigu.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @FileName ResultData
 * @Description
 * @Author mark
 * @date 2024-05-27
 **/
@Data
@Accessors(chain = true)
public class ResultData<T> {

    private String code;/** 结果状态 ,具体状态码参见枚举类ReturnCodeEnum.java*/
    private String message;
    private T data;
    private long timestamp ;


    public ResultData (){
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 生成一个表示成功的结果数据对象。
     *
     * @param data 成功时返回的数据。
     * @param <T> 数据的类型。
     * @return 返回一个包含成功代码、成功消息和实际数据的ResultData对象。
     */
    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        // 设置返回码为成功代码，消息为成功消息
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
        // 设置返回的数据
        resultData.setData(data);
        return resultData;
    }

    /**
     * 生成一个失败的结果数据对象。
     *
     * @param code 错误代码，用于标识具体的错误类型。
     * @param message 错误消息，用于描述错误的详细信息。
     * @param <T> 结果数据的泛型类型。
     * @return 返回一个初始化为失败状态的 ResultData 对象，其中包含了指定的错误代码和消息。
     */
    public static <T> ResultData<T> fail(String code, String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(code); // 设置错误代码
        resultData.setMessage(message); // 设置错误消息
        return resultData;
    }

}

