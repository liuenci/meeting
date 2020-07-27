package com.stylefeng.guns.rest.modular.cinema.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaSeriveAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.MoocAreaDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocBrandDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocActorT;
import com.stylefeng.guns.rest.common.persistence.model.MoocAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocCinemaT;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: meeting
 * @description: 默认的影院服务实现类
 * @author: liuenci
 * @create: 2020-07-27 19:12
 **/
public class DefaultCinemaServiceImpl implements CinemaSeriveAPI {
    @Autowired
    private MoocCinemaTMapper moocCinemaTMapper;
    @Autowired
    private MoocAreaDictTMapper moocAreaDictTMapper;
    @Autowired
    private MoocBrandDictTMapper moocBrandDictTMapper;
//    @Autowired
//    private MoocHallDictTMapper moocHallDictTMapper;
//    @Autowired
//    private MoocHallFilmInfoTMapper moocHallFilmInfoTMapper;
//    @Autowired
//    private MoocFieldTMapper moocFieldTMapper;


    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {
        List<CinemaVO> cinemaVOS = new ArrayList<>();

        Page<MoocCinemaT> page = new Page<>(cinemaQueryVO.getNowPage(), cinemaQueryVO.getPageSize());
        EntityWrapper<MoocCinemaT> entityWrapper = new EntityWrapper<>();
        if (cinemaQueryVO.getBrandId() != 99) {
            entityWrapper.eq("brand_id", cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getDistrictId() != 99) {
            entityWrapper.eq("area_id", cinemaQueryVO.getDistrictId());
        }
        if (cinemaQueryVO.getHallType() != 99) {
            // %#3#%
            entityWrapper.eq("hall_ids", "%#" + cinemaQueryVO.getHallType() + "#%");
        }
        List<MoocCinemaT> moocCinemaTS = moocCinemaTMapper.selectPage(page, entityWrapper);

        // 这里可以写成通过SQL查询的方式，只查询出想要的字段 TODO 就可以不用转化了
        for(MoocCinemaT moocCinemaT : moocCinemaTS){
            CinemaVO cinemaVO = new CinemaVO();

            cinemaVO.setUuid(moocCinemaT.getUuid()+"");
            cinemaVO.setMinimumPrice(moocCinemaT.getMinimumPrice()+"");
            cinemaVO.setCinemaName(moocCinemaT.getCinemaName());
            cinemaVO.setAddress(moocCinemaT.getCinemaAddress());

            cinemaVOS.add(cinemaVO);
        }
        long count = moocCinemaTMapper.selectCount(entityWrapper);
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemaVOS);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(count);
        return result;
    }

    @Override
    public List<BrandVO> getBrands(int brandId) {
        boolean flag = false;
        List<BrandVO> brandVOS = new ArrayList<>();
        MoocBrandDictT moocBrandDictT = moocBrandDictTMapper.selectById(brandId);
        if (brandId == 99 || moocBrandDictT == null || moocBrandDictT.getUuid() == null) {
            flag = true;
        }
        List<MoocBrandDictT> moocBrandDictTS = moocBrandDictTMapper.selectList(null);
        for (MoocBrandDictT brand : moocBrandDictTS) {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandName(brand.getShowName());
            brandVO.setBrandId(brand.getUuid()+"");
            // 如果flag为true，则需要99，如为false，则匹配上的内容为active
            if(flag){
                if(brand.getUuid() == 99){
                    brandVO.setIsActive(true);
                }
            }else{
                if(brand.getUuid() == brandId){
                    brandVO.setIsActive(true);
                }
            }

            brandVOS.add(brandVO);
        }

        return brandVOS;
    }

    @Override
    public List<AreaVO> getAreas(int areaId) {
        boolean flag = false;
        List<AreaVO> areaVOS = new ArrayList<>();
        // 判断brandId是否存在
        MoocAreaDictT moocAreaDictT = moocAreaDictTMapper.selectById(areaId);
        // 判断brandId 是否等于 99
        if(areaId == 99 || moocAreaDictT==null || moocAreaDictT.getUuid() == null){
            flag = true;
        }
        // 查询所有列表
        List<MoocAreaDictT> moocAreaDictTS = moocAreaDictTMapper.selectList(null);
        // 判断flag如果为true，则将99置为isActive
        for(MoocAreaDictT area : moocAreaDictTS){
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaName(area.getShowName());
            areaVO.setAreaId(area.getUuid()+"");
            // 如果flag为true，则需要99，如为false，则匹配上的内容为active
            if(flag){
                if(area.getUuid() == 99){
                    areaVO.setActive(true);
                }
            }else{
                if(area.getUuid() == areaId){
                    areaVO.setActive(true);
                }
            }

            areaVOS.add(areaVO);
        }

        return areaVOS;
    }

    @Override
    public List<HallTypeVO> getHallTypes(int hallType) {
        return null;
    }

    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        return null;
    }

    @Override
    public List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId) {
        return null;
    }

    @Override
    public HallInfoVO getFilmFieldInfo(int fieldId) {
        return null;
    }

    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        return null;
    }

    @Override
    public OrderQueryVO getOrderNeeds(int fieldId) {
        return null;
    }
}
