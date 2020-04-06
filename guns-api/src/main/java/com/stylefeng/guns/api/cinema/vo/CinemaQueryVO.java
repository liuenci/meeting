package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description: 影院查询model
 * @author: liuenci
 * @create: 2020-04-06 16:24
 **/
@Data
public class CinemaQueryVO implements Serializable {
    private Integer brandId = 99;
    private Integer districtId = 99;
    private Integer hallType = 99;
    private Integer pageSize = 12;
    private Integer nowPage = 1;
}
