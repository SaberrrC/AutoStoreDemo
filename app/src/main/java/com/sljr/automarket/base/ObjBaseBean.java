package com.sljr.automarket.base;

/**
 * Created by JR on 2017/7/15.
 */

public class ObjBaseBean<T> {


    /**
     * data : {}
     */

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
