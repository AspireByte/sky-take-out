package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    void paySuccess(String outTradeNo);

    /**
     * 用户催单
     * @param id
     */
    void reminder(Long id);

    /**
     * 条件分页查询订单
     * @param dto
     */
    PageResult conditionSearch(OrdersPageQueryDTO dto);

    /**
     * 查询订单详情
     * @param orderId
     */
    OrderVO getOrderDetail(Long orderId);

    /**
     * 派送订单
     * @param orderId
     */
    void delivery(Long orderId);

    /**
     * 完成订单
     * @param orderId
     */
    void complete(Long orderId);

    /**
     * 取消订单
     * @param dto
     */
    void cancel(OrdersCancelDTO dto);

    /**
     * 接单
     * @param dto
     */
    void confirm(OrdersConfirmDTO dto);

    /**
     * 拒单
     * @param dto
     */
    void rejection(OrdersRejectionDTO dto);

    /**
     * 查询各状态订单数量
     */
    OrderStatisticsVO getStatistics();
}
