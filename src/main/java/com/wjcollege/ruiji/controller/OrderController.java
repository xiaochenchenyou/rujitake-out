package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjcollege.ruiji.common.BaseContext;
import com.wjcollege.ruiji.common.CustomException;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.*;
import com.wjcollege.ruiji.service.*;
import org.jcp.xml.dsig.internal.dom.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;

/**
 * @author chenWei
 * @date 2022/12/5 17:11
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @Transactional
    public R orderSubmit(@RequestBody Orders orders, HttpSession httpSession) throws CustomException {
        //获得当前用户id
        Long userId = (Long) httpSession.getAttribute("user");
//        Long userId = BaseContext.getId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }

        long orderId = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(addressBook.getConsignee());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        orderService.save(orders);


        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(wrapper);

        return R.success("下单成功");
    }

    /**
     * 根据用户id返回订单
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<Orders>> returnUserPage(int page,int pageSize,HttpSession httpSession){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Long userId = (Long) httpSession.getAttribute("user");
//        Long id = BaseContext.getId();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,userId);

         orderService.page(ordersPage,ordersLambdaQueryWrapper);


        return R.success(ordersPage);
    }


    @GetMapping("/page")
    public R returnOrder(int page,int pageSize,String number,String beginTime,String endTime){
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<Orders>();
        ordersLambdaQueryWrapper.like(number!=null, Orders::getUserName,number);
        ordersLambdaQueryWrapper.between(beginTime!=null,Orders::getOrderTime,beginTime,endTime);

        Page<Orders> ordersPage = new Page<>(page,pageSize);
        orderService.page(ordersPage,ordersLambdaQueryWrapper);

        ordersPage.getRecords();

        return R.success(ordersPage);


    }

    /**
     * @return
     */
    @PutMapping
    public R dingDanStatus(@RequestBody Map map){

//        case 1:
//        str =  '待付款'
//        break;
//        case 2:
//        str =  '正在派送'
//        break;
//        case 3:
//        str =  '已派送'
//        break;
//        case 4:
//        str =  '已完成'
//        break;
//        case 5:
//        str =  '已取消'
        Orders orders = new Orders();
        LambdaUpdateWrapper<Orders> orderLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        if(map.get("status").equals(2)){
            orderLambdaQueryWrapper.eq(Orders::getId,map.get("id"));
            orders.setStatus(3);
            orderService.update(orders, orderLambdaQueryWrapper);
        }else if(map.get("status").equals(3)){
            orderLambdaQueryWrapper.eq(Orders::getId,map.get("id"));
            orders.setStatus(4);
            orderService.update(orders, orderLambdaQueryWrapper);
        }else if(map.get("status").equals(4)){
            orderLambdaQueryWrapper.eq(Orders::getId,map.get("id"));
            orders.setStatus(5);
            orderService.update(orders, orderLambdaQueryWrapper);
        }



        return R.success("设置成功");
    }
}


