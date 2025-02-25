package com.retrieval.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx")
@Data
@ToString
public class WxPayV3Bean {
    //小程序appid
    private String appId;
    //商户号
    private String mchId;
    //证书序列号
    private String mchSerialNo;
    //小程序秘钥
    private String appSecret;
    //api秘钥
    private String apiKey;
    //回调接口地址
    private String notifyUrl;
    //证书地址（公有）
    private String keyPath;
    //证书地址（私有）
    private String privateKeyPath;
    //公钥id
    private String publicKeyId;

}
