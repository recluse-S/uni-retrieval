package com.retrieval.controller;

import com.alibaba.fastjson.JSONObject;
import com.retrieval.Server.LoginServer;
import com.retrieval.pojo.JwtUtils;
import com.retrieval.pojo.R;
import com.retrieval.pojo.Result;
import com.retrieval.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class LoginController {

    private final String appId = "wx7b013dd72070f493"; //你的小程序appid
    private final String secret = "##################"; //你的小程序secret

    @Autowired
    private LoginServer loginServer;

//    @PostMapping("/login")
//    public Result Login(@RequestBody User user){
//        User s = loginServer.login(user);
//        if (s!=null){
//            Map<String ,Object> claims = new HashMap<>();
//            claims.put("id",s.getId()) ;
//            claims.put("name",s.getName());
//            String token = JwtUtils.generateJwt(claims);
//            return Result.success(token);
//        }
//        return Result.error("用户名或密码错误");
//    }

//    @PostMapping("/register")
//    public Result register(@RequestBody User user) {
//
//        return loginServer.register(user);
//    }



}
