package test;

import java.time.ZonedDateTime;

/**
 * @FileName ZonedDateTimeDemo
 * @Description
 * @Author mark
 * @date 2024-06-21
 **/
public class ZonedDateTimeDemo
{
    public static void main(String[] args)
    {
        ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
        System.out.println(zbj);
    }
}