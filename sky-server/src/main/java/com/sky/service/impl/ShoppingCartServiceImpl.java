package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        // 分支1：添加的是菜品（dishId有值）
        if (dishId != null) {
            // 查购物车：该用户是否已经加过这个菜品
            List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
            if (list != null && !list.isEmpty()) {
                // 已有 → 数量+1，更新即可（不会多占一行）
                ShoppingCart existing = list.get(0);
                existing.setNumber(existing.getNumber() + 1);
                shoppingCartMapper.updateNumberById(existing);
            } else {
                // 没有 → 查菜品信息补全字段，数量=1，插入新记录
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.insert(shoppingCart);
            }
        // 分支2：添加的是套餐（setmealId有值），逻辑同上
        } else if (setmealId != null) {
            List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
            if (list != null && !list.isEmpty()) {
                ShoppingCart existing = list.get(0);
                existing.setNumber(existing.getNumber() + 1);
                shoppingCartMapper.updateNumberById(existing);
            } else {
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.insert(shoppingCart);
            }
        }
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        if (dishId != null) {
            List<ShoppingCart> list = shoppingCartMapper.list(
                    ShoppingCart.builder().userId(userId).dishId(dishId).build());
            if (list != null && !list.isEmpty()) {
                ShoppingCart existing = list.get(0);
                if (existing.getNumber() > 1) {
                    existing.setNumber(existing.getNumber() - 1);
                    shoppingCartMapper.updateNumberById(existing);
                } else {
                    shoppingCartMapper.deleteById(existing.getId());
                }
            }
        } else if (setmealId != null) {
            List<ShoppingCart> list = shoppingCartMapper.list(
                    ShoppingCart.builder().userId(userId).setmealId(setmealId).build());
            if (list != null && !list.isEmpty()) {
                ShoppingCart existing = list.get(0);
                if (existing.getNumber() > 1) {
                    existing.setNumber(existing.getNumber() - 1);
                    shoppingCartMapper.updateNumberById(existing);
                } else {
                    shoppingCartMapper.deleteById(existing.getId());
                }
            }
        }
    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart condition = ShoppingCart.builder().userId(userId).build();
        return shoppingCartMapper.list(condition);
    }

    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }
}
