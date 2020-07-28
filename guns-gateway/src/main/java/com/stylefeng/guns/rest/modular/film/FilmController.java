package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.stylefeng.guns.api.film.FilmAsyncServiceApi;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVo;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/film/")
public class FilmController {
    private static final String IMG_PRE = "http://img.liuencier.cn/";

    @Reference(interfaceClass = FilmServiceApi.class)
    private FilmServiceApi filmServiceApi;

    @Reference(interfaceClass = FilmAsyncServiceApi.class, async = true)
    private FilmAsyncServiceApi filmAsyncServiceApi;

    @GetMapping("getIndex")
    public ResponseVO<FilmIndexVo> getIndex() {
        FilmIndexVo filmIndexVo = new FilmIndexVo();
        filmIndexVo.setBanners(filmServiceApi.getBanners());
        filmIndexVo.setHotFilms(filmServiceApi.getHotFilms(true, 8, 1, 1, 99, 99, 99));
        filmIndexVo.setSoonFilms(filmServiceApi.getSoonFilms(true, 8, 1, 1, 99, 99, 99));
        filmIndexVo.setBoxRanking(filmServiceApi.getBoxRanking());
        filmIndexVo.setExpectRanking(filmServiceApi.getExpectRanking());
        filmIndexVo.setTop100(filmServiceApi.getTop());
        return ResponseVO.success(IMG_PRE, filmIndexVo);
    }

    @GetMapping(value = "getConditionList")
    public ResponseVO getConditionList(@RequestParam(name = "catId", required = false, defaultValue = "99") String catId,
                                       @RequestParam(name = "sourceId", required = false, defaultValue = "99") String sourceId,
                                       @RequestParam(name = "yearId", required = false, defaultValue = "99") String yearId) {
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
        return ResponseVO.success(filmConditionVO);
    }

    @RequestMapping(value = "getFilms", method = RequestMethod.GET)
    public ResponseVO getFilms(FilmRequestVO filmRequestVO) {
        String imgPre = "http://img.meetingshop.cn/";
        // 根据 showType 判断影片查询类型
        // 根据 sortId 排序
        // 添加各种条件查询
        // 判断当前是第几页
        FilmVO filmVO = null;
        switch (filmRequestVO.getShowType()) {
            case 1:
                filmVO = filmServiceApi.getHotFilms(false,
                        filmRequestVO.getPageSize(),
                        filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),
                        filmRequestVO.getSourceId(),
                        filmRequestVO.getYearId(),
                        filmRequestVO.getCatId());
                break;
            case 2:
                filmVO = filmServiceApi.getSoonFilms(false,
                        filmRequestVO.getPageSize(),
                        filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),
                        filmRequestVO.getSourceId(),
                        filmRequestVO.getYearId(),
                        filmRequestVO.getCatId());
                break;
            case 3:
                filmVO = filmServiceApi.getClassicFilms(
                        filmRequestVO.getPageSize(),
                        filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),
                        filmRequestVO.getSourceId(),
                        filmRequestVO.getYearId(),
                        filmRequestVO.getCatId());
                break;
            default:
                filmVO = filmServiceApi.getHotFilms(false,
                        filmRequestVO.getPageSize(),
                        filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),
                        filmRequestVO.getSourceId(),
                        filmRequestVO.getYearId(),
                        filmRequestVO.getCatId());
                break;
        }

        return ResponseVO.success(filmVO.getNowPage(), filmVO.getTotalPages(), imgPre, filmVO.getFilmInfo());
    }

    @RequestMapping(value = "films/{searchParam}", method = RequestMethod.GET)
    public ResponseVO films(@PathVariable("searchParam") String searchParam, int searchType) throws ExecutionException, InterruptedException {
        // 根据searchType,判断查询条件
        FilmDetailVO filmDetailVO = filmServiceApi.getFilmDetail(searchType, searchParam);
        if (filmDetailVO == null || filmDetailVO.getFilmId() == null) {
            return ResponseVO.serviceFail("没有可查询的影片");
        }
        String filmId = filmDetailVO.getFilmId();

        // 不同的查询类型，传入的条件会略有不同
        // 查询影片的详细信息 -> Dubbo 的异步调用
        // 获取影片描述信息
        filmAsyncServiceApi.getFilmDesc(filmId);
        Future<FilmDescVO> filmDescVOFuture = RpcContext.getContext().getFuture();
        // 获取图片信息
        filmAsyncServiceApi.getImgs(filmId);
        Future<ImgVO> imgVOFuture = RpcContext.getContext().getFuture();
        // 获取导演信息
        filmAsyncServiceApi.getDectInfo(filmId);
        Future<ActorVO> directorFuture = RpcContext.getContext().getFuture();
        // 获取演员信息
        filmAsyncServiceApi.getActors(filmId);
        Future<List<ActorVO>> actorVOFuture = RpcContext.getContext().getFuture();

        InfoRequestVO infoRequestVO = new InfoRequestVO();
        // 组织Actor属性
        ActorRequestVO actorRequestVO = new ActorRequestVO();
        actorRequestVO.setActors(actorVOFuture.get());
        actorRequestVO.setDirector(directorFuture.get());
        // 组织Info对象
        infoRequestVO.setActors(actorRequestVO);
        infoRequestVO.setBiography(filmDescVOFuture.get().getBiography());
        infoRequestVO.setFilmId(filmId);
        infoRequestVO.setImgVO(imgVOFuture.get());
        // 组织成返回值
        filmDetailVO.setInfo04(infoRequestVO);

        return ResponseVO.success("http://img.meetingshop.cn/", filmDetailVO);
    }
}
