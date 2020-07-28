package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import lombok.Data;

import java.util.List;

/**
 * @program: meeting
 * @description: 播放场次返回VO
 * @author: liuenci
 * @create: 2020-07-28 09:18
 **/
@Data
public class CinemaFieldsResponseVO {
    private CinemaInfoVO cinemaInfo;
    private List<FilmInfoVO> filmList;
}
