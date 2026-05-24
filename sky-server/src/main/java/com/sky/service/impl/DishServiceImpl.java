package com.sky.service.impl;

import java.util.List;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void saveWithFlavor(DishDTO dishDTO) {

        //这里需要直接new出Dish对象，不能使用@Autowired注入的方式，因为@Autowired注入的对象是单例的，如果直接使用@Autowired注入的Dish对象，那么每次调用这个方法时，都会修改同一个Dish对象，导致数据混乱。
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(1);
        dishMapper.insert(dish);

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if(flavors != null && flavors.size() > 0){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
    PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
    Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
    return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 批量查询菜品
        List<Dish> dishes = dishMapper.getByIds(ids);
       

        // 校验存在性
        for (Long id : ids) {
            if (id==null) {
                throw new DeletionNotAllowedException("菜品不存在，无法删除");
            }
        }

        // 校验售卖状态
        for (Dish dish : dishes) {
            if (dish.getStatus() == 1) {
                throw new DeletionNotAllowedException("菜品正在售卖中，不能删除");
            }
        }

        // 校验套餐关联
        if (!setmealMapper.getSetmealDishIds(ids).isEmpty()) {
            throw new DeletionNotAllowedException("菜品正在被套餐使用，不能删除");
        }

        // 批量删除口味和菜品
        dishFlavorMapper.deleteByDishIds(ids);
        dishMapper.deleteByIds(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish= dishMapper.getById(id);
        if(dish == null){
            return null;
    }
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);


        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(flavors);

        return dishVO;
}

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishFlavorMapper.deleteByDishId(dish.getId());
        if (flavors != null && !flavors.isEmpty()) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }

    }


}
