package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description: 品牌VO
 * @author: liuenci
 * @create: 2020-04-06 16:49
 **/
@Data
public class BrandVO implements Serializable {
    private String brandId;
    private String brandName;
    private Boolean isActive;
}
