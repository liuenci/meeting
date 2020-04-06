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
public class HallTypeVO implements Serializable {
    private String hallTypeId;
    private String hallTypeName;
    private boolean isActive;
}
