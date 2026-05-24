package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据菜品id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal_dish where dish_id = #{dishId}")
    Integer countByDishId(Long id);

    /**
     * 批量查询被套餐关联的菜品id
     * @param dishIds
     * @return
     */
    @Select("<script>select distinct dish_id from setmeal_dish where dish_id in <foreach collection='dishIds' item='id' open='(' close=')' separator=','>#{id}</foreach></script>")
    List<Long> getSetmealDishIds(@Param("dishIds") List<Long> dishIds);

}
