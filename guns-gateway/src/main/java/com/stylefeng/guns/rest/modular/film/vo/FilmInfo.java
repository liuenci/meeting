package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

@Data
public class FilmInfo {
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
    private Integer expectNum;
    private String showTime;
    private Integer boxNum;
    private Integer score;
}
