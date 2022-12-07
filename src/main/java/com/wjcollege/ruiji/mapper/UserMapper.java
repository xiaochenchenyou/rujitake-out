package com.wjcollege.ruiji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjcollege.ruiji.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenWei
 * @date 2022/12/3 23:45
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
