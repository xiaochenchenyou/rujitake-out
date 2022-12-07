package com.wjcollege.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjcollege.ruiji.entity.Category;
import com.wjcollege.ruiji.entity.Dish;
import com.wjcollege.ruiji.mapper.CategoryMapper;
import com.wjcollege.ruiji.mapper.DishMapper;
import com.wjcollege.ruiji.service.CategoryService;
import com.wjcollege.ruiji.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author chenWei
 * @date 2022/11/29 23:49
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
}


