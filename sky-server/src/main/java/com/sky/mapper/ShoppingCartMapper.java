package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询购物车
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where id = #{id}")
    ShoppingCart getById(Long id);

    void insert(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
