package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description: 区域VO
 * @author: liuenci
 * @create: 2020-04-06 16:50
 **/
@Data
public class AreaVO implements Serializable {
    private String areaId;
    private String areaName;
    private boolean isActive;
}
