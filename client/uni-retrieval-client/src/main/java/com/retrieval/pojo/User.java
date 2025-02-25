package com.retrieval.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user")
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String password;
    private String nickname;
    private String phone;
    private String information;
//    private String code;
    private String school;
    private String station;
    private LocalDateTime createdTime;
    private LocalDateTime upTime;
    private Integer usableTimes;
    private String openid;  // 微信的 openId
    private String token;   // token

}
