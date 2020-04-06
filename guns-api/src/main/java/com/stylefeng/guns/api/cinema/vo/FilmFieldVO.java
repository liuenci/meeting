package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: meeting
 * @description: 影片详情字段
 * @author: liuenci
 * @create: 2020-04-06 16:59
 **/
@Data
public class FilmFieldVO implements Serializable {
    private String fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private String price;
}
