package com.wjcollege.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjcollege.ruiji.common.BaseContext;
import com.wjcollege.ruiji.common.R;
import com.wjcollege.ruiji.entity.User;
import com.wjcollege.ruiji.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author chenWei
 * @date 2022/12/3 23:48
 */
@RestController
@Slf4j
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 移动端用户登录
     * @param map
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public R<User> longin(@RequestBody Map map, HttpSession httpSession){
        String phone = map.get("phone").toString();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,phone);



        //查看该用户是否为新用户
        User user = userService.getOne(userLambdaQueryWrapper);


        if(user == null){
            //直接添加到数据库
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);


        }
        //向session中存储用户信息
        httpSession.setAttribute("user",user.getId());
        BaseContext.setId(user.getId());
        return R.success(user);

//        return R.error("登陆失败");
    }


    @PostMapping("/loginout")
    public R exitUser(HttpSession httpSession){
        //或许当前id
        Long id = BaseContext.getId();
        httpSession.removeAttribute("user");

        return R.success("退出成功");

    }



}
