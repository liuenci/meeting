package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.BannerVo;
import com.stylefeng.guns.api.film.vo.FilmInfo;
import com.stylefeng.guns.api.film.vo.FilmVo;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocBannerTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocBannerT;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilmT;
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
    @Override
    public List<BannerVo> getBanners() {
        List<BannerVo> result = new ArrayList<>();
        List<MoocBannerT> moocBannerTS = moocBannerTMapper.selectList(null);
        moocBannerTS.forEach(moocBannerT -> {
            BannerVo bannerVo = new BannerVo();
            bannerVo.setBannerId(moocBannerT.getUuid() + "");
            bannerVo.setBannerUrl(moocBannerT.getBannerUrl());
            bannerVo.setBannerAddress(moocBannerT.getBannerAddress());
            result.add(bannerVo);
        });
        return result;
    }

    @Override
    public FilmVo getHotFilms(boolean isLimit, int nums) {
        FilmVo filmVo = new FilmVo();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");
        if (isLimit) {
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(filmInfos.size());
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
    public FilmVo getSoonFilms(boolean isLimit, int nums) {
        FilmVo filmVo = new FilmVo();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");
        if (isLimit) {
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(filmInfos.size());
            filmVo.setFilmInfo(filmInfos);
        }
        return filmVo;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_box_office");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_score");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }
}
