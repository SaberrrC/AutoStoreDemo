package com.shanlin.autostore.bean.paramsBean;

/**
 * Created by DELL on 2017/7/28 0028.
 */

public class RealOrderBody {

    /**
     * orderNo : orderNo
     */

    private String orderNo;

    public RealOrderBody(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
