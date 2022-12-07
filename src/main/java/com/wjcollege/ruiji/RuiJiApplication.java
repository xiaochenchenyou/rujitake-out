package com.wjcollege.ruiji;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author chenWei
 * @date 2022/11/27 0:23
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
public class RuiJiApplication {
    public static void main(String[] args) {

        SpringApplication.run(RuiJiApplication.class,args);
        log.info("启动成功");


    }
}




