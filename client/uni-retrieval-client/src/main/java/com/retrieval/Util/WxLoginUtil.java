package com.retrieval.Util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class WxLoginUtil {

    @Value("${wechat.appId}")
    private static String appId;

    @Value("${wechat.appSecret}")
    private static String appSecret;

    private static final OkHttpClient client = new OkHttpClient();

    public static String getSessionKey(String code) throws IOException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=wx7b013dd72070f493&" +
                "secret=######################&" +
                "js_code=" + code +
                "&grant_type=authorization_code";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code();
            String jsonData = response.body().string();

            if (statusCode != 200) {
                throw new RuntimeException("请求微信API失败，HTTP 状态码：" + statusCode + ", 响应内容：" + jsonData);
            }

            // 解析响应数据
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            if (jsonObject.containsKey("errcode")) {
                String errcode = jsonObject.getString("errcode");
                String errmsg = jsonObject.getString("errmsg");
                throw new RuntimeException("获取session_key失败，错误码：" + errcode + ", 错误信息：" + errmsg);
            }

            return jsonObject.getString("session_key");
        }
    }

}
