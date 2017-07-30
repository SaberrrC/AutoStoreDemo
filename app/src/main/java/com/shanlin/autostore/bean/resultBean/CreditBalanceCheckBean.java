package com.shanlin.autostore.bean.resultBean;

/**
 * Created by DELL on 2017/7/27 0027.
 */

public class CreditBalanceCheckBean {


    /**
     * credit : 500
     * creditBalance : 100
     * creditLevel : 1
     * creditUsed : 400
     * status : 0
     */

    private String credit;
    private int creditBalance;
    private String creditLevel;
    private String creditUsed;
    private String status;

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public int getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(int creditBalance) {
        this.creditBalance = creditBalance;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public String getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(String creditUsed) {
        this.creditUsed = creditUsed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
