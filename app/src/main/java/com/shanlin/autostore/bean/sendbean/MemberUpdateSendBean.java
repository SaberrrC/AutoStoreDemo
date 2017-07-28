package com.shanlin.autostore.bean.sendbean;

/**
 * Created by shanlin on 2017-7-28.
 */

public class MemberUpdateSendBean {

    //	头像；base64编码的二进制图片数据(可选)	string
    public String gender;
    //	性别(可选)
    public String idCard;
    //	身份证(可选)
    public String imageBase64;
    //	人脸识别照片；base64编码的二进制图片数据(可选)	string	@mock=测试内容g54k
    public String mobile;
    //	用户电话(可选)
    public String nickName;
    //	用户昵称(可选)
    public String userDeviceId;
    //	用户设备id
    public String userName;
    //	用户账号(可选)
    public String avatorBase64;

    public MemberUpdateSendBean(String userDeviceId) {
        this.userDeviceId = userDeviceId;
    }
}
