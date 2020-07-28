package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description: 影院VO
 * @author: liuenci
 * @create: 2020-04-06 16:47
 **/
@Data
public class CinemaVO implements Serializable {
    private String uuid;
    private String cinemaName;
    private String address;
    private String minimumPrice;
}
