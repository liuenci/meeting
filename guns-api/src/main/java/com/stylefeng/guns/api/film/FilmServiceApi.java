package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.BannerVO;
import com.stylefeng.guns.api.film.vo.FilmInfo;
import com.stylefeng.guns.api.film.vo.FilmVO;

import java.util.List;

public interface FilmServiceApi {
    List<BannerVO> getBanners();
    FilmVO getHotFilms(boolean isLimit, int nums);
    FilmVO getSoonFilms(boolean isLimit, int nums);
    List<FilmInfo> getBoxRanking();
    List<FilmInfo> getExpectRanking();
    List<FilmInfo> getTop();
}
