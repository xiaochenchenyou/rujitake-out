package com.wjcollege.ruiji.common;

import com.alibaba.fastjson.JSON;
import com.wjcollege.ruiji.entity.Employee;
import com.wjcollege.ruiji.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Lang;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author chenWei
 * @date 2022/11/27 23:59
 */
@Configuration
@Slf4j
@WebFilter(filterName = "loginFilter",urlPatterns = "/*")

public class myFilter implements Filter {



    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        log.info("拦截到的url:"+request.getRequestURI());
//        long id = Thread.currentThread().getId();
//        log.info("线程id：{}",id);
        //本次请求的路径
        String requestURI = request.getRequestURI();


        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg",
                "/common/**"
        };

        //判断请求是否包含在数组中
        boolean contain = check(urls, requestURI);

        //在放行路径或者已经登录
        //3、如果不需要处理，则直接放行
        if(contain){
//            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("Employee") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("Employee"));

            Employee employee = (Employee)request.getSession().getAttribute("Employee");

            BaseContext.setId(employee.getId());

            filterChain.doFilter(request,response);
            return;
        }

        //4-2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("=================用户已登录，用户id为：{}",request.getSession().getAttribute("user")+"========================");

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

