package com.retrieval.Server.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;

import com.google.gson.Gson;
import com.retrieval.Server.MSGService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MSGServiceImpl implements MSGService {

    @Override
    public boolean send(Map map, String phone) {
        if(StringUtils.isEmpty(phone)) return false;

        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId("***************************")
                // 您的AccessKey Secret	（这两个还不知道的去我前两次关于阿里云的有教程哪里找）
                .setAccessKeySecret("***************************");
        // 访问的域名（这个不用变都是这个）
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = null;
        try {
            client = new Client(config);
            SendSmsRequest request = new SendSmsRequest();

            request.setSignName("济南诚铭教育科技");//签名名称
            request.setTemplateCode("SMS_305835524");//模版Code
            request.setPhoneNumbers(phone);//电话号码
            //这里的参数是json格式的字符串
            request.setTemplateParam(JSONObject.toJSONString(map));
            SendSmsResponse response = client.sendSms(request);
            System.out.println("发送成功："+new Gson().toJson(response));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}



