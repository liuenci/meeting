package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description:
 * @author: liuenci
 * @create: 2020-08-18 10:03
 **/
@Data
public class AliPayResultVO implements Serializable {
    private static final long serialVersionUID = -4854323205997856606L;
    private String orderId;
    private Integer orderStatus;
    private String orderMsg;
}
