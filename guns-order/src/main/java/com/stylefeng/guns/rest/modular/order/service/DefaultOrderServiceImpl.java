package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.util.UUIDUitls;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.utils.FTPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: meeting
 * @description: 默认订单实现
 * @author: liuenci
 * @create: 2020-08-03 18:04
 **/
@Slf4j
@Component
@Service(interfaceClass = OrderServiceAPI.class, group = "default")
public class DefaultOrderServiceImpl implements OrderServiceAPI {
    @Autowired
    private MoocOrderTMapper moocOrderTMapper;
    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;
    @Autowired
    private FTPUtils ftpUtils;

    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
        // 根据FieldId找到对应的座位位置图
        String seatPath = moocOrderTMapper.getSeatsByFieldId(fieldId);
        // 读取位置图，判断seats是否为真
        String fileStrByAddress = ftpUtils.getFileStrByAddress(seatPath);
        JSONObject jsonObject = JSONObject.parseObject(fileStrByAddress);
        String ids = jsonObject.get("ids").toString();
        // 这里可以改成 Set 来去重
        String[] seatArrs = seats.split(",");
        String[] idArrs = ids.split(",");
        int isTrue = 0;
        for (String id : idArrs) {
            for (String seat : seatArrs) {
                if (seat.equalsIgnoreCase(id)) {
                    isTrue++;
                }
            }
        }

        // 如果匹配上的数量与已售座位数一致，则表示全部匹配上了
        if (seatArrs.length == isTrue) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id", fieldId);
        List<MoocOrderT> list = moocOrderTMapper.selectList(entityWrapper);
        String[] seatArrs = seats.split(",");
        // 有任何一个编号匹配上，则直接返回失败
        for (MoocOrderT moocOrderT : list) {
            String[] ids = moocOrderT.getSeatsIds().split(",");
            for (String id : ids) {
                for (String seat : seatArrs) {
                    if (id.equalsIgnoreCase(seat)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        String uuid = UUIDUitls.genUuid();
        // 影片信息
        FilmInfoVO filmInfoVO = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
        int filmId = Integer.parseInt(filmInfoVO.getFilmId());

        // 获取影院信息
        OrderQueryVO orderQueryVO = cinemaServiceAPI.getOrderNeeds(fieldId);
        int cinemaId = Integer.parseInt(orderQueryVO.getCinemaId());
        double filmPrice = Double.parseDouble(orderQueryVO.getCinemaId());

        // 求订单总金额
        int solds = soldSeats.split(",").length;
        double totalPrice = getTotalPrice(solds, filmPrice);
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(uuid);
        moocOrderT.setSeatsIds(soldSeats);
        moocOrderT.setSeatsName(seatsName);
        moocOrderT.setOrderUser(userId);
        moocOrderT.setOrderPrice(totalPrice);
        moocOrderT.setFilmPrice(filmPrice);

        Integer i = moocOrderTMapper.insert(moocOrderT);
        if (i > 0) {
            // 返回查询结果
            OrderVO orderVO = moocOrderTMapper.getOrderInfoById(uuid);
            if (orderVO == null || orderVO.getOrderId() == null) {
                log.error("订单信息查询失败，订单编号 {}", uuid);
                return null;
            } else {
                return orderVO;
            }
        } else {
            // 插入出错
            log.error("订单插入失败");
            return null;
        }
    }

    private static double getTotalPrice(int solds, double filmPrice) {
        BigDecimal soldDeci = new BigDecimal(solds);
        BigDecimal filmPriceDeci = new BigDecimal(filmPrice);
        BigDecimal result = soldDeci.multiply(filmPriceDeci).setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    @Override
    public Page<OrderVO> getOrderByUserId(Integer userId, Page<OrderVO> page) {
        Page<OrderVO> result = new Page<>();
        if (userId == null) {
            log.error("订单查询业务失败，用户编号未传入");
            return null;
        } else {
            List<OrderVO> orders = moocOrderTMapper.getOrdersByUserId(userId, page);
            if (CollectionUtils.isEmpty(orders)) {
                result.setTotal(0);
                result.setRecords(new ArrayList<>());
                return result;
            } else {
                // 获取订单总数
                EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("order_user", userId);
                Integer count = moocOrderTMapper.selectCount(entityWrapper);
                result.setTotal(count);
                result.setRecords(orders);
                return result;
            }
        }
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if (fieldId == null) {
            log.error("查询已售座位错误，未传入任何场次编号");
            return null;
        } else {
            String solds = moocOrderTMapper.getSoldSeatsByFieldId(fieldId);
            return solds;
        }
    }

    @Override
    public OrderVO getOrderInfoById(String orderId) {
        OrderVO orderVO = moocOrderTMapper.getOrderInfoById(orderId);
        return orderVO;
    }

    @Override
    public boolean paySuccess(String orderId) {
        return false;
    }

    @Override
    public boolean payFail(String orderId) {
        return false;
    }
}
