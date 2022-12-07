package com.wjcollege.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjcollege.ruiji.entity.User;
import com.wjcollege.ruiji.mapper.UserMapper;
import com.wjcollege.ruiji.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author chenWei
 * @date 2022/12/3 23:45
 */
@Service
public class UserImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
