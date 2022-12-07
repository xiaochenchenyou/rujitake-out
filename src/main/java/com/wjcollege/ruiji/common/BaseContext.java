package com.wjcollege.ruiji.common;

/**
 * @author chenWei
 * @date 2022/11/30 16:00
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();


    public static void setId(Long id){
        threadLocal.set(id);
    }

    public static Long getId(){
        return threadLocal.get();
    }

}
