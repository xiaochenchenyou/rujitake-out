package com.wjcollege.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjcollege.ruiji.entity.Employee;
import com.wjcollege.ruiji.mapper.EmployeeMapper;
import com.wjcollege.ruiji.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author chenWei
 * @date 2022/11/27 13:17
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

}
