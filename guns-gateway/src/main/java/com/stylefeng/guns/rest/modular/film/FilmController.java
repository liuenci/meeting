package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.CatVO;
import com.stylefeng.guns.api.film.vo.SourceVO;
import com.stylefeng.guns.api.film.vo.YearVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVo;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/film/")
public class FilmController {
    private static final String IMG_PRE = "http://img.liuencier.cn/";
    @Reference(interfaceClass = FilmServiceApi.class)
    private FilmServiceApi filmServiceApi;

    @GetMapping("getIndex")
    public ResponseVO<FilmIndexVo> getIndex() {
        FilmIndexVo filmIndexVo = new FilmIndexVo();
        filmIndexVo.setBanners(filmServiceApi.getBanners());
        filmIndexVo.setHotFilms(filmServiceApi.getHotFilms(true, 8));
        filmIndexVo.setSoonFilms(filmServiceApi.getSoonFilms(true, 8));
        filmIndexVo.setBoxRanking(filmServiceApi.getBoxRanking());
        filmIndexVo.setExpectRanking(filmServiceApi.getExpectRanking());
        filmIndexVo.setTop100(filmServiceApi.getTop());
        return ResponseVO.sussess(IMG_PRE, filmIndexVo);
    }

    @GetMapping(value = "getConditionList")
    public ResponseVO getConditionList(@RequestParam(name = "catId",required = false, defaultValue = "99") String catId,
                                       @RequestParam(name = "sourceId",required = false, defaultValue = "99") String sourceId,
                                       @RequestParam(name = "yearId",required = false, defaultValue = "99") String yearId) {
        FilmConditionVO filmConditionVO = new FilmConditionVO();

        boolean flag = false;
        List<CatVO> cats = filmServiceApi.getCats();
        List<CatVO> catResult = new ArrayList<>();
        CatVO cat = null;
        for (CatVO catVo : cats) {
            if (catVo.getCatId().equals("99")) {
                cat = catVo;
                continue;
            }
            if (catVo.getCatId().equals(catId)) {
                flag = true;
                catVo.setActive(true);
            } else {
                catVo.setActive(false);
            }

        }
        if (!flag) {
            cat.setActive(true);
        } else {
            cat.setActive(false);
        }
        catResult.add(cat);

        flag = false;
        List<SourceVO> sourceVOS = filmServiceApi.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        SourceVO sourceVO = null;
        for (SourceVO source : sourceVOS) {
            if (source.getSourceId().equals("99")) {
                sourceVO = source;
                continue;
            }
            if (source.getSourceId().equals(sourceId)) {
                flag = true;
                source.setActive(true);
            } else {
                source.setActive(false);
            }
            sourceResult.add(source);

        }
        if (!flag) {
            sourceVO.setActive(true);
        } else {
            sourceVO.setActive(false);
        }
        sourceResult.add(sourceVO);

        flag = false;
        List<YearVO> yearVOS = filmServiceApi.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        YearVO yearVO = null;
        for (YearVO year : yearVOS) {
            if (year.getYearId().equals("99")) {
                yearVO = year;
                continue;
            }
            if (year.getYearId().equals(yearId)) {
                flag = true;
                year.setActive(true);
            } else {
                year.setActive(false);
            }
            yearResult.add(year);

        }
        if (!flag) {
            yearVO.setActive(true);
        } else {
            yearVO.setActive(false);
        }
        yearResult.add(yearVO);
        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);
        return ResponseVO.sussess(filmConditionVO);
    }
}
