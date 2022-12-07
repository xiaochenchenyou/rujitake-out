package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wjcollege.ruiji.common.BaseContext;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.AddressBook;
import com.wjcollege.ruiji.entity.Dish;
import com.wjcollege.ruiji.entity.ShoppingCart;
import com.wjcollege.ruiji.entity.User;
import com.wjcollege.ruiji.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author chenWei
 * @date 2022/12/4 17:38
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    /**
     * 根据id返回地址信息
     * @return
     */
    @GetMapping("/list")
    public R returnList(HttpSession httpSession){
        Long Userid = (Long) httpSession.getAttribute("user");
        Long id = BaseContext.getId();
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId,Userid);

        //查询所有的地址信息
        List<AddressBook> list = addressBookService.list(addressBookLambdaQueryWrapper);
        return R.success(list);

    }


    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R setDefaultAddress(@RequestBody AddressBook addressBook,HttpSession httpSession){
        Long userId = (Long) httpSession.getAttribute("user");
        //首先设置原有的default
        LambdaUpdateWrapper<AddressBook> addressBookLambdaQueryWrapper1 = new LambdaUpdateWrapper<>();

        addressBookLambdaQueryWrapper1.eq(AddressBook::getUserId,userId);
        addressBookLambdaQueryWrapper1.set(AddressBook::getIsDefault,0);

        addressBookService.update(addressBookLambdaQueryWrapper1);

        LambdaUpdateWrapper<AddressBook> addressBookLambdaQueryWrapper2 = new LambdaUpdateWrapper<>();
        //设置现在的默认地址
        addressBookLambdaQueryWrapper2.eq(AddressBook::getId,addressBook.getId());
        addressBookLambdaQueryWrapper2.set(AddressBook::getIsDefault,1);
        addressBookService.update(addressBookLambdaQueryWrapper2);



        return R.success("修改成功");
    }


    /**
     * 新建收货地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R createAddress(@RequestBody AddressBook addressBook,HttpSession httpSession){
        Long id = BaseContext.getId();
        //当前登录的id
        Long userId = (Long) httpSession.getAttribute("user");
        addressBook.setCreateUser(addressBook.getUserId());
        addressBook.setUpdateUser(userId);
        addressBook.setUserId(userId);

        addressBookService.save(addressBook);

        return R.success("添加成功");

    }


    /**
     * 支付时显示默认地址
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> defaultAddress(HttpSession httpSession){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault,1);
        Long id = BaseContext.getId();
        Long userId = (Long)httpSession.getAttribute("user");
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId,userId);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);

        return R.success(addressBook);


    }


}
