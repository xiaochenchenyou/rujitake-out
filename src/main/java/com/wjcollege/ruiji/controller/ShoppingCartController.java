package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wjcollege.ruiji.common.BaseContext;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.AddressBook;
import com.wjcollege.ruiji.entity.Dish;
import com.wjcollege.ruiji.entity.DishFlavor;
import com.wjcollege.ruiji.entity.ShoppingCart;
import com.wjcollege.ruiji.service.AddressBookService;
import com.wjcollege.ruiji.service.DishFlavorService;
import com.wjcollege.ruiji.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chenWei
 * @date 2022/12/4 21:04
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;


    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    AddressBookService addressBookService;



    @GetMapping("/list")
    public R<List<ShoppingCart>> shoppingCartList(HttpSession httpSession){
        Long userId = (Long) httpSession.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(list);
    }


    /**
     * 添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R insertShoppingCar(@RequestBody ShoppingCart shoppingCart, HttpSession httpSession){

        //查找当前添加的菜品数据库是否有，有则number加1

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();


        ShoppingCart newShoppingCart = new ShoppingCart();
        //不是套餐，加上口味
        if(shoppingCart.getDishId()!=null){
            //口味条件,菜品id查
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
            newShoppingCart = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        }else {
            //是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            newShoppingCart = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        }
        //代表数据库中有这个数据
        if(newShoppingCart!=null){
            //如果查找出来的话，number加一
            shoppingCart.setNumber(newShoppingCart.getNumber()+ 1);

            //更新数据

            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getId,newShoppingCart.getId());
            shoppingCartService.update(shoppingCart,shoppingCartLambdaQueryWrapper);
        //代表数据库中   没有   这个数据
        }else {
            shoppingCart.setCreateTime(LocalDateTime.now());
            Long userId = (Long) httpSession.getAttribute("user");
            shoppingCart.setUserId(userId);
            shoppingCartService.save(shoppingCart);

        }

        return R.success(shoppingCart);
    }

    /**
     * 删除购物车菜品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R deleteShoppingCar(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getId());

        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("删除成功");
    }

    /**
     * 根据当前id对购物车进行清空
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> deleteShoppingCar(){
        //根据当前用户的id来进行删除
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getId());

        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);


        return R.success("清空成功");
    }



}
