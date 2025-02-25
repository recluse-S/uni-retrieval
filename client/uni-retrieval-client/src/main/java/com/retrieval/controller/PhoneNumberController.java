package com.retrieval.controller;

import com.alibaba.fastjson.JSONObject;
import com.retrieval.Server.LoginServer;
import com.retrieval.Server.impl.WeChatService;
import com.retrieval.Util.AesDecryptor;
import com.retrieval.Util.WxLoginUtil;
import com.retrieval.model.DecryptRequest;
import com.retrieval.model.Response;
import com.retrieval.pojo.JwtUtils;
import com.retrieval.pojo.Result;
import com.retrieval.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/client/login")
public class PhoneNumberController {

    @Autowired
    private LoginServer loginServer;

    @Autowired
    private WeChatService weChatService;

//    @PostMapping("/decryptPhoneNumber")
//    public Response decryptPhoneNumber(@RequestBody DecryptRequest request) {
//        try {
//            String sessionKey = WxLoginUtil.getSessionKey(request.getCode());
//            if (sessionKey == null) {
//                throw new RuntimeException("Failed to get session_key from WeChat server.(未能从WeChat中成功获取密钥)");
//            }
//            String decryptedData = AesDecryptor.decrypt(request.getEncryptedData(), sessionKey, request.getIv());
//            JSONObject jsonObject = JSONObject.parseObject(decryptedData);
//            String phoneNumber = jsonObject.getString("phoneNumber");
//
//            User user = loginServer.getUserByPhoneNumber(phoneNumber);
//            if (user == null) {
//                // 如果用户不存在，创建新用户
//                user = new User();
//                user.setPhone(phoneNumber);
//                loginServer.register(user); // 假设有一个创建用户的方法
//                // 注册成功后，立即生成token并返回
//                user = loginServer.getUserByPhoneNumber(phoneNumber);
//                String token = generateToken(user);
//                return new Response(200, "注册成功", new TokenResponse(token));
//            } else {
//                // 如果用户已经存在，直接返回token
//                String token = generateToken(user);
//                return new Response(200, "登录成功", new TokenResponse(token));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Response(500, "解密失败", null);
//        }
//    }

    @PostMapping("/decryptPhoneNumber")
    public Response decryptPhoneNumber(@RequestBody DecryptRequest request) {
        try {
            // 从微信服务器获取sessionKey和openid
            String sessionKey = WxLoginUtil.getSessionKey(request.getCode());

            if (sessionKey == null) {
                throw new RuntimeException("未能从WeChat中成功获取密钥，code可能无效或过期");
            }

            // 解密数据获取手机号
            String decryptedData = AesDecryptor.decrypt(request.getEncryptedData(), sessionKey, request.getIv());
            JSONObject jsonObject = JSONObject.parseObject(decryptedData);
            String phoneNumber = jsonObject.getString("phoneNumber");

            // 获取openid
            String openid = request.getOpenid();
//            String openid = jsonObject.getString("openid");
//            System.out.println("--------------------->获取openid: " +openid);

            // 继续处理用户注册或登录逻辑
            User user = loginServer.getUserByPhoneNumber(phoneNumber);
            if (user == null) {
                // 用户不存在，注册新用户
                user = new User();
                user.setPhone(phoneNumber);
                user.setOpenid(openid);
                loginServer.register(user);

                // 获取注册后的用户信息
                user = loginServer.getUserByPhoneNumber(phoneNumber);
            }

            // 生成新的 token
            String token = generateToken(user);

            // 将 token 和 openid 存入数据库
            loginServer.updateToken(user.getId(), token, openid);

            // 返回成功的响应
            return new Response(200, "登录/注册成功", new TokenResponse(token));

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "解密失败：" + e.getMessage(), null);
        }
    }

    @PostMapping("/getOpenid")
    public ResponseEntity<?> getOpenid(@RequestBody String code) {
        try {
            org.json.JSONObject openidData = weChatService.getOpenid(code);
            return ResponseEntity.ok(openidData.toString());  // 返回获取的 openid 数据
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result Login(@RequestBody User user) {
        User s = loginServer.login(user);
        if (s != null) {
            String token = generateToken(s);
            return Result.success(token);
        }
        return Result.error("用户名或密码错误");
    }

    private String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phone", user.getPhone());
        claims.put("id", user.getId());
        return JwtUtils.generateJwt(claims);
    }

    // 内部类，用于封装 token 返回
    private static class TokenResponse {
        private String token;
        public TokenResponse(String token) {
            this.token = token;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }
}