package com.stylefeng.guns.rest.modular.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.alipay.AliPayServiceAPI;
import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: meeting
 * @description:
 * @author: liuenci
 * @create: 2020-08-18 10:06
 **/
@Slf4j
@Component
@Service(interfaceClass = AliPayServiceAPI.class, mock = "com.stylefeng.guns.api.alipay.AliPayServiceMock")
public class DefaultAlipayServiceImpl implements AliPayServiceAPI {

    @Reference(interfaceClass = OrderServiceAPI.class, check = false, group = "order2018")
    private OrderServiceAPI orderServiceAPI;

    @Autowired
    @Override
    public AliPayInfoVO getQRCode(String orderId) {
        return null;
    }

    @Override
    public AliPayResultVO getOrderStatus(String orderId) {
        return null;
    }
}
