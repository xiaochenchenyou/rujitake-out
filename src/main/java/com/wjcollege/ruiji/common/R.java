package com.wjcollege.ruiji.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenWei
 * 通用返回结果类，用于数据的返回
 * @date 2022/11/27 13:33
 */
@Data
public class R<T> {
    private Integer code; //返回成功的代码，成功返回1，失败返回0
    private String msg; //返回错误信息
    private T data;//数据
    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
