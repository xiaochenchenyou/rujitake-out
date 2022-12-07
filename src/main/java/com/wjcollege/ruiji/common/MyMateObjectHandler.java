package com.wjcollege.ruiji.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author chenWei
 * @date 2022/11/30 15:27
 */
@Configuration
@Slf4j
public class MyMateObjectHandler implements MetaObjectHandler {


    /**
     * 插入填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        /*
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(attribute.getId());
        category.setUpdateUser(attribute.getId());
         */
        log.info("插入语句执行");
//        Employee attribute = (Employee)httpServletRequest.getSession().getAttribute("Employee");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getId());
        metaObject.setValue("updateUser",BaseContext.getId());
//        long id = Thread.currentThread().getId();
//        log.info("线程id：{}",id);
//
//        BaseContext baseContext = new BaseContext();
//        Long id1 = baseContext.getId();
//        System.out.println(id1);

    }

    /**
     * 更新插入
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getId());
//        log.info("更新语句执行");
//        long id = Thread.currentThread().getId();
//        log.info("线程id：{}",id);

//        BaseContext baseContext = new BaseContext();
//        Long id1 = baseContext.getId();
//        System.out.println("============================"+id1);
    }
}
