package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description:
 * @author: liuenci
 * @create: 2020-08-18 10:02
 **/
@Data
public class AliPayInfoVO implements Serializable {
    private static final long serialVersionUID = -6401179600145062035L;
    private String orderId;
    private String QRCodeAddress;
}
