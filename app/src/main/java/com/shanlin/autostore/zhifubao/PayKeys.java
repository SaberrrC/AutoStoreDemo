package com.shanlin.autostore.zhifubao;

/**
 * Created by Administrator on 2015/9/28.
 * 提示：如何获取安全校验码和合作身份者id
 * 1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 * 2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 * 3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */
public class PayKeys {
    //
    // 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
    // 这里签名时，只需要使用生成的RSA私钥。
    // Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。

    public static final String APPID = "2017071407758055";
    //合作身份者id，以2088开头的16位纯数字 此id用来支付时快速登录
    public static final String DEFAULT_PARTNER = "2088721531049710";
    //收款支付宝账号
    public static final String DEFAULT_SELLER = "wrbl@linjia-cvs.com";
    //商户私钥，自助生成，在压缩包中有openssl，用此软件生成商户的公钥和私钥，写到此处要不然服务器返回错误。公钥要传到淘宝合作账户里详情请看淘宝的sdk文档
    public static final String  PRIVATE =
            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCqxjKFIhkC6F1K2idhrFCHR4TB/LqU8mP+HHbLkhKiNULrAlDJlDHY9DwnF+v71GIjO7AgBDWGIskduIqxScLe0R9fksnJ2b3rrN9pJ5faWvM7qfV1ecxu/lNB/gMBAa4Vo+zqxaT37ZfhMMo0IK47fsPUBC2tGiF47zzw9Qodyz6cCCYJ5T/RlwLCJx2l1itMOUQfmb2OQbf4EW2cY9yjLwTRLzrt0FLrhjpt2ZikMOSkqs7+mytufNc441lwo98/3v6FFxJHH1pDl04oiBGt/8nY1uJk7timxRIF/6QmnNFzTpr6p4BIE6cfNHOSIpWJIn+vvmm5Wn47IUObfUHzAgMBAAECggEAIMnTE1q7oKJKnz+JhihWgdLKe5Bxpn1vk2+Vo3pVV42QXpzgNAPfGj+yePMP3RcnDIq+Qzdrq2hsYud2wZle7/cI5gnB8p5655AWiRMQBd2mi9MTDM4o4Tq1c5s4qm3PJdqGd4EhLnUQzyzV9fI8YbHyGXf/6Rd2S8jrU3xIx/VXaqoERtgDn/KXrkt22gNVI/no2erPDBy3xrOE+u6E7UgwuCdeeD4EFuQ26a++0RiWuQ5Ws8GsngJtU4XNcFJjlUjESU6jLuZ6HPvwJBl2wz64mE1lqcrTCvnzYdlPT4atdshvIsXi+yFm7QvicaQoRETEwT/fzUe6xtpDlKc2kQKBgQD57QCnUpVTmIGm9K4MZ1KcOCNRGXOIVxoNpEU4Nz2CBhU1KN7uqIkrPHlCwLMWQ3ha8F3jlPfVUfR59lKvg/EP92w7y4ke2JnIPn2HE5cYU9qLk7EAV4rvGchYne+iMNTdlMdzlhsuT3JDRlUDKYmHPvnt9uYRqHL91iOhph2QmQKBgQCu7LoAnwOzgIz5ULHToU4W39v9fB2vH66DgC8sNdursTL/haTmVIbZIkxi9QP+fChrEnSY2d+MuT+lDuggmAv7vkdk30u9FFwrELdyuyWb7yl/o0i21fsuGMb3N+mSqAscgk55gCfcrrWnBFVZJMarxV3U68FvQY76zEfxnEmiawKBgF4DroRCx5N7wgHY9wQJdgT4BSkDxvcZtN4+1h8QgJ3BxgaKSlpCod7Wz9KxhGyykrq6wsbSiev/+VRLucz2eB8GdvuDVCB7YFX4U+YVE3h+Sq7IrwOu6r+I1lmnh7ZeYiS8S88KP01La3MtKNuYq3hp00CV6PPphuA/xoWPZH25AoGATJvEgk0h89VS6EWc+9BjQMVdtVBMN8wqQ9E516TmxfTfHuVK6M0QGbxZs+UWkLeThAEbK24P57Oh2goDQ3MTnHcjEVxx8ScfjoTNsuOffwFpLUs3p0tjrDaRvOex1dbetueisrebvZyOVc0iC959W/EePQfbOXx+v/wDbh9CaG0CgYBLL7TNybP6C7f+7HGRiHQFEMjzpq2ztqE04UOFN1OdyUDobUIv2h8624PY43PHwvSDm9v98QjRDJxXoDVuS2NqqLKjaB/X6dqOYm7s6eTDSjmPAvx3rlrbJDvCdljO4xP43DLHGl/7gqO36ruJKT58FA8D3Wms9aI9JLcMSm98eg==";
    //公钥
    public static final String PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqsYyhSIZAuhdStonYaxQh0eEwfy6lPJj/hx2y5ISojVC6wJQyZQx2PQ8Jxfr+9RiIzuwIAQ1hiLJHbiKsUnC3tEfX5LJydm966zfaSeX2lrzO6n1dXnMbv5TQf4DAQGuFaPs6sWk9+2X4TDKNCCuO37D1AQtrRoheO888PUKHcs+nAgmCeU/0ZcCwicdpdYrTDlEH5m9jkG3+BFtnGPcoy8E0S867dBS64Y6bdmYpDDkpKrO/psrbnzXOONZcKPfP97+hRcSRx9aQ5dOKIgRrf/J2NbiZO7YpsUSBf+kJpzRc06a+qeASBOnHzRzkiKViSJ/r75puVp+OyFDm31B8wIDAQAB";
}

