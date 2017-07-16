package com.shanlin.autostore.base;

import java.util.List;

/**
 * Created by JR on 2017/7/15.
 */

public class ListBaseBean<T> {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
