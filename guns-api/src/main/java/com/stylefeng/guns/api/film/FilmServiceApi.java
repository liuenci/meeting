package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.BannerVo;
import com.stylefeng.guns.api.film.vo.FilmInfo;
import com.stylefeng.guns.api.film.vo.FilmVo;

import java.util.List;

public interface FilmServiceApi {
    List<BannerVo> getBanners();
    FilmVo getHotFilms(boolean isLimit,int nums);
    FilmVo getSoonFilms(boolean isLimit, int nums);
    List<FilmInfo> getBoxRanking();
    List<FilmInfo> getExpectRanking();
    List<FilmInfo> getTop();
}
