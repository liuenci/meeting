package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description: 订单查询VO
 * @author: liuenci
 * @create: 2020-07-27 19:07
 **/
@Data
public class OrderQueryVO implements Serializable {
    private String cinemaId;
    private String filmPrice;
}
