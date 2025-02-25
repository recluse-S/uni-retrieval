package com.retrieval.Server;

import com.retrieval.pojo.User;

public interface LoginServer {
    User login(User user);

    void register(User user);

    void getcode(String code);

    User getUserByPhoneNumber(String phoneNumber);

    // 更新用户的 token 和 openid
    void updateToken(Integer userId, String token, String openid);

//    void createUser(User user);
}
