package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根据订单号和用户id查询订单
     * @param number
     * @param userId
     */
    @Select("select * from orders where number = #{number} and user_id = #{userId}")
    Orders getByNumberAndUserId(String number, Long userId);

    /**
     * 动态更新订单字段（仅更新非空字段）
     * @param order
     */
    void update(Orders order);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 条件分页查询订单（含订单菜品字符串）
     * @param dto
     */
    List<OrderVO> pageQuery(OrdersPageQueryDTO dto);

    /**
     * 根据id查询订单VO（含订单菜品字符串）
     * @param id
     */
    OrderVO getOrderVOById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    Integer countByStatus(Integer status);

    /**
     * 根据动态条件统计营业额
     * @param map
     */
    Double sumByMap(Map map);

    /**
     * 根据动态条件统计订单数量
     * @param map
     */
    Integer countByMap(Map map);
}
