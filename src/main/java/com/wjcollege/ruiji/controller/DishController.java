package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.Category;
import com.wjcollege.ruiji.entity.Dish;
import com.wjcollege.ruiji.entity.DishDto;
import com.wjcollege.ruiji.entity.DishFlavor;
import com.wjcollege.ruiji.service.CategoryService;
import com.wjcollege.ruiji.service.DishFlavorService;
import com.wjcollege.ruiji.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenWei
 * @date 2022/11/30 21:57
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    Dish dish;

    /**
     * 菜品分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R selectPage(int page, int pageSize, String name) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(name), Dish::getName, name);

        Page<Dish> objectPage = new Page<>(page, pageSize);

        dishService.page(objectPage, lambdaQueryWrapper);

        return R.success(objectPage);
    }

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R insertDish(@RequestBody DishDto dishDto) {


        //新增菜谱
        dishService.save(dishDto);
        //添加的口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //遍历数据，添加数据库
        for (DishFlavor dishFlavors : flavors) {
            dishFlavors.setDishId(dishDto.getId());
            dishFlavorService.save(dishFlavors);

        }

        return R.success("添加成功");
    }

    /**
     * 回显页面
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> returnPage(@PathVariable String id) {
        Dish dish = dishService.getById(id);


        LambdaQueryWrapper<DishFlavor> DishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        DishFlavorLambdaQueryWrapper.like(DishFlavor::getDishId, id);

        List<DishFlavor> flavors = dishFlavorService.list(DishFlavorLambdaQueryWrapper);

        //将dish中查询的数据封装到dishDto中
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavors);


        return R.success(dishDto);
    }


    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R saveDish(@RequestBody DishDto dishDto) {

        Long id = dishDto.getId();

        //修改菜谱
        dishService.updateById(dishDto);

        //修改口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.like(DishFlavor::getDishId, id);


        //先删除后添加
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        //遍历数据，添加数据库
        for (DishFlavor dishFlavors : flavors) {
            dishFlavors.setDishId(id);
            dishFlavorService.save(dishFlavors);

        }

        return R.success("修改成功");
    }

    /**
     * 菜品的停售
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R discontinued(String[] ids) {

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            //根据id来更改数据库的售卖状况
            LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(Dish::getId, id);
            dish.setStatus(0);
            dishService.update(dish, dishLambdaQueryWrapper);
        }


//        //根据id来更改数据库的售卖状况
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getId,ids);
        dish.setStatus(0);


        return R.success("更改成功");
    }


    /**
     * 菜品的起售
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R start(String[] ids) {

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(Dish::getId, id);
            dish.setStatus(1);
            dishService.update(dish, dishLambdaQueryWrapper);
        }

        //根据id来更改数据库的售卖状况
//        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishLambdaQueryWrapper.eq(Dish::getId,collection);
//        dish.setStatus(1);
//        dishService.update(dish,dishLambdaQueryWrapper);
//        dishService.updateBatchById(collection);

        return R.success("更改成功");
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R deleteDish(String[] ids) {
        Collection collection = new ArrayList();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            collection.add(id);
        }

        dishService.removeByIds(collection);


        return R.success("删除成功");

    }

    /**
     * 添加套餐内部的套餐菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> returnDish(Dish dish,String name){

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());

        //查询状态为1的菜品
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        //根据id来查每个菜品名
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);


        List<DishDto> dishDtoCollect = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            //分类
            Long categoryId = item.getCategoryId();
            if(categoryId!=null){
                Category category = categoryService.getById(categoryId);
                dishDto.setCategoryName(category.getName());
            }

            //获取每个菜品的id,根据id查出口味
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;


        }).collect(Collectors.toList());

        return  R.success(dishDtoCollect);
    }


}
