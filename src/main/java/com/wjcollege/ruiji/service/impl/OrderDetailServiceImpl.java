package com.wjcollege.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjcollege.ruiji.entity.OrderDetail;
import com.wjcollege.ruiji.mapper.OrderDetailMapper;
import com.wjcollege.ruiji.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}