package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = FilmServiceApi.class)
public class DefaultFilmServiceImpl implements FilmServiceApi {
    @Autowired
    private MoocBannerTMapper moocBannerTMapper;
    @Autowired
    private MoocFilmTMapper moocFilmTMapper;
    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;
    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;
    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;
    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;
    @Autowired
    private MoocActorTMapper moocActorTMapper;

    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> result = new ArrayList<>();
        List<MoocBannerT> moocBannerTS = moocBannerTMapper.selectList(null);
        moocBannerTS.forEach(moocBannerT -> {
            BannerVO bannerVo = new BannerVO();
            bannerVo.setBannerId(moocBannerT.getUuid() + "");
            bannerVo.setBannerUrl(moocBannerT.getBannerUrl());
            bannerVo.setBannerAddress(moocBannerT.getBannerAddress());
            result.add(bannerVo);
        });
        return result;
    }

    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVo = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        if (isLimit) {
            // 如果是，则限制条数、限制内容为热映影片
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(filmInfos.size());
            filmVo.setFilmInfo(filmInfos);
        } else {
            // 如果不是，则是列表页，同样需要限制内容为热映影片
            Page<MoocFilmT> page = null;
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
            }
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(filmInfos.size());

            // 需要总页数 = totalCounts / nums + 1
            int totalCounts = moocFilmTMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts / nums) + 1;
            filmVo.setTotalPages(totalPages);
            filmVo.setNowPage(nowPage);
            filmVo.setFilmInfo(filmInfos);
        }
        return filmVo;
    }

    private List<FilmInfo> getFilmInfos(List<MoocFilmT> moocFilmTS) {
        List<FilmInfo> filmInfos = new ArrayList<>();
        moocFilmTS.forEach(moocFilmT -> {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setScore(moocFilmT.getFilmScore());
            filmInfo.setImgAddress(moocFilmT.getImgAddress());
            filmInfo.setFilmType(moocFilmT.getFilmType());
            filmInfo.setFilmName(moocFilmT.getFilmName());
            filmInfo.setFilmId(moocFilmT.getUuid() + "");
            filmInfo.setExpectNum(moocFilmT.getFilmPresalenum());
            filmInfo.setBoxNum(moocFilmT.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(moocFilmT.getFilmTime()));
            filmInfos.add(filmInfo);
        });
        return filmInfos;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVo = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        if (isLimit) {
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(filmInfos.size());
            filmVo.setFilmInfo(filmInfos);
        } else {
            // 如果不是，则是列表页，同样需要限制内容为即将上映影片
            Page<MoocFilmT> page = null;
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
            }
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("file_cat", catStr);
            }
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(filmInfos.size());

            // 需要总页数 = totalCounts / nums + 1
            int totalCounts = moocFilmTMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts / nums) + 1;
            filmVo.setTotalPages(totalPages);
            filmVo.setNowPage(nowPage);
            filmVo.setFilmInfo(filmInfos);
        }
        return filmVo;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_box_office");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_score");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<CatVO> getCats() {
        List<MoocCatDictT> moocCatDictTS = moocCatDictTMapper.selectList(null);
        List<CatVO> catVOS = new ArrayList<>();
        moocCatDictTS.forEach(moocCatDictT -> {
            CatVO catVO = new CatVO();
            catVO.setCatId(moocCatDictT.getUuid() + "");
            catVO.setCatName(moocCatDictT.getShowName());
            catVOS.add(catVO);
        });
        return catVOS;
    }

    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sourceVOS = new ArrayList<>();
        List<MoocSourceDictT> moocSourceDictTS = moocSourceDictTMapper.selectList(null);
        moocSourceDictTS.forEach(moocSourceDictT -> {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(moocSourceDictT.getUuid() + "");
            sourceVO.setSourceName(moocSourceDictT.getShowName());
            sourceVOS.add(sourceVO);
        });
        return sourceVOS;
    }

    @Override
    public List<YearVO> getYears() {
        List<MoocYearDictT> moocYearDictTS = moocYearDictTMapper.selectList(null);
        List<YearVO> yearVOS = new ArrayList<>();
        moocYearDictTS.forEach(moocYearDictT -> {
            YearVO yearVO = new YearVO();
            yearVO.setYearId(moocYearDictT.getUuid() + "");
            yearVO.setYearName(moocYearDictT.getShowName());
            yearVOS.add(yearVO);
        });
        return yearVOS;
    }

    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        // searchType 1-按名称 2-按ID查找
        FilmDetailVO filmDetailVO = null;
        if (searchType == 1) {
            filmDetailVO = moocFilmTMapper.getFilmDetailByName("%" + searchParam + "%");
        } else {
            filmDetailVO = moocFilmTMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVO;
    }

    private MoocFilmInfoT getFilmInfo(String filmId) {
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        moocFilmInfoT.setFilmId(filmId);
        moocFilmInfoT = moocFilmInfoTMapper.selectOne(moocFilmInfoT);
        return moocFilmInfoT;
    }

    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(moocFilmInfoT.getBiography());
        filmDescVO.setFilmId(filmId);
        return filmDescVO;
    }

    @Override
    public ImgVO getImgs(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        String filmImgStr = moocFilmInfoT.getFilmImgs();
        String[] filmImgs = filmImgStr.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(filmImgs[0]);
        imgVO.setImg01(filmImgs[1]);
        imgVO.setImg02(filmImgs[2]);
        imgVO.setImg03(filmImgs[3]);
        imgVO.setImg04(filmImgs[4]);
        return imgVO;
    }

    @Override
    public ActorVO getDectInfo(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        Integer directId = moocFilmInfoT.getDirectorId();
        MoocActorT moocActorT = moocActorTMapper.selectById(directId);
        ActorVO actorVO = new ActorVO();
        actorVO.setImgAddress(moocActorT.getActorImg());
        actorVO.setDirectorName(moocActorT.getActorName());
        return actorVO;
    }

    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actorVOS = moocActorTMapper.getActors(filmId);
        return actorVOS;
    }

    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVo = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "3");
        // 如果不是，则是列表页，同样需要限制内容为经典影片
        Page<MoocFilmT> page = null;
        switch (sortId) {
            case 1:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
            case 2:
                page = new Page<>(nowPage, nums, "film_time");
                break;
            case 3:
                page = new Page<>(nowPage, nums, "film_score");
                break;
            default:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
        }
        if (sourceId != 99) {
            entityWrapper.eq("film_source", sourceId);
        }
        if (yearId != 99) {
            entityWrapper.eq("film_date", yearId);
        }
        if (catId != 99) {
            String catStr = "%#" + catId + "#%";
            entityWrapper.like("file_cat", catStr);
        }
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        filmInfos = getFilmInfos(moocFilmTS);
        filmVo.setFilmNum(filmInfos.size());

        // 需要总页数 = totalCounts / nums + 1
        int totalCounts = moocFilmTMapper.selectCount(entityWrapper);
        int totalPages = (totalCounts / nums) + 1;
        filmVo.setTotalPages(totalPages);
        filmVo.setNowPage(nowPage);
        filmVo.setFilmInfo(filmInfos);
        return filmVo;
    }
}
