package com.retrieval.Mapper;

import com.retrieval.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginMapper {

    @Select("SELECT * FROM user WHERE name = #{name}")
    User login(User user);

    @Insert("INSERT INTO user(phone, open_id, created_time, usable_times) VALUES (#{phone}, #{openid}, sysdate(), 5) ")
    int register(User user);

    // 检查用户名是否存在
    @Select("SELECT COUNT(*) FROM user WHERE name = #{name}")
    int countByUsername(String name);

    @Select("SELECT * FROM user WHERE phone = #{phoneNumber}")
    User loginByPhone(String phoneNumber);

    @Update("UPDATE user SET token = #{token}, open_id = #{openid} WHERE id = #{userId}")
    void updateToken(@Param("userId") Integer userId, @Param("token") String token, @Param("openid") String openid);
}
