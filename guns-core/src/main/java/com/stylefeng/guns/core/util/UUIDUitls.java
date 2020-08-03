package com.stylefeng.guns.core.util;

import java.util.UUID;

/**
 * @program: meeting
 * @description: UUID工具类
 * @author: liuenci
 * @create: 2020-08-03 19:23
 **/
public class UUIDUitls {
    public static String genUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
