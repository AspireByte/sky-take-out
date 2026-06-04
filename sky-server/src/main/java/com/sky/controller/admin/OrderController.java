package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "管理端-订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 条件分页查询订单
     * @param dto
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO dto) {
        log.info("管理端订单搜索：{}", dto);
        PageResult pageResult = orderService.conditionSearch(dto);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     * @param orderId
     */
    @GetMapping("/details/{orderId}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable Long orderId) {
        log.info("管理端查询订单详情：{}", orderId);
        OrderVO orderVO = orderService.getOrderDetail(orderId);
        return Result.success(orderVO);
    }

    /**
     * 接单
     * @param dto
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersConfirmDTO dto) {
        log.info("管理端接单：{}", dto);
        orderService.confirm(dto);
        return Result.success();
    }

    /**
     * 拒单
     * @param dto
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO dto) {
        log.info("管理端拒单：{}", dto);
        orderService.rejection(dto);
        return Result.success();
    }

    /**
     * 取消订单
     * @param dto
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO dto) {
        log.info("管理端取消订单：{}", dto);
        orderService.cancel(dto);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id) {
        log.info("管理端派送订单：{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id) {
        log.info("管理端完成订单：{}", id);
        orderService.complete(id);
        return Result.success();
    }

    /**
     * 各状态订单数量统计
     */
    @GetMapping("/statistics")
    @ApiOperation("订单状态统计")
    public Result<OrderStatisticsVO> statistics() {
        log.info("管理端订单状态统计");
        OrderStatisticsVO vo = orderService.getStatistics();
        return Result.success(vo);
    }
}