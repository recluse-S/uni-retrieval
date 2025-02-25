package com.retrieval.Server.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import java.security.KeyStore;
import java.util.Map;

@Service
public class WeChatService {

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.appSecret}")
    private String appSecret;

    private final RestTemplate restTemplate;

    @Autowired
    public WeChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


//    public Map<String, Object> getSessionKeyAndOpenId(String code) throws IOException {
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
//                "&secret=" + appSecret +
//                "&js_code=" + code +
//                "&grant_type=authorization_code";
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpGet httpGet = new HttpGet(url);
//            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
//                String result = EntityUtils.toString(response.getEntity());
//                ObjectMapper objectMapper = new ObjectMapper();
//                return objectMapper.readValue(result, Map.class);
//            }
//        }
//    }

    private CloseableHttpClient createHttpClient() throws Exception {
        // 创建 SSL 上下文
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null); // 默认信任库

        TrustManager[] trustManagers = tmf.getTrustManagers();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);

        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(sslFactory).build();
    }

    public Map<String, Object> getSessionKeyAndOpenId(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
                "&secret=" + appSecret +
                "&js_code=" + code +
                "&grant_type=authorization_code";

        try (CloseableHttpClient httpClient = createHttpClient()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String result = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(result, Map.class);
            }
        }
    }

//    public JSONObject getOpenid(String code) throws Exception {
//        // 构造请求 URL
//        String url = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/sns/jscode2session")
//                .queryParam("appid", appId)
//                .queryParam("secret", appSecret)
//                .queryParam("js_code", code)
//                .queryParam("grant_type", "authorization_code")
//                .toUriString();
//
//        // 发送请求并获取响应
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//
//        // 检查返回值是否正常
//        if (response.getStatusCode().is2xxSuccessful()) {
//            String responseBody = response.getBody();
//            if (responseBody != null) {
//                // 解析 JSON 响应
//                JSONObject jsonResponse = new JSONObject(responseBody);
//                if (jsonResponse.has("openid")) {
//                    return jsonResponse;  // 返回包含 openid 和 session_key 的 JSON
//                } else {
//                    throw new Exception("获取 openid 失败: " + jsonResponse.getString("errmsg"));
//                }
//            } else {
//                throw new Exception("微信接口返回为空");
//            }
//        } else {
//            throw new Exception("请求微信接口失败");
//        }
//    }

    public JSONObject getOpenid(String code) throws Exception {
        // 检查传入的 code 是否为空
        if (code == null || code.isEmpty()) {
            throw new Exception("code 不能为空");
        }

        JSONObject json = new JSONObject(code);
        String codeNew = json.getString("code");  // 提取 code 字段的值

        // 构造请求 URL，确保 js_code 是原始的 code 字符串，而不是 JSON 格式
        String url = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/sns/jscode2session")
                .queryParam("appid", appId)
                .queryParam("secret", appSecret)
                .queryParam("js_code", codeNew)  // 这里直接传递 code 字符串
                .queryParam("grant_type", "authorization_code")
                .toUriString();

        // 发送请求并获取响应
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String responseBody = response.getBody();

        // 检查返回值是否正常
        if (response.getStatusCode().is2xxSuccessful()) {
            if (responseBody != null) {
                // 解析 JSON 响应
                JSONObject jsonResponse = new JSONObject(responseBody);
                if (jsonResponse.has("openid")) {
                    return jsonResponse;  // 返回包含 openid 和 session_key 的 JSON
                } else {
                    // 如果没有 openid 字段，返回错误信息
                    String errmsg = jsonResponse.optString("errmsg", "未知错误");
                    throw new Exception("获取 openid 失败: " + errmsg);
                }
            } else {
                throw new Exception("微信接口返回为空");
            }
        } else {
            throw new Exception("请求微信接口失败");
        }
    }

}
