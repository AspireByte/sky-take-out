package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sky.entity.DishFlavor;

@Mapper
public interface DishFlavorMapper {
    void insertBatch(List<DishFlavor> flavors);

    void deleteByDishId(Long dishId);

    void deleteByDishIds(@Param("dishIds") List<Long> dishIds);

    List<DishFlavor> getByDishId(Long dishId);
}
