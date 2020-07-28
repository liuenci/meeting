package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.AreaVO;
import com.stylefeng.guns.api.cinema.vo.BrandVO;
import com.stylefeng.guns.api.cinema.vo.HallTypeVO;
import lombok.Data;

import java.util.List;

/**
 * @program: meeting
 * @description: 获取影院的返回VO
 * @author: liuenci
 * @create: 2020-07-28 09:16
 **/
@Data
public class CinemaConditionResponseVO {
    private List<BrandVO> brandList;
    private List<AreaVO> areaList;
    private List<HallTypeVO> halltypeList;
}
