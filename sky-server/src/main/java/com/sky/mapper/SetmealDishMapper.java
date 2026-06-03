package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 批量保存套餐和菜品的关联关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id删除套餐和菜品的关联关系
     * @param setmealId
     */
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐ids批量删除套餐和菜品的关联关系
     * @param setmealIds
     */
    void deleteBySetmealIds(@Param("setmealIds") List<Long> setmealIds);

    /**
     * 根据套餐id查询套餐和菜品的关联关系
     * @param setmealId
     * @return
     */
    List<SetmealDish> getBySetmealId(Long setmealId);
}
