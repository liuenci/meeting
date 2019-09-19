package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmVo implements Serializable {
    private Integer filmNum;
    private List<FilmInfo> filmInfo;
}
