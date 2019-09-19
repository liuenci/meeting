package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

import java.util.List;

@Data
public class FilmVo {
    private Integer filmNum;
    private List<FilmInfo> filmInfo;
}
