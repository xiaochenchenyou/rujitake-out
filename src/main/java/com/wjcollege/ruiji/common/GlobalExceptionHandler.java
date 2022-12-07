package com.wjcollege.ruiji.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常处理类
 * @author chenWei
 * @date 2022/11/28 19:20
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R CatchException(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException){
        log.info(sqlIntegrityConstraintViolationException.getMessage());
        if(sqlIntegrityConstraintViolationException.getMessage().contains("Duplicate entry")){
            return R.error("添加失败，账号已经存在");
        }

        return R.error("服务器忙");

    }
}
