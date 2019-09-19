package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

import java.util.List;

@Data
public class FilmIndexVo {
    private List<BannerVo> banners;
    private FilmVo hotFilms;
    private FilmVo soonFilms;
    private List<FilmInfo> boxRanking;
    private List<FilmInfo> expectRanking;
    private List<FilmInfo> top100;
}
