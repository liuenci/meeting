package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVo;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return null;
    }
}
