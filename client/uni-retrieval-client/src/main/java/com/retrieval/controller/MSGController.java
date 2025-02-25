package com.retrieval.controller;




import com.retrieval.Server.LoginServer;
import com.retrieval.Server.MSGService;
import com.retrieval.Util.RandomUtil;
import com.retrieval.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sends")
public class MSGController {

    @Autowired
    private MSGService jwtService;
    @Autowired
    private  LoginServer loginServer;

    @GetMapping("send/{phone}")
    public void sendMsm(@PathVariable String phone){
        //生成随机数
        String   code = RandomUtil.getFourBitRandom();
        Map map = new HashMap();
        map.put("code",code);
        loginServer.getcode(code);
        boolean b = jwtService.send(map,phone);
    }
}


