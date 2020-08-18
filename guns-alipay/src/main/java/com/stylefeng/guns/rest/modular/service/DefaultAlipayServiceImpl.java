package com.stylefeng.guns.rest.modular.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.alipay.AliPayServiceAPI;
import com.stylefeng.guns.api.alipay.vo.AliPayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AliPayResultVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.core.util.FTPUtils;
import com.stylefeng.guns.rest.modular.alipay.config.Configs;
import com.stylefeng.guns.rest.modular.alipay.service.AlipayMonitorService;
import com.stylefeng.guns.rest.modular.alipay.service.AlipayTradeService;
import com.stylefeng.guns.rest.modular.alipay.service.impl.AlipayMonitorServiceImpl;
import com.stylefeng.guns.rest.modular.alipay.service.impl.AlipayTradeServiceImpl;
import com.stylefeng.guns.rest.modular.alipay.service.impl.AlipayTradeWithHBServiceImpl;
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
    private FTPUtils ftpUtils;

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口API使用，有一份 readme.txt 可供使用
    private static AlipayMonitorService monitorService;

    static {
        // 一定要在创建AlipayTradeService之前调用Config.init()设置默认参数
        // Configs会读取classpath下的zfbinfo.properties文件配置信息,如果找不到该文件则确认该文件是否在classpath目录
        Configs.init("zfbinfo.properties");

        // 使用Configs提供的默认参数
        // AlipayTradeService 可以使用单例或者静态成员对象，不需要反复new
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        // 如果需要在程序中覆盖Configs提供的默认参数,可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do")
                .setCharset("GBK")
                .setFormat("json")
                .build();
    }

    @Override
    public AliPayInfoVO getQRCode(String orderId) {
        return null;
    }

    @Override
    public AliPayResultVO getOrderStatus(String orderId) {
        return null;
    }
}
