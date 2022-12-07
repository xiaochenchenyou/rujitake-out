package com.wjcollege.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjcollege.ruiji.entity.Category;
import com.wjcollege.ruiji.mapper.CategoryMapper;
import com.wjcollege.ruiji.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * @author chenWei
 * @date 2022/11/29 15:50
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
