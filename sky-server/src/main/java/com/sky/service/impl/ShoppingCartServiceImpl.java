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
        shoppingCart.setUserId(BaseContext.getCurrentId());

        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        if (dishId != null) {
            List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
            if (!list.isEmpty()) {
                ShoppingCart existing = list.get(0);
                existing.setNumber(existing.getNumber() + 1);
                shoppingCartMapper.updateNumberById(existing);
            } else {
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.insert(shoppingCart);
            }
        } else if (setmealId != null) {
            List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
            if (!list.isEmpty()) {
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
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        if (dishId != null) {
            List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
            if (!list.isEmpty()) {
                ShoppingCart existing = list.get(0);
                if (existing.getNumber() > 1) {
                    existing.setNumber(existing.getNumber() - 1);
                    shoppingCartMapper.updateNumberById(existing);
                } else {
                    shoppingCartMapper.deleteById(existing.getId());
                }
            }
        } else if (setmealId != null) {
            List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
            if (!list.isEmpty()) {
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