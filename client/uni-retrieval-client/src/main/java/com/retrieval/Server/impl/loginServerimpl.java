package com.retrieval.Server.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.retrieval.Mapper.LoginMapper;
import com.retrieval.Mapper.UserMapper;
import com.retrieval.Server.LoginServer;
import com.retrieval.pojo.R;
import com.retrieval.pojo.Result;
import com.retrieval.pojo.User;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
public class loginServerimpl implements LoginServer {
    private String code;

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public User login(User user) {
        User s = loginMapper.login(user);
        return s;
    }

    @Override
    public void register(User user) {
        // 检查用户名是否存在
//        int count = loginMapper.countByUsername(user.getName());
//        if (count > 0) {
//            // 用户名已存在
//            return Result.error("用户名重复，请换一个吧~");
//        }
//        System.out.println(this.code);
//        if (!user.getCode().equals(this.code) ){
//            return Result.error("验证码错误，请重新输入");
//        }
//        loginMapper.register(user);
//        return Result.success("注册成功");
        loginMapper.register(user);
    }

    @Override
    public void getcode(String code) {
       this.code= code;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        User s = loginMapper.loginByPhone(phoneNumber);
        return s;
    }

    @Override
    // 更新用户的 token 和 openid
    public void updateToken(Integer userId, String token, String openid) {
        loginMapper.updateToken(userId, token, openid);
    }

}
