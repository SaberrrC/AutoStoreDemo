package com.shanlin.android.autostore.entity.body;

/**
 * Created by DELL on 2017/7/30 0030.
 */

public class RealNameAuthenBody {

   private String idCard;
   private String userName;

    public RealNameAuthenBody(String idCard, String userName) {
        this.idCard = idCard;
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
