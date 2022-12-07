package com.wjcollege.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wjcollege.ruiji.entity.ShoppingCart;
import com.wjcollege.ruiji.mapper.ShoppingCartMapper;
import com.wjcollege.ruiji.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
