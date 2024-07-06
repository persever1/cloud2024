package com.atguigu.cloud.resp;

import lombok.Getter;

import java.util.Arrays;

/**
 * @FileName ReturnCodeEnum
 * @Description
 * @Author mark
 * @date 2024-05-27
 **/
@Getter
public enum ReturnCodeEnum {
    /**
     * 操作失败
     **/
    RC999("999", "操作失败"),
    /**
     * 操作成功
     **/
    RC200("200", "success"),
    /**
     * 服务降级
     **/
    RC201("201", "服务开启降级保护,请稍后再试!"),
    /**
     * 热点参数限流
     **/
    RC202("202", "热点参数限流,请稍后再试!"),
    /**
     * 系统规则不满足
     **/
    RC203("203", "系统规则不满足要求,请稍后再试!"),
    /**
     * 授权规则不通过
     **/
    RC204("204", "授权规则不通过,请稍后再试!"),
    /**
     * access_denied
     **/
    RC403("403", "无访问权限,请联系管理员授予权限"),
    /**
     * access_denied
     **/
    RC401("401", "匿名用户访问无权限资源时的异常"),
    RC404("404", "404页面找不到的异常"),
    /**
     * 服务异常
     **/
    RC500("500", "系统异常，请稍后重试"),
    RC375("375", "数学运算异常，请稍后重试"),

    INVALID_TOKEN("2001", "访问令牌不合法"),
    ACCESS_DENIED("2003", "没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED("1001", "客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR("1002", "用户名或密码错误"),
    BUSINESS_ERROR("1004", "业务逻辑异常"),
    UNSUPPORTED_GRANT_TYPE("1003", "不支持的认证模式");

    /**
     * 自定义状态码
     **/
    private final String code;
    /**
     * 自定义描述
     **/
    private final String message;

    ReturnCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

//    /**
//     * 遍历枚举V1，根据提供的代码字符串匹配相应的枚举实例。
//     *
//     * @param code 指定的代码字符串，用于查找相匹配的枚举实例。此参数不区分大小写。
//     * @return 如果找到与指定代码匹配的枚举实例，则返回该实例；如果没有找到匹配项，则返回null。
//     */
//    public static ReturnCodeEnum getReturnCodeEnum(String code)
//    {
//        // 遍历所有的枚举实例，查找代码匹配的实例
//        for (ReturnCodeEnum element : ReturnCodeEnum.values()) {
//            if(element.getCode().equalsIgnoreCase(code))
//            {
//                // 找到匹配的枚举实例，返回该实例
//                return element;
//            }
//        }
//        // 如果没有找到匹配的枚举实例，则返回null
//        return null;
//    }

    /**
     * 遍历枚举V2，根据提供的代码字符串匹配对应的枚举实例。
     *
     * @param code 指定的代码字符串，用于匹配枚举实例。该参数不区分大小写。
     * @return 返回与指定代码字符串匹配的枚举实例。如果没有找到匹配的枚举实例，则返回null。
     */
    public static ReturnCodeEnum getReturnCodeEnumV2(String code) {
        // 使用流遍历所有的ReturnCodeEnum枚举实例，并筛选出代码字段与输入参数匹配的实例
        return Arrays.stream(ReturnCodeEnum.values()).filter(x -> x.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }


}