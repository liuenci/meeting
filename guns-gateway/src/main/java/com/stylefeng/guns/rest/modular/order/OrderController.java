package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stylefeng.guns.api.alipay.AliPayServiceAPI;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.util.TokenBucket;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: meeting
 * @description: 订单控制器
 * @author: liuenci
 * @create: 2020-08-03 20:11
 **/
@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {
    private static TokenBucket tokenBucket = new TokenBucket();
    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = OrderServiceAPI.class, check = false, group = "order2018")
    private OrderServiceAPI orderServiceAPI;

    @Reference(interfaceClass = OrderServiceAPI.class, check = false, group = "order2017")
    private OrderServiceAPI orderServiceAPI2017;

    @Reference(interfaceClass = AliPayServiceAPI.class,check = false)
    private AliPayServiceAPI aliPayServiceAPI;

    public ResponseVO error(Integer fieldId, String soldSeats, String seatsName) {
        return ResponseVO.serviceFail("抱歉，下单的人太多了，请稍后重试");
    }


    /**
     * @Description: 购票 信号量隔离 线程池隔离 线程切换
     * @Author: liuenci
     * @Date: 2020-08-17
     */
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
    }, threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "1"),
            @HystrixProperty(name = "maxQueueSize", value = "10"),
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")
    })
    @RequestMapping(value = "buyTickets", method = RequestMethod.POST)
    public ResponseVO bugTickets(Integer fieldId, String soldSeats, String seatsName) {
        if (tokenBucket.getToken()) {
            // 验证售出的票是否为真
            boolean isTrue = orderServiceAPI.isTrueSeats(String.valueOf(fieldId), soldSeats);
            // 已经销售的座位里，有没有这些座位
            boolean isNotSold = orderServiceAPI.isNotSoldSeats(String.valueOf(fieldId), soldSeats);
            if (isTrue && isNotSold) {
                // 创建订单信息，注意获取登陆人
                String currentUser = CurrentUser.getCurrentUser();
                if (StringUtils.isBlank(currentUser)) {
                    return ResponseVO.serviceFail("用户未登陆");
                }
                OrderVO orderVO = orderServiceAPI.saveOrderInfo(fieldId, soldSeats, seatsName, Integer.parseInt(currentUser));
                if (orderVO == null) {
                    log.error("field:[{}] soldSeats:[{}] seatsName:[{}]", fieldId, soldSeats, seatsName);
                    return ResponseVO.serviceFail("网络连接异常，请重试");
                } else {
                    return ResponseVO.success(orderVO);
                }
            } else {
                return ResponseVO.serviceFail("订单中的座位编号异常");
            }
        } else {
            return ResponseVO.serviceFail("购票人数过多,请稍后重试");
        }
    }

    @RequestMapping(value = "getOrderInfo", method = RequestMethod.POST)
    public ResponseVO getOrderInfo(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize
    ) {
        String currentUser = CurrentUser.getCurrentUser();
        Page<OrderVO> page = new Page<>(nowPage, pageSize);
        if (StringUtils.isNotBlank(currentUser)) {
            Page<OrderVO> order2018 = orderServiceAPI.getOrderByUserId(Integer.parseInt(currentUser), page);
            Page<OrderVO> order2017 = orderServiceAPI2017.getOrderByUserId(Integer.parseInt(currentUser), page);
            long totalPages = order2018.getPages() + order2017.getPages();
            List<OrderVO> orderVOS = new ArrayList<>();
            orderVOS.addAll(order2017.getRecords());
            orderVOS.addAll(order2018.getRecords());
            return ResponseVO.success(nowPage, (int)totalPages, "", orderVOS);
        }
        return null;
    }

    @RequestMapping(value = "getPayInfo", method = RequestMethod.POST)
    public ResponseVO getPayInfo(@RequestParam("orderId") String orderId) {
        String currentUser = CurrentUser.getCurrentUser();
        if (StringUtils.isBlank(currentUser)) {
            return ResponseVO.serviceFail("用户未登陆");
        }
        // TODO 订单二维码返回结果
        return ResponseVO.success(IMG_PRE, "");
    }

    @RequestMapping(value = "getPayResult", method = RequestMethod.POST)
    public ResponseVO getPayResult(@RequestParam("orderId") String orderId,
                                   @RequestParam(name = "tryNums", required = false, defaultValue = "1") Integer tryNums) {
        String currentUser = CurrentUser.getCurrentUser();
        if (StringUtils.isBlank(currentUser)) {
            return ResponseVO.serviceFail("用户未登陆");
        } else {
            // TODO 支付接口
            return ResponseVO.success("支付成功");
        }
    }

}
