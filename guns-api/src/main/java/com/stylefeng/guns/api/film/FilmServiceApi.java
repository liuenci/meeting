package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmServiceApi {
    List<BannerVO> getBanners();
    FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);
    FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);
    FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);
    List<FilmInfo> getBoxRanking();
    List<FilmInfo> getExpectRanking();
    List<FilmInfo> getTop();
    List<CatVO> getCats();
    List<SourceVO> getSources();
    List<YearVO> getYears();
}
