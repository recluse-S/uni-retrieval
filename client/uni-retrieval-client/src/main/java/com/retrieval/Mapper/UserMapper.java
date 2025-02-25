package com.retrieval.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.retrieval.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
// 假设 User 是你用户表对应的实体类
public interface UserMapper extends BaseMapper<User> {

    // 可以根据需求添加其他查询方法
    @Insert("INSERT INTO user(name, password,phone,open_id,created_time) VALUES (#{name}, #{password},#{phone},#{openid},sysdate()) ")
    int register(User user);

    Map<String, Object> getOpenIdByToken(String token);
}

