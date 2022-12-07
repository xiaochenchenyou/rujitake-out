package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.Employee;
import com.wjcollege.ruiji.service.AddressBookService;
import com.wjcollege.ruiji.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.wjcollege.ruiji.common.R.success;

/**
 * @author chenWei
 * @date 2022/11/27 13:21
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    AddressBookService addressBookService;


    /**
     * 登录判断
     * @param employee
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest httpServletRequest){

        String password = employee.getPassword();
        //处理密码的加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //查询条件,无需比对密码，username唯一，不重复
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        Employee selectOne = employeeService.getOne(queryWrapper);

        if(selectOne==null) return R.error("无此用户");
        //判断密码
        if(!selectOne.getPassword().equals(password)){
            return R.error("密码错误");
        }else if(!selectOne.getStatus().equals(1)) {
            return R.error("账号禁用");
        }
//        }else if(httpServletRequest.getSession().getAttribute("Employee")!=null){
//            return R.error("账号已在别处登陆了");
//        }
        httpServletRequest.getSession().setAttribute("Employee",selectOne);

        return success(selectOne);

    }

    /**
     * 安全退出
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().removeAttribute("Employee");

        return R.success("成功退出");

    }


    /**
     * 分页查询
     * @param page 第几页
     * @param pageSize 每页显示的数字
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(name!=null,Employee::getName,name);



        //分页构造器,在上面查询结果有数据的时候，才会分页，否则不会分页
        Page<Employee> employeePage = new Page(page,pageSize);
        employeeService.page(employeePage,queryWrapper);
        System.out.println(employeePage.getRecords());


        return R.success(employeePage);
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> insertEmployee(@RequestBody Employee employee ,HttpServletRequest httpServletRequest){

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Employee attribute =(Employee) httpServletRequest.getSession().getAttribute("Employee");
//
//        employee.setCreateUser(attribute.getId());
//        employee.setUpdateUser(attribute.getId());

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

        employeeService.save(employee);

        return R.success("添加成功");

    }

    /**
     * 修改员工权限
     * @param employee
     * @param httpServletRequest
     * @return
     */
    @PutMapping
    public R<String> selectById(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        Employee attribute = (Employee) httpServletRequest.getSession().getAttribute("Employee");
        employee.setUpdateUser(attribute.getUpdateUser());
        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return R.success("添加成功");
    }

    /**
     * 填充修改员工信息表单
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> selectDateById(@PathVariable long id){

        return R.success(employeeService.getById(id));

    }

}
