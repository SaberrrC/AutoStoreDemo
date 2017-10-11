package com.shanlin.android.autostore.entity.body;

/**
 * Created by cuieney on 16/08/2017.
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
