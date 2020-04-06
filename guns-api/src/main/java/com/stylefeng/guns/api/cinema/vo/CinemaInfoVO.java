package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description: 影院信息VO
 * @author: liuenci
 * @create: 2020-04-06 16:53
 **/
@Data
public class CinemaInfoVO implements Serializable {
    private String cinemaId;
    private String imgUrl;
    private String cinemaName;
    private String cinemaAddress;
    private String cinemaPhone;
}
