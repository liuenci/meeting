package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaConditionResponseVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: meeting
 * @description: 影院控制器
 * @author: liuenci
 * @create: 2020-04-06 16:20
 **/
@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    @Reference(interfaceClass = CinemaServiceAPI.class, connections = 10, cache = "lru", check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    @RequestMapping(value = "getCinemas")
    public ResponseVO getCinemas(CinemaQueryVO cinemaQueryVO) {
        try{
            // 按照五个条件进行筛选
            Page<CinemaVO> cinemas = cinemaServiceAPI.getCinemas(cinemaQueryVO);
            // 判断是否有满足条件的影院
            if(cinemas.getRecords() == null || cinemas.getRecords().size()==0){
                return ResponseVO.success("没有影院可查");
            }else{
                return ResponseVO.success(cinemas.getCurrent(),(int)cinemas.getPages(),"",cinemas.getRecords());
            }
        }catch (Exception e){
            // 如果出现异常，应该如何处理
            log.error("获取影院列表异常",e);
            return ResponseVO.serviceFail("查询影院列表失败");
        }
    }

    @RequestMapping(value = "getCondition")
    public ResponseVO getCondition(CinemaQueryVO cinemaQueryVO) {
        try{
            // 获取三个集合，然后封装成一个对象返回即可
            List<BrandVO> brands = cinemaServiceAPI.getBrands(cinemaQueryVO.getBrandId());
            List<AreaVO> areas = cinemaServiceAPI.getAreas(cinemaQueryVO.getDistrictId());
            List<HallTypeVO> hallTypes = cinemaServiceAPI.getHallTypes(cinemaQueryVO.getHallType());

            CinemaConditionResponseVO cinemaConditionResponseVO = new CinemaConditionResponseVO();
            cinemaConditionResponseVO.setAreaList(areas);
            cinemaConditionResponseVO.setBrandList(brands);
            cinemaConditionResponseVO.setHalltypeList(hallTypes);

            return ResponseVO.success(cinemaConditionResponseVO);
        }catch (Exception e) {
            log.error("获取条件列表失败", e);
            return ResponseVO.serviceFail("获取影院查询条件失败");
        }
    }

    @RequestMapping(value = "getFields")
    public ResponseVO getFields(Integer cinemaId) {
        return null;
    }

    @RequestMapping(value = "getFieldInfo", method = RequestMethod.POST)
    public ResponseVO getFieldInfo(Integer cinemaId, Integer fieldId) {
        return null;
    }
}
