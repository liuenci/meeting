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
    // 根据影片ID或者影片名称获取影片信息
    FilmDetailVO getFilmDetail(int searchType, String searchParam);
    // 获取影片相关的其他信息【演员表、图片地址】
    // 获取影片描述信息
    FilmDescVO getFilmDesc(String filmId);
    // 获取图片信息
    ImgVO getImgs(String filmId);
    // 获取导演信息
    ActorVO getDectInfo(String filmId);
    // 获取演员信息
    List<ActorVO> getActors(String filmId);
}
