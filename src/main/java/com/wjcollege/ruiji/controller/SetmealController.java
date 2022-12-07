package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.*;
import com.wjcollege.ruiji.service.CategoryService;
import com.wjcollege.ruiji.service.SetmalService;
import com.wjcollege.ruiji.service.SetmealDishService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenWei
 * @date 2022/12/1 23:30
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmalService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    CategoryService categoryService;

    @Setter
    SetmealDto setmealDto;

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R insertSetmeal(@RequestBody SetmealDto setmealDto){

        //插入套餐的信息
        setmealService.save(setmealDto);


        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //套餐的每个菜品
        for (SetmealDish setmealDish:setmealDishes) {
            //插入套餐的id
            setmealDish.setSetmealId(setmealDto.getId());

            setmealDishService.save(setmealDish);
        }


        return R.success("添加成功");
    }

    /**
     * 套餐分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R selectPage(int page,int pageSize,String name){


        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(Strings.isNotEmpty(name),Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);


        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        //最终返回结果对象
        Page<SetmealDto> setmealDtoPage = new Page<>();

        setmealService.page(setmealPage, setmealLambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> collect = records.stream().map((items) -> {
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = items.getCategoryId();

            //对象拷贝
            BeanUtils.copyProperties(items, setmealDto);

            //根据id来查套餐分类
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }

            return setmealDto;


        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(collect);

        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping()
    public R deleteSetmal(String[] ids){

        //套餐的id
        List<String> setmealId = new ArrayList<>();


        //删除套餐
        for (String id:ids) {
            setmealId.add(id);
        }
        setmealService.removeByIds(setmealId);


        //删除套餐里面的菜品
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,setmealId);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);


        return R.success("删除成功");
    }


    /**
     * 套餐的停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R startSetmal(String[] ids){
        List<String> setmealId = new ArrayList<>();
        for (String id:ids) {
            setmealId.add(id);
        }

        //先查出所有的套餐列表
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,setmealId);

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);


        //修改套餐的status
        ArrayList<Setmeal> setmeals = new ArrayList<>();
        for (Setmeal setmeal:list) {
            setmeal.setStatus(0);
            setmeals.add(setmeal);

        }
        setmealService.updateBatchById(setmeals);


        return R.success("修改成功");
    }


    /**
     * 套餐的起售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R stopSetmal(String[] ids){
        List<String> setmealId = new ArrayList<>();
        for (String id:ids) {
            setmealId.add(id);
        }

        //先查出所有的套餐列表
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,setmealId);

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);


        //修改套餐的status
        ArrayList<Setmeal> setmeals = new ArrayList<>();
        for (Setmeal setmeal:list) {
            setmeal.setStatus(1);
            setmeals.add(setmeal);

        }
        setmealService.updateBatchById(setmeals);


        return R.success("修改成功");
    }

    /**
     * 修改套餐回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R ModifySetmeal(@PathVariable Long id){

        SetmealDto setmealDto = new SetmealDto();

        //查出套餐内容
        Setmeal byId = setmealService.getById(id);

        //查出套餐的分类
        Category category = categoryService.getById(byId.getCategoryId());
        Long categoryId = category.getId();
        setmealDto.setCategoryId(categoryId);

        //查出套餐的具体菜
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);

        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishLambdaQueryWrapper);

        //回显套餐的菜品
        setmealDto.setSetmealDishes(setmealDishList);
        //回显套餐的图片
        setmealDto.setImage(byId.getImage());
        //回显套餐的描述
        setmealDto.setDescription(byId.getDescription());
//        //回显套餐的名字
        setmealDto.setName(byId.getName());
        //回显套餐的价格
        setmealDto.setPrice(byId.getPrice());
        //设置回显的id
        setmealDto.setId(id);



        return R.success(setmealDto);


    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R modifySetmeal(@RequestBody SetmealDto setmealDto){

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //修改套餐的信息
        setmealService.updateById(setmealDto);

        //删除套餐中所有关联此id的配菜
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //添加套餐的每个菜品
        for (SetmealDish setmealDish:setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishService.save(setmealDish);
        }

        return R.success("添加成功");
    }

    /**
     * 显示客户端套餐
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> returnTaoCan(String categoryId,String status){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(categoryId!=null,Setmeal::getCategoryId,categoryId);
        setmealLambdaQueryWrapper.eq(status!=null,Setmeal::getStatus,status);

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);
    }





}
