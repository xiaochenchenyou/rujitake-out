package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.Category;
import com.wjcollege.ruiji.entity.Employee;
import com.wjcollege.ruiji.service.CategoryService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chenWei
 * @date 2022/11/29 15:39
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> selectAll(int page, int pageSize, String name) {
        LambdaQueryWrapper<Category> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Category> lambdaQueryWrapper = objectLambdaQueryWrapper.like(Strings.isNotEmpty(name), Category::getName, name);

        Page<Category> pages = new Page<>(page, pageSize);
        categoryService.page(pages, lambdaQueryWrapper);

        return R.success(pages);
    }


    @PostMapping
    public R<String> insertCategory(@RequestBody Category category, HttpServletRequest httpServletRequest) {



        categoryService.save(category);

        return R.success("新增成功");

    }


    @PutMapping
    public R<String> updateCategory(@RequestBody Category category, HttpServletRequest httpServletRequest) {
        Employee attribute = (Employee) httpServletRequest.getSession().getAttribute("Employee");

        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(attribute.getId());
        categoryService.updateById(category);


        return R.success("修改成功");

    }

    @DeleteMapping
    public R<String> deleteCategory(String ids) {
        categoryService.removeById(ids);

        return R.success("删除成功");
    }

    /**
     * 返回所有的下拉选项
     * @param category
     * @return
     */

    @GetMapping("/list")
    public R<List<Category>> selectList(Category category){
        LambdaQueryWrapper<Category> LambdaQueryWrapper = new LambdaQueryWrapper<>();

        LambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());



        List<Category> list = categoryService.list(LambdaQueryWrapper);
        return R.success(list);

    }


}
